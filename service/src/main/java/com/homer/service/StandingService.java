package com.homer.service;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.homer.data.common.IStandingRepository;
import com.homer.external.common.mlb.HittingStats;
import com.homer.external.common.mlb.PitchingStats;
import com.homer.service.utility.ESPNUtility;
import com.homer.type.*;
import com.homer.type.history.HistoryStanding;
import com.homer.util.HomerBeanUtil;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import static com.homer.service.utility.StatsUtility.calculateEra;
import static com.homer.service.utility.StatsUtility.calculateOBP;
import static com.homer.service.utility.StatsUtility.calculateWhip;

/**
 * Created by arigolub on 7/30/16.
 */
public class StandingService extends BaseVersionedIdService<Standing, HistoryStanding> implements IStandingService {

    static final Logger logger = LoggerFactory.getLogger(StandingService.class);
    
    private static final String RUN = "run";
    private static final String HR = "hr";
    private static final String RBI = "rbi";
    private static final String SB = "sb";
    private static final String OBP = "obp";
    private static final String K = "k";
    private static final String WIN = "win";
    private static final String SAVE = "save";
    private static final String ERA = "era";
    private static final String WHIP = "whip";

    private IStandingRepository standingRepo;
    private ITeamDailyService teamDailyService;
    private IPlayerDailyService playerDailyService;

    public StandingService(IStandingRepository standingRepository, ITeamDailyService teamDailyService,
                           IPlayerDailyService playerDailyService) {
        super(standingRepository);
        this.standingRepo = standingRepository;
        this.teamDailyService = teamDailyService;
        this.playerDailyService = playerDailyService;
    }

    @Override
    public List<Standing> getByDate(DateTime date) {
        return standingRepo.getByDate(date);
    }

    @Override
    public List<Standing> sortStandings(List<Standing> standings) {
        List<StandingCategory> standingCategories = breakout(standings);

        Map<String, List<StandingCategory>> categoryMap =
                $.of(standingCategories).groupBy(StandingCategory::getCategory);
        
        $.of(categoryMap.values()).forEach(StandingService::sortStandingsCategory);
        
        Map<Long, List<StandingCategory>> teamMap = 
                $.of(standingCategories).groupBy(StandingCategory::getTeamId);
        
        regroup(standings, teamMap);
        standings.sort(Standing::compareTo);
        for(int i = 0; i < standings.size(); i++) {
            standings.get(i).setPlace(i + 1);
        }
        return standings;
    }

    @Override
    public List<Standing> computeStandingsForDate(DateTime date, boolean refreshTeamDailies) {
        logger.info("BEGIN: computeStandingsForDate, date:" + date.toString() +
                ", refreshTeamDailies:" + refreshTeamDailies);
        ScoringPeriod scoringPeriod = ESPNUtility.getScoringPeriod(date);
        List<TeamDaily> teamDailies;
        if (refreshTeamDailies) {
            teamDailies = teamDailyService.refreshTeamDailies(scoringPeriod);
        } else {
            teamDailies = teamDailyService.getByDate(date);
        }
        List<Standing> yesterdaysStandings = getByDate(scoringPeriod.getDate().minusDays(1));
        List<Standing> newStandings = computeNewStandings(scoringPeriod, teamDailies, yesterdaysStandings);
        List<Standing> sortedStandings = sortStandings(newStandings);
        $.of(sortedStandings).forEach(this::upsert);
        logger.info("DONE: computeStandingsForDate, date: " + date.toString());
        return sortedStandings;
    }

    @Override
    public List<Standing> getLatestStandings() {
        List<Standing> standings = standingRepo.getAll();
        standings.sort((s1, s2) -> s1.getDate().isBefore(s2.getDate()) ? 1 : s1.getDate().isAfter(s2.getDate()) ? -1 : 0);
        DateTime latestDate = standings.get(0).getDate();
        return $.of(standings).filterToList(s -> s.getDate().equals(latestDate));
    }

