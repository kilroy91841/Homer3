package com.homer.web;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding;
import com.homer.auth.stormpath.StormpathAuthService;
import com.homer.data.*;
import com.homer.email.EmailRequest;
import com.homer.email.HtmlObject;
import com.homer.email.HtmlTag;
import com.homer.email.IEmailService;
import com.homer.email.aws.AWSEmailService;
import com.homer.exception.LoginFailedException;
import com.homer.external.common.IMLBClient;
import com.homer.external.rest.espn.ESPNRestClient;
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
import com.homer.service.schedule.IScheduler;
import com.homer.service.schedule.Scheduler;
import com.homer.type.*;
import com.homer.type.view.*;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;
import com.homer.web.model.ApiResponse;
import com.homer.web.model.AuthenticationRequest;
import com.homer.web.model.MetadataView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Created by arigolub on 2/14/16.
 */
@Singleton
@Path("/")
public class Resource {

    @Context
    private ContainerRequestContext requestContext;

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
    private IEmailService emailService;
    private IFullMinorLeagueDraftService minorLeagueDraftService;
    private IScheduler scheduler;
    private Validator validator;
    private ITransactionService transactionService;

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
        this.emailService = new AWSEmailService();
        this.scheduler = new Scheduler();

        gatherer = new Gatherer(
                playerService,
                teamService,
                playerSeasonService,
                draftDollarService,
                minorLeaguePickService,
                tradeService,
                tradeElementService
        );

        this.validator = new Validator(teamService, gatherer, emailService);

        fullTradeService = new FullTradeService(tradeService, minorLeaguePickService, draftDollarService,
                playerSeasonService, tradeElementService);

        IMLBClient mlbClient = new MLBRestClient();
        playerImporter = new PlayerImporter(
                playerService,
                playerSeasonService,
                mlbClient
        );

        minorLeagueDraftService = new FullMinorLeagueDraftService(gatherer, minorLeaguePickService, playerService, playerSeasonService,
                new FullPlayerService(playerService, playerSeasonService, mlbClient), mlbClient, emailService, userService, teamService, scheduler);

        transactionService = new TransactionService(new TransactionRepository(), playerService, playerSeasonService, new ESPNRestClient(),
                emailService);
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

    @GET
    @Path("team/{id}/players")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getPlayers(@PathParam(value = "id") long id, @QueryParam(value = "onlyMinorLeaguers") boolean onlyMinorLeaguers) {
        try {
            List<PlayerView> players = gatherer.gatherPlayersByTeamId(id, onlyMinorLeaguers);
            return new ApiResponse(null, players);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
        }
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
        Runnable runnable = SchedulingManager.update40ManRostersRunnable(playerImporter, x -> updatedPlayers.addAll(x));
        runnable.run();
        return updatedPlayers;
    }

    @Path("importer/updatePlayers")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public List<PlayerView> updatePlayers() {
        List<PlayerView> updatedPlayers = Lists.newArrayList();
        Runnable runnable = SchedulingManager.updatePlayersRunnable(playerImporter,
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
        try {
            String decodedPassword = new String(BaseEncoding.base64().decode(authRequest.getEncodedPassword()), Charsets.US_ASCII);
            User user = userService.login(authRequest.getUserName(), decodedPassword);
            return Response.ok(new ApiResponse("Login successful", user)).header(HttpHeaders.AUTHORIZATION, user.getToken()).build();
        } catch (LoginFailedException e) {
            LOGGER.error(e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @Path("passwordReset")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ApiResponse sendPasswordResetEmail(@QueryParam(value = "email") String email) {
        try {
            boolean success = userService.sendPasswordResetEmail(email);
            return new ApiResponse(null, success);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
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

    @Path("testEmail")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ApiResponse sendTestEmail() {
        EmailRequest emailRequest = new EmailRequest(Lists.newArrayList("arigolub@gmail.com"), "Test Email",
                HtmlObject.of(HtmlTag.P).body("This is a test e-mail"));
        return new ApiResponse(emailService.sendEmail(emailRequest), "");
    }

    @Path("minorLeagueDraft/order")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse orderDraft(List<Long> teamIds) {
        try {
            return new ApiResponse("success", minorLeaguePickService.orderMinorLeaguePicksForDraft(teamIds, LeagueUtil.SEASON));
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @Path("minorLeagueDraft")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ApiResponse getDraft(@QueryParam(value = "season") @Nullable Integer season) {
        try {
            MinorLeagueDraftView minorLeagueDraftView = minorLeagueDraftService.getMinorLeagueDraft(season);
            return new ApiResponse(null, minorLeagueDraftView);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @Path("minorLeagueDraft/pick")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse draftPlayer(MinorLeaguePickView minorLeaguePick) {
        try {
            MinorLeaguePickView pick = minorLeagueDraftService.draftPlayer(minorLeaguePick);
            return new ApiResponse("Congratulations, you have drafted " + pick.getPlayerView().getName() + "!", pick);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @Path("minorLeagueDraft/skip/{pickId}")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse skipPick(@PathParam(value = "pickId") long pickId) {
        try {
            MinorLeaguePickView pick = minorLeagueDraftService.skipPick(pickId);
            return new ApiResponse("You have skipped your pick. E-mail Ari to reclaim your pick during this round", pick);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @Path("minorLeagueDraft/admin")
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse adminDraft(MinorLeagueDraftAdminView view) {
        try {
            MinorLeagueDraftView draftView = minorLeagueDraftService.adminDraft(view);
            return new ApiResponse("Admin successful", draftView);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @AuthRequired
    @Path("team")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public ApiResponse updateTeam(Team team) {
        try {
            if (team.getName().length() <= 3) {
                return new ApiResponse("Team name must be longer than 3 characters", null);
            }
            Team updatedTeam = teamService.upsert(team);
            teamService.getFantasyTeamMap().put(updatedTeam.getId(), updatedTeam);
            return new ApiResponse("Team updated! Refresh to see the change", updatedTeam);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @Path("scheduled")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ApiResponse getScheduled() {
        return new ApiResponse("", scheduler.getAll());
    }

    @GET
    @Path("draftDollar/{teamId}")
    public ApiResponse getFreeAgentAuctionDollars(@PathParam(value = "teamId") long teamId) {
        try {
            DraftDollar draftDollar = $.of(draftDollarService.getDraftDollarsByTeam(teamId))
                    .filter(dd -> dd.getSeason() == LeagueUtil.SEASON && DraftDollarType.FREEAGENTAUCTION.equals(dd.getDraftDollarType()))
                    .first();
            return new ApiResponse("success", draftDollar);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("validate/team")
    public ApiResponse validateTeams() {
        try {
            return new ApiResponse("success", validator.validateTeams());
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("processTransactions")
    public ApiResponse processTransactions() {
        try {
            return new ApiResponse("success", transactionService.processDailyTransactions());
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transactions")
    public ApiResponse getTransactions() {
        try {
            return new ApiResponse("success", transactionService.getTransactions());
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), null);
        }
    }
}
