package com.homer.web;

import com.homer.data.*;
import com.homer.service.*;
import com.homer.service.full.FullPlayerService;
import com.homer.service.full.IFullPlayerService;
import com.homer.service.gather.Gatherer;
import com.homer.service.gather.IGatherer;
import com.homer.type.Player;
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

/**
 * Created by arigolub on 4/30/16.
 */
@Singleton
@Path("/player")
public class PlayerResource {

    private IGatherer gatherer;

    private ITeamService teamService;
    private IPlayerService playerService;
    private IPlayerSeasonService playerSeasonService;
    private ITradeService tradeService;
    private ITradeElementService tradeElementService;
    private IMinorLeaguePickService minorLeaguePickService;
    private IDraftDollarService draftDollarService;
    private IFullPlayerService fullPlayerService;

    public PlayerResource() {
        this.teamService = new TeamService(new TeamRepository());
        this.playerService = new PlayerService(new PlayerRepository());
        this.playerSeasonService = new PlayerSeasonService(new PlayerSeasonRepository());
        this.draftDollarService = new DraftDollarService(new DraftDollarRepository());
        this.minorLeaguePickService = new MinorLeaguePickService(new MinorLeaguePickRepository());
        this.tradeService = new TradeService(new TradeRepository());
        this.tradeElementService = new TradeElementService(new TradeElementRepository());
        this.fullPlayerService = new FullPlayerService(playerService, playerSeasonService);

        gatherer = new Gatherer(
                playerService,
                teamService,
                playerSeasonService,
                draftDollarService,
                minorLeaguePickService,
                tradeService,
                tradeElementService
        );
    }

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PlayerView> getAllPlayers() {
        return gatherer.gatherPlayers();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public ApiResponse addPlayer(Player player) {
        try {
            player = fullPlayerService.createPlayer(player);
            return new ApiResponse(String.format("Created %s", player.getName()), player);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse updatePlayer(Player player) {
        try {
            player = playerService.updatePlayer(player);
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
            updated = playerSeasonService.switchTeam(playerId, season, oldTeamId, newTeamId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), null);
        }
        updated = playerSeasonService.upsert(updated);
        return new ApiResponse(String.format("Moved %s from team %s to team %s", updated.getPlayerId(),
                oldTeamId, newTeamId), updated);
    }

    @Path("/{id}/position/{newPosition}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse switchPosition(@PathParam(value = "id") long playerId,
                                      @PathParam(value = "newPosition") int newPositionId,
                                      PlayerSeason playerSeason) {
        int season = playerSeason.getSeason();
        Position oldPosition = playerSeason.getFantasyPosition();
        Position newPosition = EnumUtil.from(Position.class, newPositionId);
        PlayerSeason updated;
        try {
            updated = playerSeasonService.switchFantasyPosition(playerId, season, oldPosition, newPosition);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), null);
        }
        updated = playerSeasonService.upsert(updated);
        return new ApiResponse(String.format("Moved %s from position %s to position %s", updated.getPlayerId(),
                oldPosition.getName(), newPosition.getName()), updated);
    }

    @Path("/vulturable")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PlayerView> getVulturablePlayers() {
        List<PlayerSeason> vulturablePlayers = playerSeasonService.getVulturablePlayerSeasons();
        return gatherer.gatherPlayersByIds($.of(vulturablePlayers).toList(PlayerSeason::getPlayerId));
    }
}