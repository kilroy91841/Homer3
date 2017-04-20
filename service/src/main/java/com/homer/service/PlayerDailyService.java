package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.data.common.IPlayerDailyRepository;
import com.homer.email.EmailRequest;
import com.homer.email.HtmlObject;
import com.homer.email.HtmlTag;
import com.homer.email.IEmailService;
import com.homer.exception.PlayerDailyException;
import com.homer.external.common.IMLBClient;
import com.homer.external.common.espn.ESPNPlayer;
import com.homer.external.common.espn.IESPNClient;
import com.homer.external.common.mlb.BaseStats;
import com.homer.external.common.mlb.HittingStats;
import com.homer.external.common.mlb.PitchingStats;
import com.homer.external.common.mlb.Stats;
import com.homer.service.utility.ESPNUtility;
import com.homer.type.*;
import com.homer.type.history.HistoryPlayerDaily;
import com.homer.util.core.$;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.homer.service.utility.ESPNUtility.translateESPNPosition;

/**
 * Created by arigolub on 7/29/16.
 */
public class PlayerDailyService extends BaseVersionedIdService<PlayerDaily, HistoryPlayerDaily> implements IPlayerDailyService {

    final static Logger logger = LoggerFactory.getLogger(PlayerDailyService.class);

    private IPlayerDailyRepository playerDailyRepo;
    private IPlayerService playerService;
    private IESPNClient espnClient;
    private IMLBClient mlbClient;
    private ITeamService teamService;
    private IEmailService emailService;

    public PlayerDailyService(IPlayerDailyRepository playerDailyRepository,
                              IPlayerService playerService, IESPNClient espnClient, IMLBClient mlbClient,
                              ITeamService teamService, IEmailService emailService) {
        super(playerDailyRepository);
        this.playerDailyRepo = playerDailyRepository;
        this.playerService = playerService;
        this.espnClient = espnClient;
        this.mlbClient = mlbClient;
        this.teamService = teamService;
        this.emailService = emailService;
    }

    @Override
    public List<PlayerDaily> getByDate(int teamId, DateTime date) {
        return playerDailyRepo.getByDate(teamId, date);
    }

    @Override
    public List<PlayerDaily> getByTeam(long teamId, int season) {
        List<PlayerDaily> playerDailies = playerDailyRepo.getByTeam(teamId);
        playerDailies = $.of(playerDailies).filterToList(pd -> pd.getDate().getYear() == season);
        Map<Long, Player> playersMap = $.of(playerService.getByIds($.of(playerDailies).toList(PlayerDaily::getPlayerId))).toIdMap();
        return $.of(playerDailies).toList(pd -> {
            pd.setPlayer(playersMap.get(pd.getPlayerId()));
            return pd;
        });
    }

    @Override
    public List<PlayerDaily> refreshPlayerDailies() {
        return refreshPlayerDailies(ESPNUtility.getTodaysScoringPeriod());
    }

    @Override
    public List<PlayerDaily> refreshPlayerDailies(ScoringPeriod scoringPeriod) {
        Map<Long, Team> teamMap = teamService.getFantasyTeamMap();
        ExecutorService executor = Executors.newWorkStealingPool();
        List<Callable<List<PlayerDaily>>> callables = $.of(teamMap.values()).toList(team ->
                () -> refreshPlayerDailies((int)team.getId(), scoringPeriod.getDate(), scoringPeriod.getScoringPeriodId()));
        List<PlayerDaily> playerDailies = Lists.newArrayList();
        try {
            $.of(executor.invokeAll(callables)).forEach(future -> {
                try {
                    playerDailies.addAll(future.get());
                    logger.info("Done processing player dailies for team");
                } catch (Exception e) {
                    logger.error("Error working with future", e);
                }
            });
        } catch (InterruptedException e) {
            logger.error("Error processing TeamDailies", e);
        }
        return playerDailies;
    }

