package com.homer.service.full;

import com.homer.type.Player;
import com.homer.type.view.PlayerView;

/**
 * Created by arigolub on 4/17/16.
 */
public interface IFullPlayerService {

    default PlayerView createPlayer(Player player) {
        return createPlayer(player, false);
    }
    PlayerView createPlayer(Player player, boolean isMinorLeaguer);
}
