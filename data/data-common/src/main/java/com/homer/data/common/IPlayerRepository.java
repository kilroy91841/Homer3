package com.homer.data.common;

import com.homer.type.Player;
import com.homer.type.history.HistoryPlayer;
import com.homer.util.core.data.IRepository;
import com.homer.util.core.data.IVersionedRepository;

import java.util.List;

/**
 * Created by arigolub on 3/14/16.
 */
public interface IPlayerRepository extends IVersionedRepository<Player, HistoryPlayer> {
}
