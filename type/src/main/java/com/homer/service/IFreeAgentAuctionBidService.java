package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.type.FreeAgentAuctionBid;
import com.homer.type.history.HistoryFreeAgentAuctionBid;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * Created by arigolub on 5/8/16.
 */
public interface IFreeAgentAuctionBidService extends IIdService<FreeAgentAuctionBid> {

    List<FreeAgentAuctionBid> getForFreeAgentAuctions(Collection<Long> freeAgentAuctionIds);
    default List<FreeAgentAuctionBid> getForFreeAgentAuction(long freeAgentAuctionId) {
        return getForFreeAgentAuctions(Lists.newArrayList(freeAgentAuctionId));
    }

    List<HistoryFreeAgentAuctionBid> getHistoricalBidsForTeam(long teamId);
}
