package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.data.common.ITeamDailyRepository;
import com.homer.service.utility.ESPNUtility;
import com.homer.type.*;
import com.homer.util.core.$;
import com.homer.util.core.data.IRepository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by arigolub on 7/30/16.
 */
public class TeamDailyService extends BaseIdService<TeamDaily> implements ITeamDailyService {

    final static Logger logger = LoggerFactory.getLogger(PlayerDailyService.class);

    private ITeamDailyRepository teamDailyRepository;
    private IPlayerDailyService playerDailyService;
    private ITeamService teamService;

    public TeamDailyService(ITeamDailyRepository teamDailyRepository, IPlayerDailyService playerDailyService,
                            ITeamService teamService) {
        super(teamDailyRepository);
        this.teamDailyRepository = teamDailyRepository;
        this.playerDailyService = playerDailyService;
        this.teamService = teamService;
    }

    @Override
    public List<TeamDaily> getByDate(DateTime date) {
        return teamDailyRepository.getByDate(date);
    }

    @Override
    public List<TeamDaily> getBetweenDates(DateTime start, DateTime end) {
        return $.of(teamDailyRepository.getAll())
                .filterToList(td -> td.getDate().withMillisOfDay(0).isBefore(end.withMillisOfDay(100)) &&
                        td.getDate().withMillisOfDay(0).isAfter(start.withMillisOfDay(100)));
    }

    @Override
    public List<TeamDaily> refreshTeamDailies() {
        return refreshTeamDailies(ESPNUtility.getTodaysScoringPeriod());
    }

    @Override
    public List<TeamDaily> refreshTeamDailies(ScoringPeriod scoringPeriod) {
        Map<Long, Team> teamMap = teamService.getFantasyTeamMap();
        List<TeamDaily> teamDailies = Lists.newArrayList();

        ExecutorService executor = Executors.newWorkStealingPool();
        List<Callable<TeamDaily>> callables = $.of(teamMap.values()).toList(team ->
                () -> refreshTeamDaily((int)team.getId(), scoringPeriod.getDate(), scoringPeriod.getScoringPeriodId()));
        try {
            $.of(executor.invokeAll(callables)).forEach(future -> {
                try {
                    TeamDaily td = future.get();
                    teamDailies.add(td);
                    logger.info("Done with team " + td.getTeamId());
                } catch (Exception e) {
                    logger.error("Error working with future", e);
                }
            });
        } catch (InterruptedException e) {
            logger.error("Error processing TeamDailies", e);
        }
        return teamDailies;
    }

    @Override
    public TeamDaily refreshTeamDaily(int teamId, DateTime date, int scoringPeriodId) {
        logger.info("teamId: " + teamId + ", date: " + date);
        List<PlayerDaily> playerDailies;

        playerDailies = playerDailyService.refreshPlayerDailies(teamId, date, scoringPeriodId);

        //exclude bench + DL players
        playerDailies = $.of(playerDailies).filter(pd -> pd.getFantasyPosition() != Position.DISABLEDLIST &&
                pd.getFantasyPosition() != Position.BENCH).toList();

        TeamDaily teamDaily = new TeamDaily();
        teamDaily.setTeamId(teamId);
        teamDaily.setDate(date);
        teamDaily.setScoringPeriodId(scoringPeriodId);

        teamDaily.setAtBats($.of(playerDailies).reduceToInt(PlayerDaily::getAtBats));
        teamDaily.setHomeRuns($.of(playerDailies).reduceToInt(PlayerDaily::getHomeRuns));
        teamDaily.setRuns($.of(playerDailies).reduceToInt(PlayerDaily::getRuns));
        teamDaily.setRbi($.of(playerDailies).reduceToInt(PlayerDaily::getRbi));
        teamDaily.setStolenBases($.of(playerDailies).reduceToInt(PlayerDaily::getStolenBases));
        teamDaily.setHitByPitches($.of(playerDailies).reduceToInt(PlayerDaily::getHitByPitches));
        teamDaily.setSacFlies($.of(playerDailies).reduceToInt(PlayerDaily::getSacFlies));
        teamDaily.setTotalBases($.of(playerDailies).reduceToInt(PlayerDaily::getTotalBases));
        teamDaily.setWins($.of(playerDailies).reduceToInt(PlayerDaily::getWins));
        teamDaily.setSaves($.of(playerDailies).reduceToInt(PlayerDaily::getSaves));
        teamDaily.setInningsPitched($.of(playerDailies).reduceToDouble(PlayerDaily::getInningsPitched));
        teamDaily.setEarnedRuns($.of(playerDailies).reduceToInt(PlayerDaily::getEarnedRuns));

        teamDaily.setWalks($.of(playerDailies).filter(pd -> pd.getPlayer().isBatter()).reduceToInt(PlayerDaily::getWalks));
        teamDaily.setPitcherWalks($.of(playerDailies).filter(pd -> !pd.getPlayer().isBatter()).reduceToInt(PlayerDaily::getWalks));

        teamDaily.setHits($.of(playerDailies).filter(pd -> pd.getPlayer().isBatter()).reduceToInt(PlayerDaily::getHits));
        teamDaily.setPitcherHits($.of(playerDailies).filter(pd -> !pd.getPlayer().isBatter()).reduceToInt(PlayerDaily::getHits));

        teamDaily.setStrikeouts($.of(playerDailies).filter(pd -> pd.getPlayer().isBatter()).reduceToInt(PlayerDaily::getStrikeouts));
        teamDaily.setPitcherStrikeouts($.of(playerDailies).filter(pd -> !pd.getPlayer().isBatter()).reduceToInt(PlayerDaily::getStrikeouts));

        return this.upsert(teamDaily);
    }
}
