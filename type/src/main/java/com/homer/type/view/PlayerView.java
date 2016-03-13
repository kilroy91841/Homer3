package com.homer.type.view;

import com.homer.type.Player;
import com.homer.type.PlayerSeason;
import com.homer.type.Position;
import com.homer.type.Team;

import java.util.List;

/**
 * Created by arigolub on 3/5/16.
 */
public class PlayerView {

    private Player player;
    private List<PlayerSeason> playerSeasons;
    private PlayerSeason currentSeason;

    public PlayerView() {
    }

    public PlayerView(Player player, List<PlayerSeason> playerSeasons) {
        this.player = player;
        this.playerSeasons = playerSeasons;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<PlayerSeason> getPlayerSeasons() {
        return playerSeasons;
    }

    public void setPlayerSeasons(List<PlayerSeason> playerSeasons) {
        this.playerSeasons = playerSeasons;
    }

    public PlayerSeason getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(PlayerSeason currentSeason) {
        this.currentSeason = currentSeason;
    }
}
