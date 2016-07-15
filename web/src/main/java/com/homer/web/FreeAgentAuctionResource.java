package com.homer.web;

import com.homer.auth.stormpath.StormpathAuthService;
import com.homer.data.*;
import com.homer.email.IEmailService;
import com.homer.email.aws.AWSEmailService;
import com.homer.external.common.IMLBClient;
import com.homer.external.rest.mlb.MLBRestClient;
import com.homer.service.*;
import com.homer.service.auth.IAuthService;
import com.homer.service.auth.UserService;
import com.homer.service.full.FullFreeAgentAuctionService;
import com.homer.service.full.FullPlayerService;
import com.homer.service.full.IFullFreeAgentAuctionService;
import com.homer.service.full.IFullPlayerService;
import com.homer.service.gather.Gatherer;
import com.homer.service.gather.IGatherer;
import com.homer.service.schedule.Scheduler;
import com.homer.type.view.FreeAgentAuctionAdminView;
import com.homer.type.view.FreeAgentAuctionBidView;
import com.homer.type.view.FreeAgentAuctionView;
import com.homer.web.model.ApiResponse;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by arigolub on 6/13/16.
 */
@Singleton
@Path("/freeAgentAuction")
public class FreeAgentAuctionResource {

    private IFullFreeAgentAuctionService fullFreeAgentAuctionService;

    public FreeAgentAuctionResource() {
        IFreeAgentAuctionService freeAgentAuctionService = new FreeAgentAuctionService(new FreeAgentAuctionRepository());
        IFreeAgentAuctionBidService freeAgentAuctionBidService = new FreeAgentAuctionBidService(new FreeAgentAuctionBidRepository());
        IPlayerSeasonService playerSeasonService = new PlayerSeasonService(new PlayerSeasonRepository());
        IPlayerService playerService = new PlayerService(new PlayerRepository());
        IFullPlayerService fullPlayerService = new FullPlayerService(playerService, playerSeasonService);
        IEmailService emailService = new AWSEmailService();
        IDraftDollarService draftDollarService = new DraftDollarService(new DraftDollarRepository());
        IMLBClient mlbClient = new MLBRestClient();
        ITeamService teamService = new TeamService(new TeamRepository());

        this.fullFreeAgentAuctionService = new FullFreeAgentAuctionService(freeAgentAuctionService,
                freeAgentAuctionBidService,
                playerSeasonService,
                playerService,
                fullPlayerService,
                emailService,
                draftDollarService,
                mlbClient,
                teamService,
                new Scheduler(),
                new UserService(StormpathAuthService.FACTORY.getInstance(), new SessionTokenRepository()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getFreeAgentAuctions() {
        return new ApiResponse("success", fullFreeAgentAuctionService.getFreeAgentAuctions());
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/request")
    public ApiResponse requestFreeAgentAuction(FreeAgentAuctionView view) {
        try {
            FreeAgentAuctionView response = fullFreeAgentAuctionService.requestFreeAgentAuction(view);
            return new ApiResponse("Free Agent Auction request for " + response.getPlayer().getName() + " is pending", response);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @Path("/admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse adminFreeAgentAuction(FreeAgentAuctionAdminView view) {
        try {
            return new ApiResponse("Admin successful", fullFreeAgentAuctionService.adminFreeAgentAuction(view));
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @Path("/bid")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse bidOnFreeAgentAuction(FreeAgentAuctionBidView view) {
        try {
            return new ApiResponse("Bid approved", fullFreeAgentAuctionService.bidOnFreeAgentAuction(view.getFreeAgentAuctionId(),
                    view.getTeamId(), view.getAmount()));
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
        }
    }
}
