package com.homer.service;

import com.google.common.collect.Maps;
import com.homer.data.common.IFreeAgentAuctionRepository;
import com.homer.type.FreeAgentAuction;

import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 5/8/16.
 */
public class FreeAgentAuctionService extends BaseIdService<FreeAgentAuction> implements IFreeAgentAuctionService {

    public FreeAgentAuctionService(IFreeAgentAuctionRepository repo) {
        super(repo);
    }

    @Override
    public List<FreeAgentAuction> getForSeason(int season) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("season", season);
        return getMany(filters);
    }
}
