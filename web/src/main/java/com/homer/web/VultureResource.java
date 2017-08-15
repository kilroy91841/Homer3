package com.homer.web;

import com.homer.service.full.IFullVultureService;
import com.homer.service.gather.IGatherer;
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

    private ServiceFactory serviceFactory = ServiceFactory.getInstance();

    private IFullVultureService fullVultureService;
    private IGatherer gatherer;

    public VultureResource() {
        this.fullVultureService = serviceFactory.get(IFullVultureService.class);
        this.gatherer = serviceFactory.get(IGatherer.class);
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
