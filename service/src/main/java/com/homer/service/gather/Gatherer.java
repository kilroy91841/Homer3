package com.homer.service.gather;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.homer.service.*;
import com.homer.type.*;
import com.homer.type.view.*;
import com.homer.util.EnumUtil;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 3/15/16.
 */
public class Gatherer implements IGatherer {

    private final IPlayerService playerService;
    private final ITeamService teamService;
    private final IPlayerSeasonService playerSeasonService;
    private final IDraftDollarService draftDollarService;
    private final IMinorLeaguePickService minorLeaguePickService;
    private final ITradeService tradeService;
    private final ITradeElementService tradeElementService;

    public Gatherer(IPlayerService playerService, ITeamService teamService, IPlayerSeasonService playerSeasonService,
                    IDraftDollarService draftDollarService, IMinorLeaguePickService minorLeaguePickService,
                    ITradeService tradeService, ITradeElementService tradeElementService) {
        this.playerService = playerService;
        this.teamService = teamService;
        this.playerSeasonService = playerSeasonService;
        this.draftDollarService = draftDollarService;
        this.minorLeaguePickService = minorLeaguePickService;
        this.tradeService = tradeService;
        this.tradeElementService = tradeElementService;
    }

    @Override
    public List<PlayerView> gatherPlayers() {
        List<Player> players = playerService.getPlayers();
        return gatherPlayers(players);
    }

    @Override
    public List<PlayerView> gatherPlayers(Collection<Player> players) {
        List<Long> playerIds = $.of(players).toIdList();
        List<PlayerSeason> playerSeasons = playerSeasonService.getPlayerSeasons(playerIds);
        return hydratePlayerViews(players, playerSeasons);
    }

    @Override
    public List<PlayerView> gatherPlayersByIds(Collection<Long> playerIds) {
        List<Player> players = playerService.getByIds(playerIds);
        return gatherPlayers(players);
    }

    @Override
    public List<PlayerView> gatherPlayersByTeamId(long teamId, boolean onlyMinorLeaguers) {
        List<PlayerSeason> playerSeasons;
        if (onlyMinorLeaguers) {
            playerSeasons = playerSeasonService.getMinorLeaguers(teamId, LeagueUtil.SEASON);
        } else {
            playerSeasons = playerSeasonService.getPlayerSeasonsByTeamId(teamId, LeagueUtil.SEASON);
        }
        List<Long> playerIds = $.of(playerSeasons).toList(PlayerSeason::getPlayerId);
        return gatherPlayersByIds(playerIds);
    }

