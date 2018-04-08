package com.homer.web;

import com.homer.service.IPlayerSeasonService;
import com.homer.service.IPlayerService;
import com.homer.service.full.IFullPlayerService;
import com.homer.service.gather.IGatherer;
import com.homer.type.Player;
import com.homer.type.PlayerElf;
import com.homer.type.PlayerSeason;
import com.homer.type.Position;
import com.homer.type.view.PlayerView;
import com.homer.util.EnumUtil;
import com.homer.util.core.$;
import com.homer.web.model.ApiResponse;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.homer.web.RestUtility.safelyDo;

/**
 * Created by arigolub on 4/30/16.
 */
@Singleton
@Path("/player")
public class PlayerResource {

    private IGatherer gatherer;
    private IPlayerService playerService;
    private IPlayerSeasonService playerSeasonService;
    private IFullPlayerService fullPlayerService;

    public PlayerResource() {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        this.playerService = serviceFactory.get(IPlayerService.class);
        this.playerSeasonService = serviceFactory.get(IPlayerSeasonService.class);
        this.fullPlayerService = serviceFactory.get(IFullPlayerService.class);
        this.gatherer = serviceFactory.get(IGatherer.class);
    }

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PlayerView> getAllPlayers() {
        return gatherer.gatherPlayers();
    }

    @AuthRequired
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public ApiResponse addPlayer(Player player) {
        try {
            player.setName(player.getFirstName() + " " + player.getLastName());
            player = fullPlayerService.createPlayer(player);
            return new ApiResponse(String.format("Created %s", player.getName()), gatherer.gatherPlayerById(player.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @AuthRequired
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse updatePlayer(Player player) {
        try {
            player = playerService.updatePlayer(player);
            return new ApiResponse(String.format("Updated %s", player.getName()), gatherer.gatherPlayerById(player.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @AuthRequired
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/season")
    @POST
    public ApiResponse updatePlayerSeason(PlayerSeason playerSeason) {
        try {
            playerSeasonService.upsert(playerSeason);
            Player player = gatherer.gatherPlayerById(playerSeason.getPlayerId());
            return new ApiResponse(String.format("Updated %s", player.getName()), player);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public PlayerView getPlayer(@PathParam(value = "id") long id) {
        return gatherer.gatherPlayerById(id);
    }

    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PlayerView> searchPlayersByName(@QueryParam(value = "name") String name) {
        return gatherer.gatherPlayers(playerService.searchPlayersByName(name));
    }

    @AuthRequired
    @Path("/{id}/team/{newTeamId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse switchTeam(@PathParam(value = "id") long playerId,
                                  @PathParam(value = "newTeamId") @Nullable Long newTeamId,
                                  PlayerSeason playerSeason) {
        int season = playerSeason.getSeason();
        Long oldTeamId = playerSeason.getTeamId();
        if (newTeamId != null && newTeamId == 0) {
            newTeamId = null;
        }
        PlayerSeason updated;
        try {
            updated = playerSeasonService.getCurrentPlayerSeason(playerId);
            PlayerElf.switchTeam(updated, newTeamId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), null);
        }
        updated = playerSeasonService.upsert(updated);
        return new ApiResponse(String.format("Moved %s from team %s to team %s", updated.getPlayerId(),
                oldTeamId, newTeamId), gatherer.gatherPlayerById(playerId));
    }

    @AuthRequired
    @Path("/{id}/position/{newPosition}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse switchPosition(@PathParam(value = "id") long playerId,
                                      @PathParam(value = "newPosition") int newPositionId) {
        try
        {
            Position newPosition = EnumUtil.from(Position.class, newPositionId);
            checkNotNull(newPosition, "New position %s not found", newPosition);
            PlayerSeason currentPlayerSeason = playerSeasonService.getCurrentPlayerSeason(playerId);
            checkNotNull(currentPlayerSeason, "Could not find player season for %s", playerId);
            Position oldPosition = currentPlayerSeason.getFantasyPosition();
            PlayerElf.switchFantasyPosition(currentPlayerSeason, oldPosition, newPosition);
            PlayerSeason updated = checkNotNull(playerSeasonService.upsert(currentPlayerSeason));
            String message = String.format("Moved %s from position %s to position %s", updated.getPlayerId(), oldPosition != null ? oldPosition.getName() : "none", newPosition.getName());
            return new ApiResponse(message, gatherer.gatherPlayerById(playerId));
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), null);
        }
    }
}
