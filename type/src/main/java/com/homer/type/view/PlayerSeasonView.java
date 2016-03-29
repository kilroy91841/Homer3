package com.homer.type.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.homer.type.Player;
import com.homer.type.PlayerSeason;
import com.homer.type.Team;
import com.homer.util.HomerBeanUtil;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 3/16/16.
 */
public class PlayerSeasonView extends PlayerSeason {

    @JsonIgnore
    private Player player;
    @Nullable
    private Team team;
    @Nullable
    private Team keeperTeam;

    public PlayerSeasonView() { }

    public static PlayerSeasonView from(PlayerSeason playerSeason) {
        PlayerSeasonView psv = new PlayerSeasonView();
        HomerBeanUtil.copyProperties(psv, playerSeason);
        return psv;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Nullable
    public Team getTeam() {
        return team;
    }

    public void setTeam(@Nullable Team team) {
        this.team = team;
    }

    @Nullable
    public Team getKeeperTeam() {
        return keeperTeam;
    }

    public void setKeeperTeam(@Nullable Team keeperTeam) {
        this.keeperTeam = keeperTeam;
    }
}
