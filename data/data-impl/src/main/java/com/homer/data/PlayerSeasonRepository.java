package com.homer.data;

import com.homer.data.common.IPlayerSeasonRepository;
import com.homer.type.PlayerSeason;
import com.homer.type.history.HistoryPlayer;
import com.homer.type.history.HistoryPlayerSeason;
import com.homer.util.data.BaseRepository;
import com.homer.util.data.BaseVersionedRepository;

/**
 * Created by arigolub on 3/15/16.
 */
public class PlayerSeasonRepository extends BaseVersionedRepository<PlayerSeason, HistoryPlayerSeason> implements IPlayerSeasonRepository {
    public PlayerSeasonRepository() {
        super(PlayerSeason.class, HistoryPlayerSeason.class);
    }
}
