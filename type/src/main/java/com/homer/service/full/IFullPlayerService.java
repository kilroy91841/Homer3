package com.homer.service.full;

import com.homer.type.Player;
import com.homer.type.view.PlayerView;

import java.util.List;

/**
 * Created by arigolub on 4/17/16.
 */
public interface IFullPlayerService {

    default PlayerView createPlayer(Player player) {
        return createPlayer(player, false);
    }
    PlayerView createPlayer(Player player, boolean isMinorLeaguer);

    List<PlayerView> updateMinorLeaguerStatusForPlayers();
}
