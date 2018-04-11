package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.type.Player;
import com.homer.util.core.$;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * Created by arigolub on 3/15/16.
 */
public interface IPlayerService extends IIdService<Player> {

    List<Player> getPlayers();

    List<Player> getPlayersByNames(Collection<String> names);
    List<Player> getPlayersByEspnNames(Collection<String> names);
    @Nullable
    default Player getPlayerByName(String name) {
        return $.of(getPlayersByNames(Lists.newArrayList(name))).first();
    }

    List<Player> getPlayersByEspnPlayerIds(Collection<Integer> espnPlayerIds);

    List<Player> getPlayersByMLBPlayerIds(Collection<Long> mlbPlayerIds);
    @Nullable
    default Player getPlayerByMLBPlayerId(long mlbPlayerId)
    {
        return $.of(getPlayersByMLBPlayerIds(Lists.newArrayList(mlbPlayerId))).first();
    }

    List<Player> searchPlayersByName(String search);

    Player createPlayer(Player player);

    Player updatePlayer(Player player);
}