    @Override
    public List<TeamView> gatherTeamsByIds(Collection<Long> teamIds) {
        List<TeamView> teamViews = Lists.newArrayList();

        List<Team> teams = $.of(teamService.getFantasyTeamMap().values()).filterToList(t -> teamIds.contains(t.getId()));
        List<PlayerSeason> playerSeasons = playerSeasonService.getPlayerSeasonsByTeamIds(teamIds, LeagueUtil.SEASON);
        List<Player> players = playerService.getByIds($.of(playerSeasons).toList(PlayerSeason::getPlayerId));
        List<DraftDollar> draftDollars = draftDollarService.getDraftDollarsByTeams(teamIds);
        List<MinorLeaguePick> minorLeaguePicks = minorLeaguePickService.getMinorLeaguePicksByTeams(teamIds, false);

        List<PlayerView> playerViews = hydratePlayerViews(players, playerSeasons);
        List<DraftDollarView> draftDollarViews = hydrateDraftDollarViews(draftDollars);
        List<MinorLeaguePickView> minorLeaguePickViews = hydrateMinorLeaguePickViews(minorLeaguePicks);
        List<Trade> trades = gatherTradesByIds($.of(tradeService.getTradesByTeamIds(teamIds)).toIdList());
        Multimap<Long, PlayerView> teamToPlayers = Multimaps.index(playerViews, pv -> pv.getCurrentSeason().getTeamId());
        Multimap<Long, DraftDollarView> teamToDraftDollars = Multimaps.index(draftDollarViews, DraftDollarView::getTeamId);
        Multimap<Long, MinorLeaguePickView> teamToMinorLeaguePicks = Multimaps.index(minorLeaguePickViews, MinorLeaguePickView::getOwningTeamId);
        Multimap<Long, Trade> team1ToTrades = Multimaps.index(trades, Trade::getTeam1Id);
        Multimap<Long, Trade> team2ToTrades = Multimaps.index(trades, Trade::getTeam2Id);

        teams.forEach(t -> {
            TeamView tv = TeamView.from(t);
            List<PlayerView> playersForTeam = $.of(teamToPlayers.get(t.getId())).toList();
            tv.setMajorLeaguers($.of(playersForTeam).filterToList(p -> p.getCurrentSeason().getFantasyPosition() != Position.MINORLEAGUES));
            tv.setMinorLeaguers($.of(playersForTeam).filterToList(p -> p.getCurrentSeason().getFantasyPosition() == Position.MINORLEAGUES));
            int salary = $.of(tv.getMajorLeaguers())
                    .filter(pv -> pv.getCurrentSeason().getFantasyPosition() != Position.DISABLEDLIST)
                    .reduceToInt(pv -> pv.getCurrentSeason().getSalary());
            int disabledListSalary = $.of(tv.getMajorLeaguers())
                    .filter(pv -> pv.getCurrentSeason().getFantasyPosition() == Position.DISABLEDLIST)
                    .reduceToInt(pv -> pv.getCurrentSeason().getSalary());
            tv.setSalary(salary);
            tv.setDisabledListSalary(disabledListSalary);
            tv.setDraftDollars($.of(teamToDraftDollars.get(t.getId())).toList());
            tv.setMinorLeaguePicks($.of(teamToMinorLeaguePicks.get(t.getId())).toList());
            List<Trade> teamTrades = $.of(team1ToTrades.get(t.getId())).toList();
            teamTrades.addAll(team2ToTrades.get(t.getId()));
            tv.setTrades(teamTrades);
            teamViews.add(tv);
        });
        return teamViews;
    }

    @Override
    public List<Trade> gatherTradesByIds(Collection<Long> tradeIds) {
        List<Trade> trades = tradeService.getByIds(tradeIds);
        List<TradeElement> allTradeElements = tradeElementService.getTradeElementsByTradeIds($.of(trades).toIdList());
        Multimap<Long, TradeElement> tradeElementMap = Multimaps.index(allTradeElements, TradeElement::getTradeId);

        List<Player> players = playerService.getByIds(
                $.of(allTradeElements).filter(t -> t.getPlayerId() != null).toList(TradeElement::getPlayerId));
        List<MinorLeaguePick> minorLeaguePicks = minorLeaguePickService.getByIds(
                $.of(allTradeElements).filter(t -> t.getMinorLeaguePickId() != null).toList(TradeElement::getMinorLeaguePickId));
        List<DraftDollar> draftDollars = draftDollarService.getByIds(
                $.of(allTradeElements).filter(t -> t.getDraftDollarId() != null).toList(TradeElement::getDraftDollarId));

        Map<Long, Player> playerMap = $.of(players).toIdMap();
        Map<Long, MinorLeaguePick> minorLeaguePickMap = $.of(minorLeaguePicks).toIdMap();
        Map<Long, DraftDollar> draftDollarMap = $.of(draftDollars).toIdMap();

        trades.forEach(t -> {
            t.setTeam1(teamService.getFantasyTeamMap().get(t.getTeam1Id()));
            t.setTeam2(teamService.getFantasyTeamMap().get(t.getTeam2Id()));

            List<TradeElement> tradeElements = $.of(tradeElementMap.get(t.getId())).toList();
            tradeElements.forEach(te -> {
                if (playerMap.get(te.getPlayerId()) != null) {
                    te.setPlayer(playerMap.get(te.getPlayerId()));
                }
                if (minorLeaguePickMap.get(te.getMinorLeaguePickId()) != null) {
                    MinorLeaguePickView mlpv = MinorLeaguePickView.from(minorLeaguePickMap.get(te.getMinorLeaguePickId()));
                    mlpv.setOriginalTeam(teamService.getFantasyTeamMap().get(mlpv.getOriginalTeamId()));
                    te.setMinorLeaguePick(mlpv);
                }
                if (draftDollarMap.get(te.getDraftDollarId()) != null) {
                    DraftDollarView ddv = DraftDollarView.from(draftDollarMap.get(te.getDraftDollarId()));
                    ddv.setTeam(teamService.getFantasyTeamMap().get(ddv.getTeamId()));
                    te.setDraftDollar(ddv);
                }
            });
            t.getTradeElements().addAll(tradeElements);
        });
        return trades;
    }

