package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.type.TradeElement;

import java.util.Collection;
import java.util.List;

/**
 * Created by arigolub on 3/18/16.
 */
public interface ITradeElementService extends IIdService<TradeElement> {

    List<TradeElement> getTradeElements();

    List<TradeElement> getTradeElementsByTradeIds(Collection<Long> tradeIds);
    default List<TradeElement> getTradeElementsByTradeId(long tradeId) {
        return this.getTradeElementsByTradeIds(Lists.newArrayList(tradeId));
    }
}
