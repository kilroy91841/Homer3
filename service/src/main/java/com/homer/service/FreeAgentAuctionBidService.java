package com.homer.service;

import com.google.common.collect.Maps;
import com.homer.data.common.IFreeAgentAuctionBidRepository;
import com.homer.type.FreeAgentAuctionBid;
import com.homer.type.history.HistoryFreeAgentAuctionBid;
import com.homer.util.core.data.IRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 5/8/16.
 */
public class FreeAgentAuctionBidService extends BaseIdService<FreeAgentAuctionBid> implements IFreeAgentAuctionBidService {

    public FreeAgentAuctionBidService(IFreeAgentAuctionBidRepository repo) {
        super(repo);
    }

    @Override
    public List<FreeAgentAuctionBid> getForFreeAgentAuctions(Collection<Long> freeAgentAuctionIds) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("freeAgentAuctionId", freeAgentAuctionIds);
        return getMany(filters);
    }

    @Override
    public List<HistoryFreeAgentAuctionBid> getHistoricalBidsForTeam(long teamId) {
        return null;
    }
}
