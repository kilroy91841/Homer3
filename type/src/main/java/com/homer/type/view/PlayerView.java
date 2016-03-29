package com.homer.type.view;

import com.homer.type.MLBTeam;
import com.homer.type.Player;
import com.homer.util.HomerBeanUtil;

import java.util.List;

/**
 * Created by arigolub on 3/5/16.
 */
public class PlayerView extends Player {

    private MLBTeam mlbTeam;
    private List<PlayerSeasonView> playerSeasons;
    private PlayerSeasonView currentSeason;

    public PlayerView() { }

    public static PlayerView from(Player player) {
        PlayerView pv = new PlayerView();
        HomerBeanUtil.copyProperties(pv, player);
        return pv;
    }

    public MLBTeam getMlbTeam() {
        return mlbTeam;
    }

    public void setMlbTeam(MLBTeam mlbTeam) {
        this.mlbTeam = mlbTeam;
    }

    public List<PlayerSeasonView> getPlayerSeasons() {
        return playerSeasons;
    }

    public void setPlayerSeasons(List<PlayerSeasonView> playerSeasons) {
        this.playerSeasons = playerSeasons;
    }

    public PlayerSeasonView getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(PlayerSeasonView currentSeason) {
        this.currentSeason = currentSeason;
    }
}