    @Override
    public List<Standing> computeStandingsBetweenDates(DateTime start, DateTime end) {
        logger.info("BEGIN: computeStandingsBetweenDates");
        Map<Long, List<TeamDaily>> teamDailyMap = $.of(teamDailyService.getBetweenDates(start, end)).groupBy(TeamDaily::getTeamId);
        List<Standing> standings = Lists.newArrayList();
        $.of(teamDailyMap.values()).forEach(teamDailies -> {
            Standing standing = new Standing();
            standing.setTeamId(teamDailies.get(0).getTeamId());

            standing.setAtBatsTotal($.of(teamDailies).reduceToInt(BaseDaily::getAtBats));
            standing.setHrTotal($.of(teamDailies).reduceToInt(BaseDaily::getHomeRuns));
            standing.setRunTotal($.of(teamDailies).reduceToInt(TeamDaily::getRuns));
            standing.setRbiTotal($.of(teamDailies).reduceToInt(TeamDaily::getRbi));
            standing.setSbTotal($.of(teamDailies).reduceToInt(TeamDaily::getStolenBases));
            standing.setHitByPitchesTotal($.of(teamDailies).reduceToInt(TeamDaily::getHitByPitches));
            standing.setSacFliesTotal($.of(teamDailies).reduceToInt(TeamDaily::getSacFlies));
            standing.setTotalBasesTotal($.of(teamDailies).reduceToInt(TeamDaily::getTotalBases));
            standing.setHitsTotal($.of(teamDailies).reduceToInt(TeamDaily::getHits));
            standing.setWalksTotal($.of(teamDailies).reduceToInt(TeamDaily::getWalks));
            standing.setObpTotal(calculateOBP(standing.getAtBatsTotal(), standing.getHitsTotal(), standing.getWalksTotal(),
                    standing.getSacFliesTotal(), standing.getHitByPitchesTotal()));
            standing.setWalksTotal($.of(teamDailies).reduceToInt(TeamDaily::getWalks));

            standing.setWinTotal($.of(teamDailies).reduceToInt(TeamDaily::getWins));
            standing.setSaveTotal($.of(teamDailies).reduceToInt(TeamDaily::getSaves));
            standing.setkTotal($.of(teamDailies).reduceToInt(TeamDaily::getPitcherStrikeouts));
            standing.setInningsPitchedTotal($.of(teamDailies).reduceToDouble(TeamDaily::getInningsPitched));
            standing.setEarnedRunsTotal($.of(teamDailies).reduceToInt(TeamDaily::getEarnedRuns));
            standing.setPitcherWalksTotal($.of(teamDailies).reduceToInt(TeamDaily::getPitcherWalks));
            standing.setPitcherHitsTotal($.of(teamDailies).reduceToInt(TeamDaily::getPitcherHits));
            standing.setEraTotal(calculateEra(standing.getInningsPitchedTotal(), standing.getEarnedRunsTotal()));
            standing.setWhipTotal(calculateWhip(standing.getInningsPitchedTotal(), standing.getPitcherHitsTotal(), standing.getPitcherWalksTotal()));

            standings.add(standing);
        });
        List<Standing> sortedStandings = sortStandings(standings);
        logger.info("DONE: computeStandingsBetweenDates");
        return sortedStandings;
    }

