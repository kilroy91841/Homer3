package com.homer.service;

import com.homer.data.common.ITradeElementRepository;
import com.homer.type.TradeElement;

import java.util.Collection;
import java.util.List;

/**
 * Created by arigolub on 3/18/16.
 */
public class TradeElementService extends BaseIdService<TradeElement> implements ITradeElementService {

    private ITradeElementRepository repo;

    public TradeElementService(ITradeElementRepository repo) {
        super(repo);
        this.repo = repo;
    }

    @Override
    public List<TradeElement> getTradeElements() {
        return repo.getAll();
    }

    @Override
    public List<TradeElement> getTradeElementsByTradeIds(Collection<Long> tradeIds) {
        return repo.getMany("tradeId", tradeIds);
    }

}
