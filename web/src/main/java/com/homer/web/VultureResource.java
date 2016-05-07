package com.homer.web;

import com.homer.data.PlayerRepository;
import com.homer.data.PlayerSeasonRepository;
import com.homer.data.TeamRepository;
import com.homer.data.VultureRepository;
import com.homer.service.*;
import com.homer.service.full.FullVultureService;
import com.homer.service.full.IFullVultureService;
import com.homer.type.Vulture;
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

    public VultureResource() {
        this.fullVultureService = new FullVultureService(new VultureService(new VultureRepository()),
                new PlayerSeasonService(new PlayerSeasonRepository()),
                new TeamService(new TeamRepository()),
                new PlayerService(new PlayerRepository()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Vulture> getInProgressVultures() {
        return fullVultureService.getInProgressVultures();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Vulture createVulture(VultureRequest vultureRequest) {
        return fullVultureService.createVulture(vultureRequest.getVultureTeamId(), vultureRequest.getPlayerId(),
                vultureRequest.getDropPlayerId(), vultureRequest.getIsCommissionerVulture());
    }

    @POST
    @Path("/{playerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse markInProgressVultureForPlayerAsFixed(@PathParam(value="playerId") long playerId) {
        boolean success = fullVultureService.markInProgressVultureForPlayerAsFixed(playerId);
        return new ApiResponse("Vulture for " + playerId + " was marked as fixed: " + success, success);
    }
}
