package com.homer.service.full;

import com.homer.type.FreeAgentAuction;
import com.homer.type.FreeAgentAuctionBid;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 5/8/16.
 */
public interface IFullFreeAgentAuctionService {

    FreeAgentAuction requestFreeAgentAuction(long requestingTeamId, @Nullable Long playerId, @Nullable String playerName);

    FreeAgentAuction denyFreeAgentAuctionRequest(long freeAgentAuctionId);

    FreeAgentAuction updateFreeAgentAuctionPlayerId(long freeAgentAuctionId, long playerId);

    FreeAgentAuction startFreeAgentAuction(long freeAgentAuctionId);

    FreeAgentAuction endFreeAgentAuction(long freeAgentAuctionId);

    FreeAgentAuction resolveTiedFreeAgentAuction(long freeAgentAuctionId, long winningTeamId, int winningBid);

    List<FreeAgentAuction> getAllFreeAgentAuctions();

    FreeAgentAuctionBid bidOnFreeAgentAuction(long freeAgentAuctionId, long teamId, int amount);

}
