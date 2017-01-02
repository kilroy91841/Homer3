package com.homer.service.eventbus;

import com.google.common.eventbus.Subscribe;
import com.homer.service.IKeeperService;
import com.homer.type.PlayerSeason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Created by arigolub on 1/2/17.
 */
public class PlayerSeasonChangeEventRecorder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerSeasonChangeEventRecorder.class);

    private IKeeperService keeperService;

    public PlayerSeasonChangeEventRecorder(IKeeperService keeperService) {
        this.keeperService = keeperService;
    }

    @Subscribe public void recordPlayerSeasonChangeEvent(PlayerSeason playerSeason) {
        LOGGER.debug("Recording player season change event for playerId:" + playerSeason.getPlayerId() + ", season:" + playerSeason.getSeason());
        if (!Objects.equals(playerSeason.getOldTeamId(), playerSeason.getTeamId())) {
            LOGGER.info("Deselecting keeper for playerId:" + playerSeason.getPlayerId() + ", season:" + playerSeason.getSeason());
            keeperService.deselectKeeper(playerSeason.getPlayerId());
        }
    }
}
