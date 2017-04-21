package com.homer.service;

import com.google.common.eventbus.EventBus;
import com.homer.data.PlayerRepository;
import com.homer.data.PlayerSeasonRepository;
import com.homer.external.rest.mlb.MLBRestClient;
import com.homer.service.full.FullPlayerService;
import com.homer.service.importer.PlayerImporter;
import com.homer.type.MLBTeam;
import com.homer.type.Player;
import com.homer.type.PlayerSeason;
import com.homer.util.core.$;

import java.util.List;

/**
 * Created by arigolub on 5/4/16.
 */
public class PlayerImporterTest {

    public static void main(String[] args) {

        PlayerService playerService = new PlayerService(new PlayerRepository());
        PlayerSeasonService playerSeasonService = new PlayerSeasonService(new PlayerSeasonRepository(), new EventBus());
        MLBRestClient mlbClient = new MLBRestClient();
        PlayerImporter playerImporter = new PlayerImporter(
                playerService,
                playerSeasonService,
                mlbClient,
                new FullPlayerService(playerService, playerSeasonService, mlbClient)
        );

//        List<PlayerSeason> playerSeasons = playerSeasonService.getActivePlayers();
//        List<Long> playerIds = $.of(playerSeasons).toList(PlayerSeason::getPlayerId);
//        List<Player> players = playerService.getByIds(playerIds);
//        for (Player player : players) {
//            try {
//                playerImporter.updatePlayer(player);
//            } catch (Exception e) {
//                System.out.println(String.format(
//                        "Error updating %s : \n%s", player.getName(), e.getMessage()));
//            }
//        }

        for (MLBTeam team : MLBTeam.values()) {
            if (team == MLBTeam.FREEAGENT) {
                continue;
            }
            playerImporter.update40ManRoster(team.getId());
        }
    }
}
