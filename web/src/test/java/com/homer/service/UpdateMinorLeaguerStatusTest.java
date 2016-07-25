package com.homer.service;

import com.homer.data.PlayerRepository;
import com.homer.data.PlayerSeasonRepository;
import com.homer.external.rest.mlb.MLBRestClient;
import com.homer.service.full.FullPlayerService;
import com.homer.type.PlayerSeason;
import com.homer.type.view.PlayerView;

import java.util.List;

/**
 * Created by arigolub on 7/23/16.
 */
public class UpdateMinorLeaguerStatusTest {

    public static void main(String[] args) {
        PlayerService playerService = new PlayerService(new PlayerRepository());
        PlayerSeasonService playerSeasonService = new PlayerSeasonService(new PlayerSeasonRepository());
        FullPlayerService fullPlayerService
                = new FullPlayerService(playerService, playerSeasonService, new MLBRestClient());

        List<PlayerView> updatedPlayers = fullPlayerService.updateMinorLeaguerStatusForPlayers();
        System.out.println(updatedPlayers);
    }
}
