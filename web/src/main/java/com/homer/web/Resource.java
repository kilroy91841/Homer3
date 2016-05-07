package com.homer.web;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding;
import com.homer.auth.stormpath.StormpathAuthService;
import com.homer.data.*;
import com.homer.exception.LoginFailedException;
import com.homer.external.rest.mlb.MLBRestClient;
import com.homer.service.*;
import com.homer.service.auth.IUserService;
import com.homer.service.auth.User;
import com.homer.service.auth.UserService;
import com.homer.service.full.*;
import com.homer.service.gather.Gatherer;
import com.homer.service.gather.IGatherer;
import com.homer.service.importer.IPlayerImporter;
import com.homer.service.importer.PlayerImporter;
import com.homer.type.*;
import com.homer.type.view.PlayerView;
import com.homer.type.view.TeamView;
import com.homer.web.model.ApiResponse;
import com.homer.web.model.AuthenticationRequest;
import com.homer.web.model.MetadataView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by arigolub on 2/14/16.
 */
@Singleton
@Path("/")
public class Resource {

    private static final Logger LOGGER = LoggerFactory.getLogger(Resource.class);

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
    private IFullTeamService fullTeamService;
    private IUserService userService;

    public Resource() {
        this.teamService = new TeamService(new TeamRepository());
        this.playerService = new PlayerService(new PlayerRepository());
        this.playerSeasonService = new PlayerSeasonService(new PlayerSeasonRepository());
        this.draftDollarService = new DraftDollarService(new DraftDollarRepository());
        this.minorLeaguePickService = new MinorLeaguePickService(new MinorLeaguePickRepository());
        this.tradeService = new TradeService(new TradeRepository());
        this.tradeElementService = new TradeElementService(new TradeElementRepository());
        this.fullTeamService = new FullTeamService(playerSeasonService, teamService);
        this.userService = new UserService(StormpathAuthService.FACTORY.getInstance(), new SessionTokenRepository());

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

    @GET
    @Path("team/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TeamView getTeam(@PathParam(value = "id") long id) {
        return gatherer.gatherTeamById(id);
    }

    @GET
    @Path("team/salary")
    @Produces(MediaType.APPLICATION_JSON)
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

    @AuthRequired
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

    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response login(AuthenticationRequest authRequest) {
        ApiResponse response;
        try {
            String decodedPassword = new String(BaseEncoding.base64().decode(authRequest.getEncodedPassword()), Charsets.US_ASCII);
            User user = userService.login(authRequest.getUserName(), decodedPassword);
            return Response.ok(new ApiResponse("Login successful", user)).header(HttpHeaders.AUTHORIZATION, user.getToken()).build();
        } catch (LoginFailedException e) {
            LOGGER.error(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Path("hasAccess")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ApiResponse hasAccess(@QueryParam(value = "token") String token) {
        boolean hasAccess = userService.hasAccess(token);
        String message = hasAccess ? "User authenticated" : "User authentication failed";
        return new ApiResponse(message, hasAccess);
    }
}
