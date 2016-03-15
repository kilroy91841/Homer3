package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.type.Trade;
import com.homer.type.TradeElement;

import java.util.Collection;
import java.util.List;

/**
 * Created by arigolub on 3/18/16.
 */
public interface ITradeService extends IIdService<Trade> {

    List<Trade> getTrades();

    List<Trade> getTradesByTeamIds(Collection<Long> teamIds, boolean currentSeasonOnly);
    default List<Trade> getTradesByTeamIds(Collection<Long> teamIds) {
        return getTradesByTeamIds(teamIds, true);
    }
    default List<Trade> getTradesByTeamId(long teamId, boolean currentSeasonOnly) {
        return this.getTradesByTeamIds(Lists.newArrayList(teamId), currentSeasonOnly);
    }
    default List<Trade> getTradesByTeamId(long teamId) {
        return this.getTradesByTeamIds(Lists.newArrayList(teamId));
    }
}
