package com.homer.service.full;

import com.homer.email.IEmailService;
import com.homer.service.*;
import com.homer.type.EventStatus;
import com.homer.type.FreeAgentAuction;
import com.homer.type.FreeAgentAuctionBid;
import com.homer.type.Player;
import com.homer.util.LeagueUtil;

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
    private IEmailService emailService;
    private IDraftDollarService draftDollarService;

    public FullFreeAgentAuctionService(IFreeAgentAuctionService freeAgentAuctionService,
                                       IFreeAgentAuctionBidService freeAgentAuctionBidService,
                                       IPlayerSeasonService playerSeasonService,
                                       IPlayerService playerService,
                                       IEmailService emailService,
                                       IDraftDollarService draftDollarService) {
        this.freeAgentAuctionService = freeAgentAuctionService;
        this.freeAgentAuctionBidService = freeAgentAuctionBidService;
        this.playerSeasonService = playerSeasonService;
        this.playerService = playerService;
        this.emailService = emailService;
        this.draftDollarService = draftDollarService;
    }

    @Override
    public FreeAgentAuction requestFreeAgentAuction(long requestingTeamId, @Nullable Long playerId, @Nullable String playerName) {
        if (playerId == null && playerSeasonService == null) {
            throw new RuntimeException("Must supply either playerId or playerName");
        }

        FreeAgentAuction freeAgentAuction = new FreeAgentAuction();
        freeAgentAuction.setRequestingTeamId(requestingTeamId);
        freeAgentAuction.setAuctionStatus(EventStatus.REQUESTED);
        freeAgentAuction.setSeason(LeagueUtil.SEASON);
        if (playerId != null) {
            Player player = playerService.getById(playerId);
            if (player == null) {
                throw new RuntimeException("No player with id " + playerId + " found to start free agent auction");
            }
            freeAgentAuction.setPlayerId(playerId);
        } else if (playerName != null) {
            Player player = playerService.getPlayerByName(playerName);
            if (player != null) {
                freeAgentAuction.setPlayerId(player.getId());
            }
        }
        emailService.sendEmail(null);
        return freeAgentAuctionService.upsert(freeAgentAuction);
    }

    @Override
    public FreeAgentAuction denyFreeAgentAuctionRequest(long freeAgentAuctionId) {
        return null;
    }

    @Override
    public FreeAgentAuction updateFreeAgentAuctionPlayerId(long freeAgentAuctionId, long playerId) {
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
    public FreeAgentAuction resolveTiedFreeAgentAuction(long freeAgentAuctionId, long winningTeamId, int winningBid) {
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