    @Override
    public List<PlayerDaily> getActiveStatsForTeam(long teamId) {
        List<PlayerDaily> response = Lists.newArrayList();
        Map<Long, List<PlayerDaily>> playerDailiesMap =
                $.of(playerDailyService.getByTeam(teamId, LeagueUtil.SEASON)).groupBy(PlayerDaily::getPlayerId);

        for (List<PlayerDaily> playerDailies : playerDailiesMap.values()) {
            if (playerDailies.size() == 0) {
                continue;
            }
            PlayerDaily pd = new PlayerDaily();
            pd.setPlayer($.of(playerDailies).first().getPlayer());
            pd.setHits($.of(playerDailies).reduceToInt(PlayerDaily::getHits));
            pd.setAtBats($.of(playerDailies).reduceToInt(PlayerDaily::getAtBats));
            pd.setRuns($.of(playerDailies).reduceToInt(PlayerDaily::getRuns));
            pd.setRbi($.of(playerDailies).reduceToInt(PlayerDaily::getRbi));
            pd.setHomeRuns($.of(playerDailies).reduceToInt(PlayerDaily::getHomeRuns));
            pd.setStolenBases($.of(playerDailies).reduceToInt(PlayerDaily::getStolenBases));
            pd.setWalks($.of(playerDailies).reduceToInt(PlayerDaily::getWalks));
            pd.setHitByPitches($.of(playerDailies).reduceToInt(PlayerDaily::getHitByPitches));
            pd.setSacFlies($.of(playerDailies).reduceToInt(PlayerDaily::getSacFlies));
            pd.setTotalBases($.of(playerDailies).reduceToInt(PlayerDaily::getTotalBases));
            pd.setWins($.of(playerDailies).reduceToInt(PlayerDaily::getWins));
            pd.setSaves($.of(playerDailies).reduceToInt(PlayerDaily::getSaves));
            pd.setStrikeouts($.of(playerDailies).reduceToInt(PlayerDaily::getStrikeouts));
            pd.setHits($.of(playerDailies).reduceToInt(PlayerDaily::getHits));
            pd.setWalks($.of(playerDailies).reduceToInt(PlayerDaily::getWalks));
            pd.setEarnedRuns($.of(playerDailies).reduceToInt(PlayerDaily::getEarnedRuns));
            pd.setInningsPitched($.of(playerDailies).reduceToDouble(PlayerDaily::getInningsPitched));

            pd.setObp(calculateOBP(pd.getAtBats(), pd.getHits(), pd.getWalks(), pd.getSacFlies(), pd.getHitByPitches()));
            pd.setEra(calculateEra(pd.getInningsPitched(), pd.getEarnedRuns()));
            pd.setWhip(calculateWhip(pd.getInningsPitched(), pd.getHits(), pd.getWalks()));

            response.add(pd);
        }
        return $.of(response).sorted((pd1, pd2) -> {
            if (pd1.getPlayer().isBatter() && !pd2.getPlayer().isBatter()) {
                return -1;
            } else if (!pd1.getPlayer().isBatter() && pd2.getPlayer().isBatter()) {
                return 1;
            } else if (pd1.getPlayer().isBatter() && pd2.getPlayer().isBatter()) {
                return Integer.compare(pd1.getAtBats(), pd2.getAtBats()) * -1;
            } else if (!pd1.getPlayer().isBatter() && !pd2.getPlayer().isBatter()) {
                return Double.compare(pd1.getInningsPitched(), pd2.getInningsPitched()) * -1;
            } else {
                return 0;
            }
        });
    }

    // region helpers

