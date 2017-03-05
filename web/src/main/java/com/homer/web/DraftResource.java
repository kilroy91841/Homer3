package com.homer.web;

import com.homer.data.common.IMajorLeaguePickRepository;
import com.homer.service.IDraftDollarService;
import com.homer.service.IPlayerSeasonService;
import com.homer.service.IPlayerService;
import com.homer.service.gather.IGatherer;
import com.homer.type.*;
import com.homer.type.view.PlayerSeasonView;
import com.homer.type.view.PlayerView;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;
import com.homer.web.model.ApiResponse;
import com.homer.web.model.MajorLeagueDraftView;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.homer.web.RestUtility.safelyDo;

/**
 * Created by arigolub on 2/25/17.
 */
@Singleton
@Path("/majorLeagueDraft")
public class DraftResource {

    @Context
    private ContainerRequestContext requestContext;

    private IPlayerSeasonService playerSeasonService;
    private IDraftDollarService draftDollarService;
    private IPlayerService playerService;
    private IMajorLeaguePickRepository majorLeaguePickRepo;
    private IGatherer gatherer;

    public DraftResource()
    {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        this.playerSeasonService = serviceFactory.get(IPlayerSeasonService.class);
        this.draftDollarService = serviceFactory.get(IDraftDollarService.class);
        this.playerService = serviceFactory.get(IPlayerService.class);
        this.gatherer = serviceFactory.get(IGatherer.class);
        this.majorLeaguePickRepo = serviceFactory.get(IMajorLeaguePickRepository.class);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse draftPlayer(MajorLeagueDraftView majorLeagueDraftView)
    {
        return safelyDo(() ->
        {
            long playerId = majorLeagueDraftView.getPlayerId();
            long teamId = majorLeagueDraftView.getTeamId();
            int salary = majorLeagueDraftView.getSalary();

            if (salary == 0)
            {
                throw new RuntimeException("Salary must be greater than 0");
            }
            if (teamId == 0)
            {
                throw new RuntimeException("Must select a team");
            }
            if (playerId == 0)
            {
                throw new RuntimeException("Must select a player");
            }

            List<PlayerSeason> playerSeasons = playerSeasonService.getPlayerSeasonsByTeamId(teamId, LeagueUtil.SEASON);
            int rosteredPlayers = $.of(playerSeasons).filterToList(playerSeason1 -> playerSeason1.getFantasyPosition() != Position.MINORLEAGUES).size();
            if (rosteredPlayers >= 23)
            {
                throw new RuntimeException("Roster is full");
            }

            DraftDollar draftDollar = $.of(draftDollarService.getDraftDollarsBySeason(LeagueUtil.SEASON)).first(dd -> dd.getTeamId() == teamId);
            int playersLeft = 23 - rosteredPlayers;
            if (draftDollar.getAmount() < (salary + playersLeft - 1))
            {
                throw new RuntimeException("Insufficient funds");
            }

            PlayerSeason playerSeason = checkNotNull(playerSeasonService.getCurrentPlayerSeason(playerId));
            if (playerSeason.getTeamId() != null)
            {
                throw new RuntimeException("Player already on a team");
            }

            playerSeason.setTeamId(teamId);
            playerSeason.setDraftTeamId(teamId);
            playerSeason.setSalary(salary);
            playerSeason.setFantasyPosition(Position.BENCH);
            playerSeason = playerSeasonService.upsert(playerSeason);

            draftDollar.setAmount(draftDollar.getAmount() - salary);
            draftDollar.setTradeId(null);
            draftDollar.setDraftedPlayerId(playerId);
            draftDollar = draftDollarService.upsert(draftDollar);

            MajorLeaguePick majorLeaguePick = new MajorLeaguePick();
            majorLeaguePick.setAmount(salary);
            majorLeaguePick.setPlayerId(playerId);
            majorLeaguePick.setTeamId(teamId);
            majorLeaguePick.setSeason(LeagueUtil.SEASON);
            majorLeaguePickRepo.upsertNoHistory(majorLeaguePick);

            Player player = playerService.getById(playerId);
            PlayerView playerView = PlayerView.from(player);
            playerView.setCurrentSeason(PlayerSeasonView.from(playerSeason));
            return playerView;
        });
    }

    @GET
    @Path("/draftDollars")
    public ApiResponse getDraftDollars()
    {
        return safelyDo(() -> $.of(draftDollarService.getDraftDollarsBySeason(LeagueUtil.SEASON))
                .filterToList(draftDollar -> draftDollar.getDraftDollarType() == DraftDollarType.MLBAUCTION));
    }

    @Path("/players")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ApiResponse getFreeAgents()
    {
        return safelyDo(() -> {
            MajorLeagueDraftView view = new MajorLeagueDraftView();
            List<PlayerSeason> players = playerSeasonService.getActivePlayers();
            List<PlayerView> playerViews = gatherer.gatherPlayersByIds($.of(players).toList(PlayerSeason::getPlayerId));
            List<Long> freeAgentPlayerIds = $.of(players)
                    .filter(playerSeason -> playerSeason.getTeamId() == null)
                    .toList(PlayerSeason::getPlayerId);
            List<Long> playerIds = $.of(players)
                    .filter(playerSeason -> playerSeason.getTeamId() != null)
                    .toList(PlayerSeason::getPlayerId);
            view.setFreeAgents($.of(playerViews).filterToList(playerView -> freeAgentPlayerIds.contains(playerView.getId())));
            view.setPlayers($.of(playerViews).filterToList(playerView -> playerIds.contains(playerView.getId())));
            view.setCurrentPlayer(currentPlayer);
            Map<Long, PlayerView> playerViewMap = $.of(playerViews).toIdMap();
            List<MajorLeaguePick> majorLeaguePicks = majorLeaguePickRepo.getAll();
            for (MajorLeaguePick majorLeaguePick : majorLeaguePicks)
            {
                majorLeaguePick.setPlayerView(playerViewMap.get(majorLeaguePick.getPlayerId()));
                view.getPicks().add(majorLeaguePick);
            }
            return view;
        });
    }

    private static Player currentPlayer = null;

    @Path("/currentPlayer/{playerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public ApiResponse setCurrentPlayer(@PathParam(value = "playerId") long playerId)
    {
        return safelyDo(() -> {
            currentPlayer = playerService.getById(playerId);
            return currentPlayer;
        });
    }
}
