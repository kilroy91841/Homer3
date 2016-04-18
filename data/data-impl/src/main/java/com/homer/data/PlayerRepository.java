package com.homer.data;

import com.google.common.collect.Maps;
import com.homer.data.common.IPlayerRepository;
import com.homer.type.Player;
import com.homer.type.history.HistoryPlayer;
import com.homer.util.data.BaseRepository;
import com.homer.util.data.BaseVersionedRepository;

import java.util.Map;

/**
 * Created by arigolub on 3/14/16.
 */
public class PlayerRepository extends BaseVersionedRepository<Player, HistoryPlayer> implements IPlayerRepository {
    public PlayerRepository() {
        super(Player.class, HistoryPlayer.class);
    }
}
