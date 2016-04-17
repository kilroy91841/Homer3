package com.homer.web;

import com.homer.data.*;
import com.homer.service.*;
import com.homer.service.full.FullTradeService;
import com.homer.service.full.IFullTradeService;
import com.homer.service.gather.Gatherer;
import com.homer.service.gather.IGatherer;
import com.homer.type.PlayerSeason;
import com.homer.type.Position;
import com.homer.type.Team;
import com.homer.type.Trade;
import com.homer.type.view.PlayerView;
import com.homer.type.view.TeamView;
import com.homer.util.EnumUtil;
import com.homer.web.response.ApiResponse;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by arigolub on 2/14/16.
 */
@Singleton
@Path("/")
public class Resource {

    private IGatherer gatherer;
    private IFullTradeService fullTradeService;

    private ITeamService teamService;
    private IPlayerService playerService;
    private IPlayerSeasonService playerSeasonService;
    private ITradeService tradeService;
    private ITradeElementService tradeElementService;
    private IMinorLeaguePickService minorLeaguePickService;
    private IDraftDollarService draftDollarService;

    public Resource() {
        this.teamService = new TeamService(new TeamRepository());
        this.playerService = new PlayerService(new PlayerRepository());
        this.playerSeasonService = new PlayerSeasonService(new PlayerSeasonRepository());
        this.draftDollarService = new DraftDollarService(new DraftDollarRepository());
        this.minorLeaguePickService = new MinorLeaguePickService(new MinorLeaguePickRepository());
        this.tradeService = new TradeService(new TradeRepository());
        this.tradeElementService = new TradeElementService(new TradeElementRepository());

        gatherer = new Gatherer(
                playerService,
                teamService,
                playerSeasonService,
                draftDollarService,
                minorLeaguePickService,
                tradeService,
                tradeElementService
        );

        fullTradeService = new FullTradeService(tradeService, minorLeaguePickService, draftDollarService,
                playerSeasonService, tradeElementService);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String helloWord() {
        return "Hello World";
    }

    @Path("player")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PlayerView> getAllPlayers() {
        return gatherer.gatherPlayers();
    }

    @Path("player/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public PlayerView getPlayer(@PathParam(value = "id") long id) {
        return gatherer.gatherPlayerById(id);
    }

    @Path("player/search")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PlayerView> searchPlayersByName(@QueryParam(value = "name") String name) {
        return gatherer.gatherPlayers(playerService.searchPlayersByName(name));
    }

    @Path("team/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public TeamView getTeam(@PathParam(value = "id") long id) {
        return gatherer.gatherTeamById(id);
    }

    @Path("team")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<Team> getTeams() {
        return teamService.getTeams();
    }

    @Path("player/{id}/team/{newTeamId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public PlayerSeason switchTeam(@PathParam(value = "id") long playerId,
                                 @PathParam(value = "newTeamId") @Nullable Long newTeamId,
                                 PlayerSeason playerSeason) {
        int season = playerSeason.getSeason();
        Long oldTeamId = playerSeason.getTeamId();
        if (newTeamId != null && newTeamId == 0) {
            newTeamId = null;
        }
        PlayerSeason updated = playerSeasonService.switchTeam(playerId, season, oldTeamId, newTeamId);
        return playerSeasonService.upsert(updated);
    }

    @Path("player/{id}/position/{newPosition}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public PlayerSeason switchPosition(@PathParam(value = "id") long playerId,
                                   @PathParam(value = "newPosition") int newPositionId,
                                   PlayerSeason playerSeason) {
        int season = playerSeason.getSeason();
        Position oldPosition = playerSeason.getFantasyPosition();
        Position newPosition = EnumUtil.from(Position.class, newPositionId);
        PlayerSeason updated = playerSeasonService.switchFantasyPosition(playerId, season, oldPosition, newPosition);
        return playerSeasonService.upsert(updated);
    }

    @Path("trade")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse acceptTrade(Trade trade) {
        ApiResponse apiResponse = new ApiResponse();
        try {
            apiResponse.setData(fullTradeService.validateAndProcess(trade));
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse.setMessage(e.getMessage());
        }
        return apiResponse;
    }
}
