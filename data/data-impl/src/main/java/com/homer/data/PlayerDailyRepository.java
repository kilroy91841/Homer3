package com.homer.data;

import com.homer.data.common.IPlayerDailyRepository;
import com.homer.type.PlayerDaily;
import com.homer.type.history.HistoryPlayerDaily;
import com.homer.util.data.BaseVersionedRepository;

/**
 * Created by arigolub on 7/29/16.
 */
public class PlayerDailyRepository extends BaseVersionedRepository<PlayerDaily, HistoryPlayerDaily>
        implements IPlayerDailyRepository {
    public PlayerDailyRepository() {
        super(PlayerDaily.class, HistoryPlayerDaily.class);
    }
}
