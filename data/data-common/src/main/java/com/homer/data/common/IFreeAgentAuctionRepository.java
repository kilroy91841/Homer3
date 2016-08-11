package com.homer.data.common;

import com.homer.type.FreeAgentAuction;
import com.homer.type.history.HistoryFreeAgentAuction;
import com.homer.util.core.data.IRepository;
import com.homer.util.core.data.IVersionedRepository;

/**
 * Created by arigolub on 5/8/16.
 */
public interface IFreeAgentAuctionRepository extends IVersionedRepository<FreeAgentAuction, HistoryFreeAgentAuction> {
}
