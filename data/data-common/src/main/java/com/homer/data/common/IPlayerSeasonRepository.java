package com.homer.data.common;

import com.homer.type.PlayerSeason;
import com.homer.type.history.HistoryPlayerSeason;
import com.homer.util.core.data.IRepository;
import com.homer.util.core.data.IVersionedRepository;

/**
 * Created by arigolub on 3/15/16.
 */
public interface IPlayerSeasonRepository extends IVersionedRepository<PlayerSeason, HistoryPlayerSeason> {
}
