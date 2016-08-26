package com.homer.type.view;

import com.google.common.collect.Lists;
import com.homer.type.Trade;

import java.util.List;

/**
 * Created by arigolub on 8/21/16.
 */
public class TradesView {

    List<Trade> activeTrades = Lists.newArrayList();
    List<Trade> completedTrades = Lists.newArrayList();
    List<Trade> rejectedTrades = Lists.newArrayList();
    List<Trade> cancelledTrades = Lists.newArrayList();

    public List<Trade> getActiveTrades() {
        return activeTrades;
    }

    public List<Trade> getCompletedTrades() {
        return completedTrades;
    }

    public List<Trade> getRejectedTrades() {
        return rejectedTrades;
    }

    public List<Trade> getCancelledTrades() {
        return cancelledTrades;
    }
}