    private static List<Standing> computeNewStandings(ScoringPeriod scoringPeriod, List<TeamDaily> teamDailies, List<Standing> yesterdaysStandings) {
        Map<Long, Standing> yesterdaysStandingsMap = $.of(yesterdaysStandings).toMap(Standing::getTeamId);
        return $.of(teamDailies).toList(teamDaily -> {
            Standing yesterdaysStanding = yesterdaysStandingsMap.get(teamDaily.getTeamId());
            if (yesterdaysStanding == null) {
                yesterdaysStanding = new Standing();
                yesterdaysStanding.setTeamId(teamDaily.getTeamId());
                yesterdaysStanding.setDate(scoringPeriod.getDate().minusDays(1));
            }

            Standing newStanding = HomerBeanUtil.cloneBean(yesterdaysStanding);
            newStanding.setDate(newStanding.getDate().plusDays(1));
            addValueAndResetPoints(teamDaily.getRuns(), (x, y) -> x + y, newStanding, Standing::getRunTotal, Standing::setRunTotal, Standing::setRunPoints);
            addValueAndResetPoints(teamDaily.getWalks(), (x, y) -> x + y, newStanding, Standing::getWalksTotal, Standing::setWalksTotal, (x, y) -> {});
            addValueAndResetPoints(teamDaily.getHomeRuns(), (x, y) -> x + y, newStanding, Standing::getHrTotal, Standing::setHrTotal, Standing::setHrPoints);
            addValueAndResetPoints(teamDaily.getRbi(), (x, y) -> x + y, newStanding, Standing::getRbiTotal, Standing::setRbiTotal, Standing::setRbiPoints);
            addValueAndResetPoints(teamDaily.getStolenBases(), (x, y) -> x + y, newStanding, Standing::getSbTotal, Standing::setSbTotal, Standing::setSbPoints);
            addValueAndResetPoints(teamDaily.getHitByPitches(), (x, y) -> x + y, newStanding, Standing::getHitByPitchesTotal, Standing::setHitByPitchesTotal, (x, y) -> {});
            addValueAndResetPoints(teamDaily.getSacFlies(), (x, y) -> x + y, newStanding, Standing::getSacFliesTotal, Standing::setSacFliesTotal, (x, y) -> {});
            addValueAndResetPoints(teamDaily.getTotalBases(), (x, y) -> x + y, newStanding, Standing::getTotalBasesTotal, Standing::setTotalBasesTotal, (x, y) -> {});
            addValueAndResetPoints(teamDaily.getHits(), (x, y) -> x + y, newStanding, Standing::getHitsTotal, Standing::setHitsTotal, (x, y) -> {});
            addValueAndResetPoints(teamDaily.getAtBats(), (x, y) -> x + y, newStanding, Standing::getAtBatsTotal, Standing::setAtBatsTotal, (x, y) -> {});
            newStanding.setObpTotal(calculateOBP(newStanding.getAtBatsTotal(), newStanding.getHitsTotal(), newStanding.getWalksTotal(), newStanding.getSacFliesTotal(), newStanding.getHitByPitchesTotal()));

            addValueAndResetPoints(teamDaily.getPitcherStrikeouts(), (x, y) -> x + y, newStanding, Standing::getkTotal, Standing::setkTotal, Standing::setkPoints);
            addValueAndResetPoints(teamDaily.getWins(), (x, y) -> x + y, newStanding, Standing::getWinTotal, Standing::setWinTotal, Standing::setWinPoints);
            addValueAndResetPoints(teamDaily.getSaves(), (x, y) -> x + y, newStanding, Standing::getSaveTotal, Standing::setSaveTotal, Standing::setSavePoints);
            addValueAndResetPoints(teamDaily.getPitcherWalks(), (x, y) -> x + y, newStanding, Standing::getPitcherWalksTotal, Standing::setPitcherWalksTotal, (x, y) -> {});
            addValueAndResetPoints(teamDaily.getPitcherHits(), (x, y) -> x + y, newStanding, Standing::getPitcherHitsTotal, Standing::setPitcherHitsTotal, (x, y) -> {});
            addValueAndResetPoints(teamDaily.getInningsPitched(), (x, y) -> x + y, newStanding, Standing::getInningsPitchedTotal, Standing::setInningsPitchedTotal, (x, y) -> {});
            addValueAndResetPoints(teamDaily.getEarnedRuns(), (x, y) -> x + y, newStanding, Standing::getEarnedRunsTotal , Standing::setEarnedRunsTotal, (x, y) -> {});
            newStanding.setWhipTotal(calculateWhip(newStanding.getInningsPitchedTotal(), newStanding.getPitcherHitsTotal(), newStanding.getPitcherWalksTotal()));
            newStanding.setEraTotal(calculateEra(newStanding.getInningsPitchedTotal(), newStanding.getEarnedRunsTotal()));
            return newStanding;
        });
    }

    private static <T extends Number> void addValueAndResetPoints(T newStat, BiFunction<T, T, T> addFunc, Standing standing, Function<Standing, T> getter, BiConsumer<Standing, T> setter,
                                                                  BiConsumer<Standing, Double> pointsResetter) {
        setter.accept(standing, addFunc.apply(getter.apply(standing), newStat));
        pointsResetter.accept(standing, 0.0);
    }

