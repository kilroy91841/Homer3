package com.homer.service.full;

import com.google.common.collect.Lists;
import com.homer.external.common.IMLBClient;
import com.homer.external.common.espn.IESPNClient;
import com.homer.external.common.mlb.BaseStats;
import com.homer.external.common.mlb.HittingStats;
import com.homer.external.common.mlb.PitchingStats;
import com.homer.external.common.mlb.Stats;
import com.homer.service.IPlayerSeasonService;
import com.homer.service.IPlayerService;
import com.homer.service.ITransactionService;
import com.homer.type.Player;
import com.homer.type.PlayerSeason;
import com.homer.type.Position;
import com.homer.type.view.PlayerSeasonView;
import com.homer.type.view.PlayerView;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by arigolub on 4/17/16.
 */
public class FullPlayerService implements IFullPlayerService {

    final static Logger logger = LoggerFactory.getLogger(FullPlayerService.class);

    private IPlayerService playerService;
    private IPlayerSeasonService playerSeasonService;
    private IMLBClient mlbClient;

    private static final int MINOR_LEAGUER_AB_THRESHOLD = 150;
    private static final double MINOR_LEAGUE_IP_THRESHOLD = 50;

    public FullPlayerService(IPlayerService playerService,
                             IPlayerSeasonService playerSeasonService,
                             IMLBClient mlbClient) {
        this.playerService = playerService;
        this.playerSeasonService = playerSeasonService;
        this.mlbClient = mlbClient;
    }

    @Override
    public PlayerView createPlayer(Player player, boolean isMinorLeaguer) {
        Player createdPlayer = playerService.createPlayer(player);
        PlayerSeason playerSeason = playerSeasonService.createPlayerSeason(createdPlayer.getId(), LeagueUtil.SEASON, isMinorLeaguer);
        PlayerView view = PlayerView.from(createdPlayer);
        PlayerSeasonView playerSeasonView = PlayerSeasonView.from(playerSeason);
        List<PlayerSeasonView> playerSeasons = Lists.newArrayList(playerSeasonView);
        view.setPlayerSeasons(playerSeasons);
        return view;
    }

    @Override
    public List<PlayerView> updateMinorLeaguerStatusForPlayers() {
        logger.info("BEGIN: updateMinorLeaguerStatusForPlayers");
        List<PlayerSeason> playersWithMinorLeaguerStatus = $.of(playerSeasonService.getActivePlayers()).filterToList(PlayerSeason::getIsMinorLeaguer);
        logger.info("Found " + playersWithMinorLeaguerStatus.size() + " minor leaguers");
        List<Player> players = playerService.getByIds($.of(playersWithMinorLeaguerStatus).toList(PlayerSeason::getPlayerId));
        players = $.of(players).filterToList(player -> player.getMlbPlayerId() != null);
        logger.info("Found " + playersWithMinorLeaguerStatus.size() + " minor leaguers with non-null MLB player ids");

        List<PlayerView> updatedPlayers = Lists.newArrayList();
        for (Player player : players) {
            try {
                logger.info(player.getName() + ": retrieving stats");
                boolean isBatter = player.isBatter();
                PlayerView playerView;
                if (isBatter) {
                    playerView = getStatsAndUpdateMinorLeaguerStatus(player,
                            (playerId) -> (Stats<HittingStats>) mlbClient.getStats(playerId, isBatter), stats -> {
                                logger.info(player.getName() + ": ab- " + stats.getSeasonStats().getAtBats());
                                return stats.getSeasonStats().getAtBats() >= MINOR_LEAGUER_AB_THRESHOLD;
                            });
                } else {
                    playerView = getStatsAndUpdateMinorLeaguerStatus(player,
                            (playerId) -> (Stats<PitchingStats>) mlbClient.getStats(playerId, isBatter), stats -> {
                                logger.info(player.getName() + ": ip- " + stats.getSeasonStats().getInningsPitched());
                                return stats.getSeasonStats().getInningsPitched() >= MINOR_LEAGUE_IP_THRESHOLD;
                            });
                }
                if (playerView != null) {
                    updatedPlayers.add(playerView);
                }
            } catch (Exception e) {
                logger.error("Error getting stats and updating minor league status for " + player.getName(), e);
            }
        }
        logger.info("END: updateMinorLeaguerStatusForPlayers");
        return updatedPlayers;
    }

    @Nullable
    private <T extends BaseStats> PlayerView getStatsAndUpdateMinorLeaguerStatus(Player player, Function<Long, Stats<T>> statsFunction,
                                                                                Function<Stats<T>, Boolean> shouldUpdateFunction) {
        Stats<T> stats = statsFunction.apply(player.getMlbPlayerId());
        if (stats.getSeasonStats() == null) {
            logger.info(player.getName() + ": had no season stats");
            return null;
        }
        boolean shouldUpdate = shouldUpdateFunction.apply(stats);
        if (shouldUpdate) {
            playerSeasonService.updateMinorLeaguerStatus(player.getId(), false);
            PlayerSeason updatedPlayerSeason = playerSeasonService.updateHasRookieStatus(player.getId(), false);
            PlayerView playerView = PlayerView.from(player);
            playerView.setCurrentSeason(PlayerSeasonView.from(updatedPlayerSeason));
            logger.info(player.getName() + ": updated isMinorLeaguer=false, hasRookieStatus=false");
            return playerView;
        }
        return null;
    }
}
