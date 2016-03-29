package com.homer.type.view;

import com.homer.type.MinorLeaguePick;
import com.homer.type.Player;
import com.homer.type.Team;
import com.homer.util.HomerBeanUtil;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 3/17/16.
 */
public class MinorLeaguePickView extends MinorLeaguePick {

    private Team originalTeam;
    private Team owningTeam;
    @Nullable
    private Team swapTeam;
    @Nullable
    private Player player;

    public MinorLeaguePickView() { }

    public static MinorLeaguePickView from(MinorLeaguePick minorLeaguePick) {
        MinorLeaguePickView mlpv = new MinorLeaguePickView();
        HomerBeanUtil.copyProperties(mlpv, minorLeaguePick);
        return mlpv;
    }

    public Team getOriginalTeam() {
        return originalTeam;
    }

    public void setOriginalTeam(Team originalTeam) {
        this.originalTeam = originalTeam;
    }

    public Team getOwningTeam() {
        return owningTeam;
    }

    public void setOwningTeam(Team owningTeam) {
        this.owningTeam = owningTeam;
    }

    @Nullable
    public Team getSwapTeam() {
        return swapTeam;
    }

    public void setSwapTeam(@Nullable Team swapTeam) {
        this.swapTeam = swapTeam;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(@Nullable Player player) {
        this.player = player;
    }
}
