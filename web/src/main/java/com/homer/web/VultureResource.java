package com.homer.web;

import com.homer.auth.stormpath.StormpathAuthService;
import com.homer.data.*;
import com.homer.email.aws.AWSEmailService;
import com.homer.service.*;
import com.homer.service.auth.UserService;
import com.homer.service.full.FullPlayerService;
import com.homer.service.full.FullVultureService;
import com.homer.service.full.IFullVultureService;
import com.homer.service.gather.Gatherer;
import com.homer.service.gather.IGatherer;
import com.homer.service.schedule.Scheduler;
import com.homer.type.PlayerSeason;
import com.homer.type.Vulture;
import com.homer.type.view.PlayerView;
import com.homer.util.core.$;
import com.homer.web.model.ApiResponse;
import com.homer.web.model.VultureRequest;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by arigolub on 5/7/16.
 */
@Singleton
@Path("/vulture")
public class VultureResource {

    private IFullVultureService fullVultureService;
    private IGatherer gatherer;

    public VultureResource() {
        ITeamService teamService = new TeamService(new TeamRepository());
        IPlayerSeasonService playerSeasonService = new PlayerSeasonService(new PlayerSeasonRepository());
        IPlayerService playerService = new PlayerService(new PlayerRepository());

        this.fullVultureService = new FullVultureService(new VultureService(new VultureRepository()),
                playerSeasonService,
                teamService,
                playerService,
                new UserService(StormpathAuthService.FACTORY.getInstance(), new SessionTokenRepository()),
                new AWSEmailService(),
                new Scheduler());

        gatherer = new Gatherer(
                playerService,
                teamService,
                playerSeasonService,
                new DraftDollarService(new DraftDollarRepository()),
                new MinorLeaguePickService(new MinorLeaguePickRepository()),
                new TradeService(new TradeRepository()),
                new TradeElementService(new TradeElementRepository())
        );
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getInProgressVultures() {
        return new ApiResponse("success", fullVultureService.getInProgressVultures());
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createVulture(VultureRequest vultureRequest) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            Vulture vulture = fullVultureService.createVulture(vultureRequest.getVultureTeamId(), vultureRequest.getPlayerId(),
                    vultureRequest.getDropPlayerId(), vultureRequest.getIsCommissionerVulture());
            apiResponse.setData(vulture);
            apiResponse.setMessage("success");
        } catch (Exception e) {
            apiResponse.setMessage(e.getMessage());
        }
        return apiResponse;
    }

    @POST
    @Path("/{playerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse markInProgressVultureForPlayerAsFixed(@PathParam(value="playerId") long playerId) {
        boolean success = fullVultureService.markInProgressVultureForPlayerAsFixed(playerId);
        return new ApiResponse("Vulture for " + playerId + " was marked as fixed: " + success, success);
    }

    @Path("/vulturable")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PlayerView> getVulturablePlayers() {
        List<PlayerSeason> vulturablePlayers = fullVultureService.getVulturablePlayerSeasons();
        return gatherer.gatherPlayersByIds($.of(vulturablePlayers).toList(PlayerSeason::getPlayerId));
    }
}