    // region hydraters

    private List<PlayerView> hydratePlayerViews(Collection<Player> players, Collection<PlayerSeason> playerSeasons) {
        List<PlayerView> playerViews = Lists.newArrayList();
        Multimap<Long, PlayerSeason> multimap = Multimaps.index(playerSeasons, PlayerSeason::getPlayerId);

        players.forEach(p -> {
            PlayerView pv = PlayerView.from(p);
            pv.setMlbTeam(EnumUtil.from(MLBTeam.class, p.getMlbTeamId()));
            pv.setPlayerSeasons(hydratePlayerSeasons(p, $.of(multimap.get(pv.getId())).toList()));
            pv.setCurrentSeason($.of(pv.getPlayerSeasons()).first(ps -> ps.getSeason() == LeagueUtil.SEASON));
            playerViews.add(pv);
        });
        return playerViews;
    }

    private List<PlayerSeasonView> hydratePlayerSeasons(Player player, Collection<PlayerSeason> playerSeasons) {
        List<PlayerSeasonView> playerSeasonViews = Lists.newArrayList();
        playerSeasons.forEach(ps -> {
            PlayerSeasonView psv = PlayerSeasonView.from(ps);
            psv.setPlayer(player);
            psv.setTeam(teamService.getFantasyTeamMap().get(psv.getTeamId()));
            psv.setKeeperTeam(teamService.getFantasyTeamMap().get(psv.getKeeperTeamId()));
            playerSeasonViews.add(psv);
        });
        return playerSeasonViews;
    }

    private List<DraftDollarView> hydrateDraftDollarViews(Collection<DraftDollar> draftDollars) {
        List<DraftDollarView> draftDollarViews = Lists.newArrayList();
        draftDollars.forEach(dd -> {
            DraftDollarView ddv = DraftDollarView.from(dd);
            ddv.setTeam(teamService.getFantasyTeamMap().get(dd.getTeamId()));
            draftDollarViews.add(ddv);
        });
        return draftDollarViews;
    }

    private List<MinorLeaguePickView> hydrateMinorLeaguePickViews(Collection<MinorLeaguePick> minorLeaguePicks) {
        List<MinorLeaguePickView> minorLeaguePickViews = Lists.newArrayList();
        List<Player> minorLeaguePickPlayers = playerService.getByIds($.of(minorLeaguePicks).filter(mlp -> mlp.getPlayerId() != null).toList(MinorLeaguePick::getPlayerId));
        Map<Long, Player> minorLeaguePickPlayerMap = $.of(minorLeaguePickPlayers).toIdMap();
        minorLeaguePicks.forEach(mlp -> {
            MinorLeaguePickView mlpv = MinorLeaguePickView.from(mlp);
            mlpv.setOriginalTeam(teamService.getFantasyTeamMap().get(mlpv.getOriginalTeamId()));
            mlpv.setOwningTeam(teamService.getFantasyTeamMap().get(mlpv.getOwningTeamId()));
            if(mlpv.getSwapTeamId() != null) {
                mlpv.setSwapTeam(teamService.getFantasyTeamMap().get(mlpv.getSwapTeamId()));
            }
            if(mlpv.getPlayerId() != null) {
                mlpv.setPlayerView(PlayerView.from(minorLeaguePickPlayerMap.get(mlpv.getPlayerId())));
            }
            minorLeaguePickViews.add(mlpv);
        });
        return minorLeaguePickViews;
    }

    // endregion
}
