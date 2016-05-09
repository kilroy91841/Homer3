package com.homer.service.full;

import com.homer.service.IFreeAgentAuctionBidService;
import com.homer.service.IFreeAgentAuctionService;
import com.homer.service.IPlayerSeasonService;
import com.homer.service.IPlayerService;
import com.homer.type.FreeAgentAuction;
import com.homer.type.FreeAgentAuctionBid;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 5/8/16.
 */
public class FullFreeAgentAuctionService implements IFullFreeAgentAuctionService {

    private IFreeAgentAuctionService freeAgentAuctionService;
    private IFreeAgentAuctionBidService freeAgentAuctionBidService;
    private IPlayerSeasonService playerSeasonService;
    private IPlayerService playerService;

    public FullFreeAgentAuctionService(IFreeAgentAuctionService freeAgentAuctionService, IFreeAgentAuctionBidService freeAgentAuctionBidService, IPlayerSeasonService playerSeasonService, IPlayerService playerService) {
        this.freeAgentAuctionService = freeAgentAuctionService;
        this.freeAgentAuctionBidService = freeAgentAuctionBidService;
        this.playerSeasonService = playerSeasonService;
        this.playerService = playerService;
    }

    @Override
    public FreeAgentAuction requestFreeAgentAuction(@Nullable Long playerId, @Nullable String playerName) {
        return null;
    }

    @Override
    public FreeAgentAuction denyFreeAgentAuctionRequest(long freeAgentAuctionId) {
        return null;
    }

    @Override
    public FreeAgentAuction startFreeAgentAuction(long freeAgentAuctionId) {
        return null;
    }

    @Override
    public FreeAgentAuction endFreeAgentAuction(long freeAgentAuctionId) {
        return null;
    }

    @Override
    public List<FreeAgentAuction> getAllFreeAgentAuctions() {
        return null;
    }

    @Override
    public FreeAgentAuctionBid bidOnFreeAgentAuction(long freeAgentAuctionId, long teamId, int amount) {
        return null;
    }
}
