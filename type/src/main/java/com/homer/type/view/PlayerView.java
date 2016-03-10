package com.homer.type.view;

import com.homer.type.Player;
import com.homer.type.PlayerSeason;
import com.homer.type.Position;
import com.homer.type.Team;

/**
 * Created by arigolub on 3/5/16.
 */
public class PlayerView {

    private Player player;
    private PlayerSeason playerSeason;
    private Team fantasyTeam;
    private Position fantasyPosition;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayerSeason getPlayerSeason() {
        return playerSeason;
    }

    public void setPlayerSeason(PlayerSeason playerSeason) {
        this.playerSeason = playerSeason;
    }

    public Team getFantasyTeam() {
        return fantasyTeam;
    }

    public void setFantasyTeam(Team fantasyTeam) {
        this.fantasyTeam = fantasyTeam;
    }

    public Position getFantasyPosition() {
        return fantasyPosition;
    }

    public void setFantasyPosition(Position fantasyPosition) {
        this.fantasyPosition = fantasyPosition;
    }
}