    @Override
    public List<PlayerDaily> refreshPlayerDailies(int teamId, DateTime date, int scoringPeriodId) {
        logger.info("Date: " + date + ", Scoring Period: " + scoringPeriodId);
        List<PlayerDaily> playerDailies = Lists.newArrayList();

        List<ESPNPlayer> espnPlayers = espnClient.getRoster(teamId, scoringPeriodId);
        logger.info("Found " + espnPlayers.size() + " ESPN players");
        Map<String, Player> homerPlayers =
                $.of(playerService.getPlayersByNames($.of(espnPlayers).toList(ESPNPlayer::getName))).toMap(Player::getName);
        Map<Long, Player> homerPlayersByEspnPlayerId =
                $.of(playerService.getPlayersByEspnPlayerIds($.of(espnPlayers).toList(ESPNPlayer::getEspnPlayerId))).toMap(Player::getEspnPlayerId);
        logger.info("Found " + homerPlayers.keySet().size() + " Homer players");
        List<Callable<List<PlayerDaily>>> callables = $.of(espnPlayers).toList(espnPlayer ->
                () ->
        {
            try {
                Player player = homerPlayers.get(espnPlayer.getName());
                if (player == null) {
                    player = homerPlayersByEspnPlayerId.get(new Long(espnPlayer.getEspnPlayerId()));
                    if (player == null) {
                        String message = "No Homer player for ESPN player with name " + espnPlayer.getName();
                        logger.error(message);
                        throw new PlayerDailyException(message, new RuntimeException());
                    }
                }
                if (player.getEspnPlayerId() == null) {
                    logger.info("Saving new ESPN player id for " + player.getName() + ", espnPlayerId: " + espnPlayer.getEspnPlayerId());
                    player.setEspnPlayerId(new Long(espnPlayer.getEspnPlayerId()));
                    playerService.upsert(player);
                }

                if (player.getMlbPlayerId() == null) {
                    String message = "No MLB playerId for Homer player with name " + player.getName();
                    logger.error(message);
                    throw new PlayerDailyException(message, new RuntimeException());
                }

                Stats stats = mlbClient.getStats(player.getMlbPlayerId(), player.isBatter());
                List<BaseStats> dailyStats = getStatsOnDate(stats, date);

                logger.info("Stats found for " + player.getName() + ", count= " + dailyStats.size());
                List<PlayerDaily> playerDailiesForDate = Lists.newArrayList();
                for (BaseStats singleStat : dailyStats) {
                    PlayerDaily pd = new PlayerDaily();
                    pd.setPlayer(player);
                    pd.setPlayerId(player.getId());
                    pd.setDate(date);
                    pd.setTeamId(new Long(espnPlayer.getTeamId()));
                    pd.setFantasyPosition(translateESPNPosition(espnPlayer.getPosition()));
                    pd.setScoringPeriodId(scoringPeriodId);
                    pd.setGameId(getGameId(singleStat, date));
                    if (singleStat != null) {
                        if (player.isBatter()) {
                            HittingStats hittingStats = (HittingStats) singleStat;
                            pd.setHits(toInt(hittingStats.getHits()));
                            pd.setAtBats(toInt(hittingStats.getAtBats()));
                            pd.setRuns(toInt(hittingStats.getRuns()));
                            pd.setRbi(toInt(hittingStats.getRbi()));
                            pd.setHomeRuns(toInt(hittingStats.getHomeRuns()));
                            pd.setStolenBases(toInt(hittingStats.getStolenBases()));
                            pd.setWalks(toInt(hittingStats.getWalks()));
                            pd.setHitByPitches(toInt(hittingStats.getHitByPitches()));
                            pd.setSacFlies(toInt(hittingStats.getSacFlies()));
                            pd.setTotalBases(toInt(hittingStats.getTotalBases()));
                        } else {
                            PitchingStats pitchingStats = (PitchingStats) singleStat;
                            pd.setInningsPitched(toDouble(pitchingStats.getInningsPitched()));
                            pd.setWins(toInt(pitchingStats.getWins()));
                            pd.setSaves(toInt(pitchingStats.getSaves()));
                            pd.setStrikeouts(toInt(pitchingStats.getStrikeouts()));
                            pd.setHits(toInt(pitchingStats.getHits()));
                            pd.setWalks(toInt(pitchingStats.getWalks()));
                            pd.setEarnedRuns(toInt(pitchingStats.getEarnedRuns()));
                        }
                    }
                    this.upsert(pd);
                    playerDailiesForDate.add(pd);
                }
                return playerDailiesForDate;
            } catch (Exception e) {
                logger.error("Error creating PlayerDaily for " + espnPlayer.getName(), e);
                throw new PlayerDailyException(e.getMessage(), e.getCause());
            }
        });
        ExecutorService executor = Executors.newWorkStealingPool();
        try {
            $.of(executor.invokeAll(callables)).forEach(future -> {
                try {
                    List<PlayerDaily> pd = future.get();
                    if (pd != null) {
                        playerDailies.addAll(pd);
                    }
                } catch (Exception e) {
                    logger.error("Error working with future: " + e.getMessage(), e);
                    sendPlayerDailyExceptionEmail(e);
                }
            });
        } catch (InterruptedException e) {
            logger.error("Error processing TeamDailies", e);
        }
        return playerDailies;
    }

    private void sendPlayerDailyExceptionEmail(Exception e) {
        HtmlObject htmlObject = HtmlObject.of(HtmlTag.DIV).child(HtmlObject.of(HtmlTag.P).body(e.getMessage()));
        EmailRequest emailRequest = new EmailRequest(Lists.newArrayList(IEmailService.COMMISSIONER_EMAIL),
                "Player Daily Exception", htmlObject);
        emailService.sendEmail(emailRequest);
    }

    private static List<BaseStats> getStatsOnDate(Stats<BaseStats> stats, DateTime date) {
        return $.of(stats.getGameLog())
                .filterToList(s -> s.getGameDate().dayOfYear().get() == date.dayOfYear().get());
    }

    private static String getGameId(BaseStats singleStat, DateTime date) {
        return singleStat != null ? singleStat.getGameId() : date.toString("YYYY-MM-dd");
    }

    private static int toInt(@Nullable Integer num) {
        return num == null ? 0 : num;
    }

    private static double toDouble(@Nullable Double num) {
        return num == null ? 0 : num;
    }
}
