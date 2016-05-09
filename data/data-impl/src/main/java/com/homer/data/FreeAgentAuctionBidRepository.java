package com.homer.data;

import com.homer.data.common.IFreeAgentAuctionBidRepository;
import com.homer.type.FreeAgentAuctionBid;
import com.homer.type.history.HistoryFreeAgentAuctionBid;
import com.homer.util.data.BaseVersionedRepository;

/**
 * Created by arigolub on 5/8/16.
 */
public class FreeAgentAuctionBidRepository extends BaseVersionedRepository<FreeAgentAuctionBid, HistoryFreeAgentAuctionBid> implements IFreeAgentAuctionBidRepository {
    public FreeAgentAuctionBidRepository() {
        super(FreeAgentAuctionBid.class, HistoryFreeAgentAuctionBid.class);
    }
}
