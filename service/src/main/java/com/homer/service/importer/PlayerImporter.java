package com.homer.service.importer;

import com.google.common.collect.Lists;
import com.homer.external.common.IMLBClient;
import com.homer.external.common.mlb.MLBPlayer;
import com.homer.external.common.mlb.MLBPlayerStatus;
import com.homer.service.IPlayerSeasonService;
import com.homer.service.IPlayerService;
import com.homer.type.Player;
import com.homer.type.PlayerSeason;
import com.homer.type.Status;
import com.homer.type.view.PlayerSeasonView;
import com.homer.type.view.PlayerView;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 4/20/16.
 */
//TODO Test this class
public class PlayerImporter implements IPlayerImporter {

    final static Logger logger = LoggerFactory.getLogger(PlayerImporter.class);

    private IPlayerService playerService;
    private IPlayerSeasonService playerSeasonService;
    private IMLBClient mlbClient;

    public PlayerImporter(IPlayerService playerService, IPlayerSeasonService playerSeasonService, IMLBClient mlbClient) {
        this.playerService = playerService;
        this.playerSeasonService = playerSeasonService;
        this.mlbClient = mlbClient;
    }

    @Override
    public List<PlayerView> update40ManRoster(long teamId) {
        List<MLBPlayer> mlbPlayers = mlbClient.get40ManRoster(teamId);
        List<String> playerNames = $.of(mlbPlayers).toList(MLBPlayer::getName);
        List<Player> players = playerService.getPlayersByNames(playerNames);
        Map<String, Player> nameToPlayerMap = $.of(players).toMap(Player::getName);

        List<PlayerView> playerViews = Lists.newArrayList();
        for(MLBPlayer mlbPlayer : mlbPlayers) {
            try {
                Player player = nameToPlayerMap.get(mlbPlayer.getName());
                if (player != null) {
                    playerViews.add(updatePlayerImpl(player, mlbPlayer));
                    logger.info("Updated " + player.getName());
                } else {
                    logger.info(mlbPlayer.getName() + " not in database");
                }
            } catch (Exception e) {
                logger.error("Error processing " + mlbPlayer.getName(), e);
            }
        }
        return playerViews;
    }

    @Override
    public PlayerView updatePlayer(Player player) {
        if (player.getMlbPlayerId() == null) {
            throw new IllegalArgumentException(String.format("Cannot update player %s, no mlb player id present", player.getId()));
        }

        MLBPlayer mlbPlayer = mlbClient.getPlayer(player.getMlbPlayerId());
        if (mlbPlayer == null) {
            throw new IllegalArgumentException(String.format("Could not find mlb player id %s for player id %s", player.getMlbPlayerId(),
                    player.getId()));
        }

        return updatePlayerImpl(player, mlbPlayer);
    }

    private PlayerView updatePlayerImpl(Player player, MLBPlayer mlbPlayer) {
        List<PlayerSeason> playerSeasons = playerSeasonService.getPlayerSeasons(player.getId());
        PlayerSeason currentSeason = $.of(playerSeasons).first(ps -> ps.getSeason() == LeagueUtil.SEASON);
        if (currentSeason == null) {
            throw new IllegalArgumentException(String.format("Could not find player season for mlb player id %s and season %s",
                    player.getId(), LeagueUtil.SEASON));
        }

        if (player.getMlbPlayerId() == null) {
            player.setMlbPlayerId(mlbPlayer.getId());
            playerService.upsert(player);
        }

        Status newStatus = fromStatus(mlbPlayer.getMlbPlayerStatus());
        if (newStatus != currentSeason.getMlbStatus()) {
            currentSeason.setMlbStatus(newStatus);
            playerSeasonService.updateVulturable(currentSeason);
            playerSeasonService.upsert(currentSeason);
        }

        PlayerView pv = PlayerView.from(player);
        pv.setPlayerSeasons($.of(playerSeasons).toList(ps -> PlayerSeasonView.from((PlayerSeason)ps)));
        pv.setCurrentSeason(PlayerSeasonView.from(currentSeason));
        return pv;
    }

    private static Status fromStatus(@Nullable MLBPlayerStatus status) {
        if (status == null) {
            return Status.UNKNOWN;
        }
        switch (status) {
            case ACTIVE:
                return Status.ACTIVE;
            case DISABLEDLIST:
                return Status.DISABLEDLIST;
            case MINORS:
                return Status.MINORS;
            case FREEAGENT:
                return Status.FREEAGENT;
            case UNKNOWN:
                return Status.UNKNOWN;
            default:
                return Status.UNKNOWN;
        }
    }
}
