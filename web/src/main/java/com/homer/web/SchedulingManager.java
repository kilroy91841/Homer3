package com.homer.web;

import com.homer.service.*;
import com.homer.service.full.IFullPlayerService;
import com.homer.service.full.IFullVultureService;
import com.homer.service.importer.IPlayerImporter;
import com.homer.type.MLBTeam;
import com.homer.type.Player;
import com.homer.type.PlayerSeason;
import com.homer.type.Vulture;
import com.homer.type.view.PlayerView;
import com.homer.util.EnvironmentUtility;
import com.homer.util.core.$;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.joda.time.DateTime;
import org.slf4j.*;

/**
 * Created by arigolub on 4/28/16.
 */
public class SchedulingManager {

    final static Logger logger = LoggerFactory.getLogger(SchedulingManager.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    private final ServiceFactory serviceFactory = ServiceFactory.getInstance();

    private IPlayerImporter playerImporter;
    private IPlayerSeasonService playerSeasonService;
    private IPlayerService playerService;
    private IFullVultureService fullVultureService;
    private Validator validator;
    private EnvironmentUtility envUtil;
    private IFullPlayerService fullPlayerService;
    private ITransactionService transactionService;
    private IStandingService standingsService;

    public SchedulingManager() {
        envUtil = EnvironmentUtility.getInstance();
        playerService = serviceFactory.get(IPlayerService.class);
        playerSeasonService = serviceFactory.get(IPlayerSeasonService.class);
        playerImporter = serviceFactory.get(IPlayerImporter.class);
        fullVultureService = serviceFactory.get(IFullVultureService.class);
        validator = serviceFactory.get(Validator.class);
        fullPlayerService = serviceFactory.get(IFullPlayerService.class);
        transactionService = serviceFactory.get(ITransactionService.class);
        standingsService = serviceFactory.get(IStandingService.class);
    }

    public void run() {
        update40ManRosters();
        updatePlayers();

        rescheduleVultures();

        validateRosters();

        updateMinorLeaguerStatusForPlayers();

        processTransactions();

        updateStandings();
    }

    private ScheduledFuture processTransactions() {
        Runnable runnable = processTransactionsRunnable(transactionService);
        DateTime now = DateTime.now();
        int offsetMinutes = 60 - now.getMinuteOfHour();
        return scheduler.scheduleAtFixedRate(runnable, offsetMinutes, 60, TimeUnit.MINUTES);
    }

    private ScheduledFuture validateRosters() {
        Runnable runnable = validateRostersRunnable(validator);
        return scheduler.scheduleAtFixedRate(runnable,
                1,
                1440,
                TimeUnit.MINUTES);
    }

    private ScheduledFuture updateMinorLeaguerStatusForPlayers() {
        Runnable runnable = updateMinorLeaguerStatusForPlayersRunnable(fullPlayerService);
        return scheduler.scheduleAtFixedRate(runnable,
                5,
                1440,
                TimeUnit.MINUTES);
    }

    private void rescheduleVultures() {
        List<Vulture> vultures = fullVultureService.getInProgressVultures();
        vultures.forEach(v -> fullVultureService.scheduleVulture(v));
    }

    public ScheduledFuture update40ManRosters() {
        Runnable runnable = update40ManRostersRunnable(playerImporter, x -> {
            return;
        });
        return scheduler.scheduleAtFixedRate(
                runnable,
                envUtil.getUpdate40ManRostersDelay(),
                envUtil.getUpdate40ManRostersPeriod(),
                TimeUnit.MINUTES
        );
    }

    public ScheduledFuture updatePlayers() {
        Runnable runnable = updatePlayersRunnable(playerImporter, playerSeasonService, playerService, x -> {
            return;
        });
        return scheduler.scheduleAtFixedRate(
                runnable,
                envUtil.getUpdatePlayersDelay(),
                envUtil.getUpdatePlayersPeriod(),
                TimeUnit.MINUTES
        );
    }

    public ScheduledFuture updateStandings() {
        long delayMinutes;
        DateTime now = DateTime.now();
        if (now.getHourOfDay() <= 6) {
            delayMinutes = 420 - now.getMinuteOfDay();
        } else {
            delayMinutes = 1440 - (now.getMinuteOfDay() - 420);
        }
        logger.info("Delay minutes: " + delayMinutes);
        Runnable runnable = updateStandingsRunnable(standingsService);
        return scheduler.scheduleAtFixedRate(runnable,
                delayMinutes,
                1440,
                TimeUnit.MINUTES
        );
    }

    public static Runnable update40ManRostersRunnable(IPlayerImporter playerImporter,
                                                      Consumer<List<PlayerView>> consumer) {
        return () -> {
            try {
                logger.info("BEGIN: update40ManRostersRunnable");
                for (MLBTeam team : MLBTeam.values()) {
                    if (team == MLBTeam.FREEAGENT) {
                        continue;
                    }
                    consumer.accept(playerImporter.update40ManRoster(team.getId()));
                }
                logger.info("END: update40ManRostersRunnable");
            } catch (Exception e) {
                logger.error(String.format("ERROR: update40ManRostersRunnable, %s %s", e.getMessage(), e.getStackTrace().toString()));
            }
        };
    }

    public static Runnable updatePlayersRunnable(IPlayerImporter playerImporter,
                                                 IPlayerSeasonService playerSeasonService,
                                                 IPlayerService playerService,
                                                 Consumer<PlayerView> consumer) {
        return () -> {
            try {
                logger.info("BEGIN: updatePlayersRunnable");
                List<PlayerSeason> playerSeasons = playerSeasonService.getActivePlayers();
                List<Long> playerIds = $.of(playerSeasons).toList(PlayerSeason::getPlayerId);
                List<Player> players = playerService.getByIds(playerIds);
                for (Player player : players) {
                    try {
                        consumer.accept(playerImporter.updatePlayer(player));
                    } catch (Exception e) {
                        logger.info(String.format(
                                "Error updating %s : \n%s", player.getName(), e.getMessage()));
                    }
                }
                logger.info("END: updatePlayersRunnable");
            } catch (Exception e) {
                logger.error(String.format("ERROR: updatePlayersRunnable, %s %s", e.getMessage(), e.getStackTrace().toString()));
            }
        };
    }

    public static Runnable validateRostersRunnable(Validator validator) {
        return () -> {
            try {
                validator.validateTeams();
            } catch (Exception e) {
                logger.error(String.format("ERROR: validateTeams, %s %s", e.getMessage(), e.getStackTrace().toString()));
            }
        };
    }

    public static Runnable updateMinorLeaguerStatusForPlayersRunnable(IFullPlayerService fullPlayerService) {
        return () -> {
            try {
                fullPlayerService.updateMinorLeaguerStatusForPlayers();
            } catch (Exception e) {
                logger.error(String.format("ERROR: updateMinorLeaguerStatusForPlayers, %s %s", e.getMessage(), e.getStackTrace().toString()));
            }
        };
    }

    public static Runnable processTransactionsRunnable(ITransactionService transactionService) {
        return () -> {
            try {
                transactionService.processDailyTransactions();
            } catch (Exception e) {
                logger.error(String.format("ERROR: processTransactions, %s %s", e.getMessage(), e.getMessage().toString()));
            }
        };
    }

    public static Runnable updateStandingsRunnable(IStandingService standingService) {
        return () -> {
            try {
                DateTime date = DateTime.now().withMillisOfDay(0);
                standingService.computeStandingsForDate(date, true);
            } catch (Exception e) {
                logger.error(String.format("ERROR: updateStandings, %s", e.getMessage()), e);
            }
        };
    }
}
