package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.data.common.IPlayerDailyRepository;
import com.homer.external.common.IMLBClient;
import com.homer.external.common.espn.ESPNPlayer;
import com.homer.external.common.espn.IESPNClient;
import com.homer.external.common.mlb.BaseStats;
import com.homer.external.common.mlb.HittingStats;
import com.homer.external.common.mlb.PitchingStats;
import com.homer.external.common.mlb.Stats;
import com.homer.type.Player;
import com.homer.type.PlayerDaily;
import com.homer.type.Team;
import com.homer.util.core.$;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.homer.service.utility.ESPNUtility.translateESPNPosition;

/**
 * Created by arigolub on 7/29/16.
 */
public class PlayerDailyService extends BaseIdService<PlayerDaily> implements IPlayerDailyService {

    final static Logger logger = LoggerFactory.getLogger(PlayerDailyService.class);

    private static final DateTime SCORING_PERIOD_1 = DateTime.parse("2016-04-03T12:00:00");

    private IPlayerService playerService;
    private IESPNClient espnClient;
    private IMLBClient mlbClient;
    private ITeamService teamService;

    public PlayerDailyService(IPlayerDailyRepository playerDailyRepository,
                              IPlayerService playerService, IESPNClient espnClient, IMLBClient mlbClient,
                              ITeamService teamService) {
        super(playerDailyRepository);
        this.playerService = playerService;
        this.espnClient = espnClient;
        this.mlbClient = mlbClient;
        this.teamService = teamService;
    }

    public List<PlayerDaily> refreshPlayerDailies() {
        DateTime now = DateTime.now().minusHours(6).withMillisOfDay(0);
        int scoringPeriodId = now.dayOfYear().get() - SCORING_PERIOD_1.dayOfYear().get() + 1;
        Map<Long, Team> teamMap = teamService.getFantasyTeamMap();
        List<PlayerDaily> playerDailies = Lists.newArrayList();
        for (Team team : teamMap.values()) {
            playerDailies.addAll(refreshPlayerDailies((int)team.getId(), now, scoringPeriodId));
        }
        return playerDailies;
    }

    public List<PlayerDaily> refreshPlayerDailies(int teamId, DateTime date, int scoringPeriodId) {
        logger.info("Date: " + date + ", Scoring Period: " + scoringPeriodId);
        List<PlayerDaily> playerDailies = Lists.newArrayList();

        List<ESPNPlayer> espnPlayers = espnClient.getRoster(teamId, scoringPeriodId);
        logger.info("Found " + espnPlayers.size() + " ESPN players");
        Map<String, Player> homerPlayers =
                $.of(playerService.getPlayersByNames($.of(espnPlayers).toList(ESPNPlayer::getName))).toMap(Player::getName);
        logger.info("Found " + homerPlayers.keySet().size() + " Homer players");
        for (ESPNPlayer espnPlayer : espnPlayers) {
            try {
                Player player = homerPlayers.get(espnPlayer.getName());
                if (player == null) {
                    logger.info("No Homer player for ESPN player with name " + espnPlayer.getName());
                    continue;
                }
                if (player.getEspnPlayerId() == null) {
                    logger.info("Saving new ESPN player id for " + player.getName() + ", espnPlayerId: " + espnPlayer.getEspnPlayerId());
                    player.setEspnPlayerId(new Long(espnPlayer.getEspnPlayerId()));
                    playerService.upsert(player);
                }

                PlayerDaily pd = new PlayerDaily();
                pd.setPlayerId(player.getId());
                pd.setTeamId(new Long(espnPlayer.getTeamId()));
                pd.setDate(date);
                pd.setFantasyPosition(translateESPNPosition(espnPlayer.getPosition()));
                pd.setScoringPeriodId(scoringPeriodId);

                Stats stats = mlbClient.getStats(player.getMlbPlayerId(), player.isBatter());
                BaseStats singleStat = getSingleStats(stats, date);
                logger.info("Stats found for " + player.getName() + ": " + (singleStat != null));
                if (singleStat != null) {
                    if (player.isBatter()) {
                        HittingStats hittingStats = (HittingStats) singleStat;
                        pd.setAtBats(hittingStats.getAtBats());
                        pd.setRuns(hittingStats.getRuns());
                        pd.setRbi(hittingStats.getRbi());
                        pd.setHomeRuns(hittingStats.getHomeRuns());
                        pd.setStolenBases(hittingStats.getStolenBases());
                        pd.setWalks(hittingStats.getWalks());
                        pd.setHitByPitches(hittingStats.getHitByPitches());
                        pd.setSacFlies(hittingStats.getSacFlies());
                        pd.setTotalBases(hittingStats.getTotalBases());
                    } else {
                        PitchingStats pitchingStats = (PitchingStats) singleStat;
                        pd.setInningsPitched(pitchingStats.getInningsPitched());
                        pd.setWins(pitchingStats.getWins());
                        pd.setSaves(pitchingStats.getSaves());
                        pd.setStrikeouts(pitchingStats.getStrikeouts());
                        pd.setHits(pitchingStats.getHits());
                        pd.setWalks(pitchingStats.getWalks());
                        pd.setEarnedRuns(pitchingStats.getEarnedRuns());
                    }
                }
                this.upsert(pd);
                playerDailies.add(pd);
            } catch (Exception e) {
                logger.error("Error creating PlayerDaily for " + espnPlayer.getName(), e);
            }
        }
        return playerDailies;
    }

    private static BaseStats getSingleStats(Stats stats, DateTime date) {
        return (BaseStats)
                $.of(stats.getGameLog()).first(s -> ((BaseStats)s).getGameDate().dayOfYear().get() == date.dayOfYear().get());
    }
}
