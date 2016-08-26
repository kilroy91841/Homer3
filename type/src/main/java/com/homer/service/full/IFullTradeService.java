package com.homer.service.full;

import com.google.common.collect.Lists;
import com.homer.type.Trade;
import com.homer.type.view.TradesView;
import com.homer.util.core.$;

import java.util.Collection;
import java.util.List;

/**
 * Created by arigolub on 3/20/16.
 */
public interface IFullTradeService {

    Trade proposeTrade(Trade inTrade);
    Trade rejectTrade(long tradeId);
    Trade acceptTrade(long tradeId);
    Trade cancelTrade(long inTradeId);

    default Trade getFullTrade(long tradeId) {
        return $.of(getFullTrades(Lists.newArrayList(tradeId))).first();
    }
    List<Trade> getFullTrades(Collection<Long> tradeIds);
    TradesView getTradesForTeam(long teamId);
}
