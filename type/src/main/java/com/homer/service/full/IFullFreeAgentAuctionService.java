package com.homer.service.full;

import com.homer.exception.FreeAgentAuctionBidException;
import com.homer.type.FreeAgentAuction;
import com.homer.type.FreeAgentAuctionBid;
import com.homer.type.Player;
import com.homer.type.view.FreeAgentAuctionAdminView;
import com.homer.type.view.FreeAgentAuctionView;

import java.util.List;

/**
 * Created by arigolub on 5/8/16.
 */
public interface IFullFreeAgentAuctionService {

    List<FreeAgentAuctionView> getFreeAgentAuctions();

    FreeAgentAuctionView requestFreeAgentAuction(FreeAgentAuctionView view) throws FreeAgentAuctionBidException;

    FreeAgentAuction denyFreeAgentAuctionRequest(long freeAgentAuctionId, String reason);

    FreeAgentAuction startFreeAgentAuction(long freeAgentAuctionId);

    FreeAgentAuction endFreeAgentAuction(long freeAgentAuctionId) throws FreeAgentAuctionBidException;

    FreeAgentAuction setWinningBid(long freeAgentAuctionId, long teamId);

    FreeAgentAuctionBid bidOnFreeAgentAuction(long freeAgentAuctionId, long teamId, int amount) throws FreeAgentAuctionBidException;

    List<FreeAgentAuctionView> adminFreeAgentAuction(FreeAgentAuctionAdminView adminView);

    FreeAgentAuction cancelAuction(long freeAgentAuctionId);

}
