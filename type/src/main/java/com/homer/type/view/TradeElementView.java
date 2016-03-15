package com.homer.type.view;

import com.homer.type.*;
import com.homer.util.HomerBeanUtil;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 3/18/16.
 */
public class TradeElementView extends TradeElement {

    private Team teamFrom;
    private Team teamTo;

    @Nullable
    private Player player;
    @Nullable
    private DraftDollar draftDollar;
    @Nullable
    private MinorLeaguePick minorLeaguePick;

    public static TradeElementView from(TradeElement tradeElement) {
        TradeElementView tev = new TradeElementView();
        HomerBeanUtil.copyProperties(tev, tradeElement);
        return tev;
    }

    public Team getTeamFrom() {
        return teamFrom;
    }

    public void setTeamFrom(Team teamFrom) {
        this.teamFrom = teamFrom;
    }

    public Team getTeamTo() {
        return teamTo;
    }

    public void setTeamTo(Team teamTo) {
        this.teamTo = teamTo;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(@Nullable Player player) {
        this.player = player;
    }

    @Nullable
    public DraftDollar getDraftDollar() {
        return draftDollar;
    }

    public void setDraftDollar(@Nullable DraftDollar draftDollar) {
        this.draftDollar = draftDollar;
    }

    @Nullable
    public MinorLeaguePick getMinorLeaguePick() {
        return minorLeaguePick;
    }

    public void setMinorLeaguePick(@Nullable MinorLeaguePick minorLeaguePick) {
        this.minorLeaguePick = minorLeaguePick;
    }
}