    private static void sortStandingsCategory(List<StandingCategory> standingCategories) {
        standingCategories.sort(StandingCategory::compareTo);
        for(int i = 0; i < standingCategories.size();) {
            List<Integer> indexes = Lists.newArrayList();
            Double categoryValue = standingCategories.get(i).getResult();
            while(i < standingCategories.size() && Objects.equals(categoryValue, standingCategories.get(i).getResult())) {
                indexes.add(i);
                i++;
            }
            double average = indexes.stream().reduce(0, (runningTotal, next) -> runningTotal += standingCategories.size() - next) / (double)indexes.size();
            indexes.forEach(teamIndex -> standingCategories.get(teamIndex).setPoints(average));
        }
    }

    private static List<StandingCategory> breakout(List<Standing> standings) {
        List<StandingCategory> standingCategories = Lists.newArrayList();
        standingCategories.addAll(breakout(RUN, s -> (double)s.getRunTotal(), standings));
        standingCategories.addAll(breakout(HR, s -> (double)s.getHrTotal(), standings));
        standingCategories.addAll(breakout(RBI, s -> (double)s.getRbiTotal(), standings));
        standingCategories.addAll(breakout(SB, s -> (double)s.getSbTotal(), standings));
        standingCategories.addAll(breakout(OBP, Standing::getObpTotal, standings));

        standingCategories.addAll(breakout(K, s -> (double)s.getkTotal(), standings));
        standingCategories.addAll(breakout(WIN, s -> (double)s.getWinTotal(), standings));
        standingCategories.addAll(breakout(SAVE, s -> (double)s.getSaveTotal(), standings));
        standingCategories.addAll(breakout(ERA, Standing::getEraTotal, standings));
        standingCategories.addAll(breakout(WHIP, Standing::getWhipTotal, standings));
        return standingCategories;
    }

    private static List<StandingCategory> breakout(String category, Function<Standing, Double> func, List<Standing> standings) {
        return $.of(standings).toList(s -> new StandingCategory(s.getTeamId(), category, func.apply(s)));
    }
    
    private static void regroup(List<Standing> standings, Map<Long, List<StandingCategory>> teamMap) {
        $.of(standings).forEach(s -> {
            List<StandingCategory> teamCats = teamMap.get(s.getTeamId());
            regroup(s, Standing::setRunPoints, $.of(teamCats).filter(tc -> tc.getCategory().equals(RUN)).first());
            regroup(s, Standing::setHrPoints, $.of(teamCats).filter(tc -> tc.getCategory().equals(HR)).first());
            regroup(s, Standing::setRbiPoints, $.of(teamCats).filter(tc -> tc.getCategory().equals(RBI)).first());
            regroup(s, Standing::setSbPoints, $.of(teamCats).filter(tc -> tc.getCategory().equals(SB)).first());
            regroup(s, Standing::setObpPoints, $.of(teamCats).filter(tc -> tc.getCategory().equals(OBP)).first());

            regroup(s, Standing::setkPoints, $.of(teamCats).filter(tc -> tc.getCategory().equals(K)).first());
            regroup(s, Standing::setWinPoints, $.of(teamCats).filter(tc -> tc.getCategory().equals(WIN)).first());
            regroup(s, Standing::setSavePoints, $.of(teamCats).filter(tc -> tc.getCategory().equals(SAVE)).first());
            regroup(s, Standing::setEraPoints, $.of(teamCats).filter(tc -> tc.getCategory().equals(ERA)).first());
            regroup(s, Standing::setWhipPoints, $.of(teamCats).filter(tc -> tc.getCategory().equals(WHIP)).first());

            s.setTotalPoints($.of(teamCats).reduceToDouble(StandingCategory::getPoints));
        });
    }
    
    private static void regroup(Standing standing, BiConsumer<Standing, Double> pointSetter, StandingCategory standingCategory) {
        pointSetter.accept(standing, standingCategory.getPoints());
    }

    // endregion
}
