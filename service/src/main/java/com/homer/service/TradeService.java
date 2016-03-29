package com.homer.service;

import com.google.common.collect.Maps;
import com.homer.data.common.ITradeRepository;
import com.homer.type.Trade;
import com.homer.util.LeagueUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 3/18/16.
 */
public class TradeService extends BaseIdService<Trade> implements ITradeService {

    private ITradeRepository repo;

    public TradeService(ITradeRepository repo) {
        super(repo);
        this.repo = repo;
    }

    @Override
    public List<Trade> getTrades() {
        return repo.getAll();
    }

    @Override
    public List<Trade> getTradesByTeamIds(Collection<Long> teamIds, boolean currentSeasonOnly) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("team1Id", teamIds);
        if (currentSeasonOnly) {
            filters.put("season", LeagueUtil.SEASON);
        }
        List<Trade> trades = repo.getMany(filters);
        filters.remove("team1Id");
        filters.put("team2Id", teamIds);
        trades.addAll(repo.getMany(filters));
        return trades;
    }
}
