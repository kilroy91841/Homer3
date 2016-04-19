package com.homer.web;

import com.homer.data.PlayerRepository;
import com.homer.data.PlayerSeasonRepository;
import com.homer.external.rest.mlb.MLBRestClient;
import com.homer.service.IPlayerSeasonService;
import com.homer.service.IPlayerService;
import com.homer.service.PlayerSeasonService;
import com.homer.service.PlayerService;
import com.homer.service.importer.IPlayerImporter;
import com.homer.service.importer.PlayerImporter;
import com.homer.type.MLBTeam;
import com.homer.type.Player;
import com.homer.type.PlayerSeason;
import com.homer.util.EnvironmentUtility;
import com.homer.util.core.$;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by arigolub on 4/28/16.
 */
public class Scheduler {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private IPlayerImporter playerImporter;
    private IPlayerSeasonService playerSeasonService;
    private IPlayerService playerService;
    private EnvironmentUtility envUtil;

    public Scheduler() {
        envUtil = EnvironmentUtility.getInstance();
        playerService = new PlayerService(new PlayerRepository());
        playerSeasonService = new PlayerSeasonService(new PlayerSeasonRepository());
        playerImporter = new PlayerImporter(
                new PlayerService(new PlayerRepository()),
                playerSeasonService,
                new MLBRestClient()
        );
    }

    public void run() {
        update40ManRosters();
        updatePlayers();
    }

    public ScheduledFuture update40ManRosters() {
        Runnable runnable = () -> {
                for (MLBTeam team : MLBTeam.values()) {
                    if (team == MLBTeam.FREEAGENT) {
                        continue;
                    }
                    playerImporter.update40ManRoster(team.getId());
                }
            };
        return scheduler.scheduleAtFixedRate(
                runnable,
                envUtil.getUpdate40ManRostersDelay(),
                envUtil.getUpdate40ManRostersPeriod(),
                TimeUnit.MINUTES
        );
    }

    public ScheduledFuture updatePlayers() {
        Runnable runnable = () -> {
            List<PlayerSeason> playerSeasons = playerSeasonService.getActivePlayers();
            List<Long> playerIds = $.of(playerSeasons).toList(PlayerSeason::getPlayerId);
            List<Player> players = playerService.getByIds(playerIds);
            for (Player player : players) {
                playerImporter.updatePlayer(player);
            }
        };
        return scheduler.scheduleAtFixedRate(
                runnable,
                envUtil.getUpdatePlayersDelay(),
                envUtil.getUpdatePlayersPeriod(),
                TimeUnit.MINUTES
        );
    }
}
