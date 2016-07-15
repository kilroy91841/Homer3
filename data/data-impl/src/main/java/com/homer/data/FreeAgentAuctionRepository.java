package com.homer.data;

import com.homer.data.common.IFreeAgentAuctionRepository;
import com.homer.type.FreeAgentAuction;
import com.homer.type.history.HistoryFreeAgentAuction;
import com.homer.util.data.BaseVersionedRepository;

/**
 * Created by arigolub on 5/8/16.
 */
public class FreeAgentAuctionRepository extends BaseVersionedRepository<FreeAgentAuction, HistoryFreeAgentAuction> implements IFreeAgentAuctionRepository {

    public FreeAgentAuctionRepository() {
        super(FreeAgentAuction.class, HistoryFreeAgentAuction.class);
    }
}
