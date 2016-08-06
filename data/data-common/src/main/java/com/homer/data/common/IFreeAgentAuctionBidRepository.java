package com.homer.data.common;

import com.homer.type.FreeAgentAuctionBid;
import com.homer.type.history.HistoryFreeAgentAuctionBid;
import com.homer.util.core.data.IRepository;
import com.homer.util.core.data.IVersionedRepository;

/**
 * Created by arigolub on 5/8/16.
 */
public interface IFreeAgentAuctionBidRepository extends IVersionedRepository<FreeAgentAuctionBid, HistoryFreeAgentAuctionBid> {
}
