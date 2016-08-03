package com.homer.data;

import com.google.common.collect.Maps;
import com.homer.data.common.IPlayerDailyRepository;
import com.homer.type.PlayerDaily;
import com.homer.type.history.HistoryPlayerDaily;
import com.homer.util.data.BaseVersionedRepository;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 7/29/16.
 */
public class PlayerDailyRepository extends BaseVersionedRepository<PlayerDaily, HistoryPlayerDaily>
        implements IPlayerDailyRepository {
    public PlayerDailyRepository() {
        super(PlayerDaily.class, HistoryPlayerDaily.class);
    }

    @Nullable
    @Override
    public PlayerDaily getByKey(long playerId, DateTime date) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("date", date);
        filters.put("playerId", playerId);
        return this.get(filters);
    }

    @Override
    public List<PlayerDaily> getByDate(int teamId, DateTime date) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("date", date.withMillisOfDay(0));
        filters.put("teamId", teamId);
        return getMany(filters);
    }
}
