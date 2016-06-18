package com.homer.web;

import com.homer.auth.stormpath.StormpathAuthService;
import com.homer.data.*;
import com.homer.email.aws.AWSEmailService;
import com.homer.external.rest.mlb.MLBRestClient;
import com.homer.service.*;
import com.homer.service.auth.UserService;
import com.homer.service.full.FullVultureService;
import com.homer.service.full.IFullVultureService;
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
    private EnvironmentUtility envUtil;

    public SchedulingManager() {
        envUtil = EnvironmentUtility.getInstance();
        playerService = new PlayerService(new PlayerRepository());
        playerSeasonService = new PlayerSeasonService(new PlayerSeasonRepository());
        playerImporter = new PlayerImporter(
                new PlayerService(new PlayerRepository()),
                playerSeasonService,
                new MLBRestClient()
        );

        fullVultureService = new FullVultureService(
                new VultureService(new VultureRepository()),
                playerSeasonService,
                new TeamService(new TeamRepository()),
                playerService,
                new UserService(StormpathAuthService.FACTORY.getInstance(), new SessionTokenRepository()),
                new AWSEmailService(),
                new Scheduler());
    }

    public void run() {
        update40ManRosters();
        updatePlayers();

        rescheduleVultures();
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
}
