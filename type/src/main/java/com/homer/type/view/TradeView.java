package com.homer.type.view;

import com.google.common.collect.Lists;
import com.homer.type.Team;
import com.homer.type.Trade;
import com.homer.util.HomerBeanUtil;

import java.util.List;

/**
 * Created by arigolub on 3/18/16.
 */
public class TradeView extends Trade {

    private Team team1;
    private Team team2;

    private List<TradeElementView> tradeElements = Lists.newArrayList();

    public static TradeView from(Trade trade) {
        TradeView tv = new TradeView();
        HomerBeanUtil.copyProperties(tv, trade);
        return tv;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public List<TradeElementView> getTradeElements() {
        return tradeElements;
    }

    public void setTradeElements(List<TradeElementView> tradeElements) {
        this.tradeElements = tradeElements;
    }
}
