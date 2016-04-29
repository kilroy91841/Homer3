package com.homer.web;

import com.google.common.collect.Lists;
import com.homer.data.*;
import com.homer.service.*;
import com.homer.service.full.*;
import com.homer.service.gather.Gatherer;
import com.homer.service.gather.IGatherer;
import com.homer.type.*;
import com.homer.type.view.PlayerSeasonView;
import com.homer.type.view.PlayerView;
import com.homer.type.view.TeamView;
import com.homer.util.EnumUtil;
import com.homer.util.core.$;
import com.homer.web.model.ApiResponse;
import com.homer.web.model.MetadataView;

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
    private IFullPlayerService fullPlayerService;
    private IFullTeamService fullTeamService;

    public Resource() {
        this.teamService = new TeamService(new TeamRepository());
        this.playerService = new PlayerService(new PlayerRepository());
        this.playerSeasonService = new PlayerSeasonService(new PlayerSeasonRepository());
        this.draftDollarService = new DraftDollarService(new DraftDollarRepository());
        this.minorLeaguePickService = new MinorLeaguePickService(new MinorLeaguePickRepository());
        this.tradeService = new TradeService(new TradeRepository());
        this.tradeElementService = new TradeElementService(new TradeElementRepository());
        this.fullPlayerService = new FullPlayerService(playerService, playerSeasonService);
        this.fullTeamService = new FullTeamService(playerSeasonService, teamService);

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

    @Path("player/vulturable")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PlayerView> getVulturablePlayers() {
        List<PlayerSeason> vulturablePlayers = playerSeasonService.getVulturablePlayerSeasons();
        return gatherer.gatherPlayersByIds($.of(vulturablePlayers).toList(PlayerSeason::getPlayerId));
    }

    @Path("team/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public TeamView getTeam(@PathParam(value = "id") long id) {
        return gatherer.gatherTeamById(id);
    }

    @Path("team/salary")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ApiResponse getTeamSalaries() {
        List<TeamView> teamViews = fullTeamService.getTeamSalaries();
        return new ApiResponse("success", teamViews);
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

    @Path("player/{id}/position/{newPosition}")
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
        String oldPositionText = oldPosition == null ? "no position" : "position " + oldPosition.getName();
        return new ApiResponse(String.format("Moved %s from %s to position %s", updated.getPlayerId(),
                oldPositionText, newPosition.getName()), updated);
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

    @Path("player")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse addPlayer(Player player) {
        try {
            player = fullPlayerService.createPlayer(player);
            return new ApiResponse(String.format("Created %s", player.getName()), player);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @Path("metadata")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public MetadataView getMetadata() {
        MetadataView mv = new MetadataView();
        mv.setPositions(Lists.newArrayList(Position.class.getEnumConstants()));
        mv.setMlbTeams(Lists.newArrayList(MLBTeam.class.getEnumConstants()));
        return mv;
    }
}
