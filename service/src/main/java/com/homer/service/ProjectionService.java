package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.data.common.IProjectionRepository;
import com.homer.external.common.IFangraphsClient;
import com.homer.external.common.mlb.HittingStats;
import com.homer.external.common.mlb.PitchingStats;
import com.homer.external.common.mlb.Stats;
import com.homer.type.Player;
import com.homer.type.PlayerDaily;
import com.homer.type.PlayerSeason;
import com.homer.type.Projection;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by arigolub on 5/2/18.
 */
public class ProjectionService extends BaseIdService<Projection> implements IProjectionService {

    private IFangraphsClient fangraphsClient;
    private IProjectionRepository projectionRepo;
    private IPlayerSeasonService playerSeasonService;
    private IPlayerService playerService;

    public ProjectionService(IFangraphsClient fangraphsClient, IProjectionRepository projectionRepo, IPlayerSeasonService playerSeasonService, IPlayerService playerService) {
        super(projectionRepo);
        this.fangraphsClient = fangraphsClient;
        this.projectionRepo = projectionRepo;
        this.playerSeasonService = playerSeasonService;
        this.playerService = playerService;
    }

    @Override
    public List<Projection> saveProjections()
    {
        List<PlayerSeason> playerSeasons = playerSeasonService.getActivePlayers();
        List<Player> players = playerService.getByIds($.of(playerSeasons).toList(PlayerSeason::getPlayerId));
        List<Player> playersWithFangraphsId = $.of(players).filterToList(player -> player.getFangraphsPlayerId() != null);
        DateTime season = new DateTime().withYear(LeagueUtil.SEASON).withMonthOfYear(1).withDayOfMonth(1);
        List<Projection> projections = new ArrayList<>();
        ExecutorService executor = Executors.newWorkStealingPool();
        List<Callable<Projection>> callables = $.of(playersWithFangraphsId).toList(player -> () -> fetchAndSave(player, season));
        try {
            $.of(executor.invokeAll(callables)).forEach(future -> {
                try {
                    projections.add(future.get());
//                    logger.info("Done processing player dailies for team");
                } catch (Exception e) {
//                    logger.error("Error working with future", e);
                }
            });
        } catch (InterruptedException e) {
//            logger.error("Error processing TeamDailies", e);
        }
        return projections;
    }

    private Projection fetchAndSave(Player player, DateTime season)
    {
        Stats stats = fangraphsClient.getProjections(checkNotNull(player.getFangraphsPlayerId()), player.isBatter());
        Projection projection = new Projection();
        projection.setPlayerId(player.getId());
        projection.setDate(season);
        if (player.isBatter()) {
            HittingStats hittingStats = (HittingStats) stats.getSeasonStats();
            checkNotNull(hittingStats);
            projection.setHits(toInt(hittingStats.getHits()));
            projection.setAtBats(toInt(hittingStats.getAtBats()));
            projection.setRuns(toInt(hittingStats.getRuns()));
            projection.setRbi(toInt(hittingStats.getRbi()));
            projection.setHomeRuns(toInt(hittingStats.getHomeRuns()));
            projection.setStolenBases(toInt(hittingStats.getStolenBases()));
            projection.setWalks(toInt(hittingStats.getWalks()));
            projection.setHitByPitches(toInt(hittingStats.getHitByPitches()));
            projection.setSacFlies(toInt(hittingStats.getSacFlies()));
            projection.setTotalBases(toInt(hittingStats.getTotalBases()));
        } else {
            PitchingStats pitchingStats = (PitchingStats) stats.getSeasonStats();
            checkNotNull(pitchingStats);
            projection.setInningsPitched(toDouble(pitchingStats.getInningsPitched()));
            projection.setWins(toInt(pitchingStats.getWins()));
            projection.setSaves(toInt(pitchingStats.getSaves()));
            projection.setStrikeouts(toInt(pitchingStats.getStrikeouts()));
            projection.setHits(toInt(pitchingStats.getHits()));
            projection.setWalks(toInt(pitchingStats.getWalks()));
            projection.setEarnedRuns(toInt(pitchingStats.getEarnedRuns()));
        }
        return projectionRepo.upsertNoHistory(projection);
    }

    private static int toInt(@Nullable Integer num) {
        return num == null ? 0 : num;
    }

    private static double toDouble(@Nullable Double num) {
        return num == null ? 0 : num;
    }
}
