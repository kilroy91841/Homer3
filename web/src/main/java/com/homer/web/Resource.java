package com.homer.web;

import com.google.common.collect.Lists;
import com.homer.data.*;
import com.homer.external.rest.mlb.MLBRestClient;
import com.homer.service.*;
import com.homer.service.full.FullTradeService;
import com.homer.service.full.IFullTradeService;
import com.homer.service.gather.Gatherer;
import com.homer.service.gather.IGatherer;
import com.homer.service.importer.IPlayerImporter;
import com.homer.service.importer.PlayerImporter;
import com.homer.type.*;
import com.homer.type.view.PlayerView;
import com.homer.type.view.TeamView;
import com.homer.web.model.ApiResponse;
import com.homer.web.model.MetadataView;

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
    private IPlayerImporter playerImporter;

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

        playerImporter = new PlayerImporter(
                playerService,
                playerSeasonService,
                new MLBRestClient()
        );
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String helloWord() {
        return "Hello World";
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

    @Path("metadata")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public MetadataView getMetadata() {
        MetadataView mv = new MetadataView();
        mv.setPositions(Lists.newArrayList(Position.class.getEnumConstants()));
        mv.setMlbTeams(Lists.newArrayList(MLBTeam.class.getEnumConstants()));
        return mv;
    }

    @Path("importer/update40Man")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PlayerView> update40Man() {
        List<PlayerView> updatedPlayers = Lists.newArrayList();
        Runnable runnable = Scheduler.update40ManRostersRunnable(playerImporter, x -> updatedPlayers.addAll(x));
        runnable.run();
        return updatedPlayers;
    }

    @Path("importer/updatePlayers")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PlayerView> updatePlayers() {
        List<PlayerView> updatedPlayers = Lists.newArrayList();
        Runnable runnable = Scheduler.updatePlayersRunnable(playerImporter,
                playerSeasonService, playerService,
                x -> updatedPlayers.add(x));
        runnable.run();
        return updatedPlayers;
    }
}
