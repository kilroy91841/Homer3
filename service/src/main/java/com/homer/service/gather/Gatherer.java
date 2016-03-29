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

    private static Map<Long, Team> FANTASY_TEAM_MAP;

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

        FANTASY_TEAM_MAP = $.of(teamService.getTeams()).toIdMap();
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
    public List<TeamView> gatherTeamsByIds(Collection<Long> teamIds) {
        List<TeamView> teamViews = Lists.newArrayList();

        List<Team> teams = $.of(FANTASY_TEAM_MAP.values()).filterToList(t -> teamIds.contains(t.getId()));
        List<PlayerSeason> playerSeasons = playerSeasonService.getPlayerSeasonsByTeamIds(teamIds, LeagueUtil.SEASON);
        List<Player> players = playerService.getByIds($.of(playerSeasons).toList(PlayerSeason::getPlayerId));
        List<DraftDollar> draftDollars = draftDollarService.getDraftDollarsByTeams(teamIds);
        List<MinorLeaguePick> minorLeaguePicks = minorLeaguePickService.getMinorLeaguePicksByTeams(teamIds);
        List<Trade> trades = tradeService.getTradesByTeamIds(teamIds);

        List<PlayerView> playerViews = hydratePlayerViews(players, playerSeasons);
        List<DraftDollarView> draftDollarViews = hydrateDraftDollarViews(draftDollars);
        List<MinorLeaguePickView> minorLeaguePickViews = hydrateMinorLeaguePickViews(minorLeaguePicks);
        List<TradeView> tradeViews = hydrateTradeViews(trades);
        Multimap<Long, PlayerView> teamToPlayers = Multimaps.index(playerViews, pv -> pv.getCurrentSeason().getTeamId());
        Multimap<Long, DraftDollarView> teamToDraftDollars = Multimaps.index(draftDollarViews, DraftDollarView::getTeamId);
        Multimap<Long, MinorLeaguePickView> teamToMinorLeaguePicks = Multimaps.index(minorLeaguePickViews, MinorLeaguePickView::getOwningTeamId);
        Multimap<Long, TradeView> team1ToTrades = Multimaps.index(tradeViews, TradeView::getTeam1Id);
        Multimap<Long, TradeView> team2ToTrades = Multimaps.index(tradeViews, TradeView::getTeam2Id);

        teams.forEach(t -> {
            TeamView tv = TeamView.from(t);
            List<PlayerView> playersForTeam = $.of(teamToPlayers.get(t.getId())).toList();
            int salary = $.of(playersForTeam).reduceToInt(pv -> pv.getCurrentSeason().getSalary());
            tv.setMajorLeaguers($.of(playersForTeam).filterToList(p -> p.getCurrentSeason().getFantasyPosition() != Position.MINORLEAGUES));
            tv.setMinorLeaguers($.of(playersForTeam).filterToList(p -> p.getCurrentSeason().getFantasyPosition() == Position.MINORLEAGUES));
            tv.setSalary(salary);
            tv.setDraftDollars($.of(teamToDraftDollars.get(t.getId())).toList());
            tv.setMinorLeaguePicks($.of(teamToMinorLeaguePicks.get(t.getId())).toList());
            List<TradeView> teamTradeViews = $.of(team1ToTrades.get(t.getId())).toList();
            teamTradeViews.addAll(team2ToTrades.get(t.getId()));
            tv.setTrades(teamTradeViews);
            teamViews.add(tv);
        });
        return teamViews;
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
            psv.setTeam(FANTASY_TEAM_MAP.get(psv.getTeamId()));
            psv.setKeeperTeam(FANTASY_TEAM_MAP.get(psv.getKeeperTeamId()));
            playerSeasonViews.add(psv);
        });
        return playerSeasonViews;
    }

    private List<DraftDollarView> hydrateDraftDollarViews(Collection<DraftDollar> draftDollars) {
        List<DraftDollarView> draftDollarViews = Lists.newArrayList();
        draftDollars.forEach(dd -> {
            DraftDollarView ddv = DraftDollarView.from(dd);
            ddv.setTeam(FANTASY_TEAM_MAP.get(dd.getTeamId()));
            draftDollarViews.add(ddv);
        });
        return draftDollarViews;
    }

    private List<MinorLeaguePickView> hydrateMinorLeaguePickViews(Collection<MinorLeaguePick> minorLeaguePicks) {
        List<MinorLeaguePickView> minorLeaguePickViews = Lists.newArrayList();
        List<Player> minorLeaguePickPlayers = playerService.getByIds($.of(minorLeaguePicks).filter(mlp -> mlp.getPlayerId() != null).toIdList());
        Map<Long, Player> minorLeaguePickPlayerMap = $.of(minorLeaguePickPlayers).toIdMap();
        minorLeaguePicks.forEach(mlp -> {
            MinorLeaguePickView mlpv = MinorLeaguePickView.from(mlp);
            mlpv.setOriginalTeam(FANTASY_TEAM_MAP.get(mlpv.getOriginalTeamId()));
            mlpv.setOwningTeam(FANTASY_TEAM_MAP.get(mlpv.getOwningTeamId()));
            if(mlpv.getSwapTeamId() != null) {
                mlpv.setSwapTeam(FANTASY_TEAM_MAP.get(mlpv.getSwapTeamId()));
            }
            if(mlpv.getPlayerId() != null) {
                mlpv.setPlayer(minorLeaguePickPlayerMap.get(mlpv.getPlayerId()));
            }
            minorLeaguePickViews.add(mlpv);
        });
        return minorLeaguePickViews;
    }

    private List<TradeView> hydrateTradeViews(Collection<Trade> trades) {
        List<TradeView> tradeViews = Lists.newArrayList();

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
            TradeView tv = TradeView.from(t);
            tv.setTeam1(FANTASY_TEAM_MAP.get(tv.getTeam1Id()));
            tv.setTeam2(FANTASY_TEAM_MAP.get(tv.getTeam2Id()));

            List<TradeElement> tradeElements = $.of(tradeElementMap.get(tv.getId())).toList();
            tradeElements.forEach(te -> {
                TradeElementView tev = TradeElementView.from(te);
                tev.setPlayer(playerMap.get(tev.getPlayerId()));
                tev.setMinorLeaguePick(minorLeaguePickMap.get(tev.getMinorLeaguePickId()));
                tev.setDraftDollar(draftDollarMap.get(tev.getDraftDollarId()));
                tv.getTradeElements().add(tev);
            });

            tradeViews.add(tv);
        });

        return tradeViews;
    }

    // endregion
}
