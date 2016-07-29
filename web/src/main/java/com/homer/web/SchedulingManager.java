package com.homer.web;

import com.homer.auth.stormpath.StormpathAuthService;
import com.homer.data.*;
import com.homer.email.IEmailService;
import com.homer.email.aws.AWSEmailService;
import com.homer.external.common.IMLBClient;
import com.homer.external.rest.espn.ESPNRestClient;
import com.homer.external.rest.mlb.MLBRestClient;
import com.homer.service.*;
import com.homer.service.auth.UserService;
import com.homer.service.full.FullPlayerService;
import com.homer.service.full.FullVultureService;
import com.homer.service.full.IFullPlayerService;
import com.homer.service.full.IFullVultureService;
import com.homer.service.gather.Gatherer;
import com.homer.service.gather.IGatherer;
import com.homer.service.importer.IPlayerImporter;
import com.homer.service.importer.PlayerImporter;
import com.homer.service.schedule.Scheduler;
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

    private IPlayerImporter playerImporter;
    private IPlayerSeasonService playerSeasonService;
    private IPlayerService playerService;
    private IFullVultureService fullVultureService;
    private Validator validator;
    private EnvironmentUtility envUtil;
    private IFullPlayerService fullPlayerService;
    private ITransactionService transactionService;

    public SchedulingManager() {
        envUtil = EnvironmentUtility.getInstance();
        playerService = new PlayerService(new PlayerRepository());
        playerSeasonService = new PlayerSeasonService(new PlayerSeasonRepository());

        IMLBClient mlbClient = new MLBRestClient();
        playerImporter = new PlayerImporter(
                new PlayerService(new PlayerRepository()),
                playerSeasonService,
                mlbClient
        );

        ITeamService teamService = new TeamService(new TeamRepository());
        IEmailService emailService = new AWSEmailService();
        fullVultureService = new FullVultureService(
                new VultureService(new VultureRepository()),
                playerSeasonService,
                teamService,
                playerService,
                new UserService(StormpathAuthService.FACTORY.getInstance(), new SessionTokenRepository()),
                emailService,
                new Scheduler());

        IGatherer gatherer = new Gatherer(playerService, teamService, playerSeasonService,
                new DraftDollarService(new DraftDollarRepository()), new MinorLeaguePickService(new MinorLeaguePickRepository()),
                new TradeService(new TradeRepository()), new TradeElementService(new TradeElementRepository()));

        validator = new Validator(teamService, gatherer, emailService);

        fullPlayerService = new FullPlayerService(playerService, playerSeasonService, mlbClient);

        transactionService = new TransactionService(new TransactionRepository(),
                playerService, playerSeasonService, new ESPNRestClient(), new AWSEmailService());
    }

    public void run() {
        update40ManRosters();
        updatePlayers();

        rescheduleVultures();

        validateRosters();

        updateMinorLeaguerStatusForPlayers();

        processTransactions();
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
}
