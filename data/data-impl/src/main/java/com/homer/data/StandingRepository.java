package com.homer.data;

import com.google.common.collect.Maps;
import com.homer.data.common.IStandingRepository;
import com.homer.type.PlayerDaily;
import com.homer.type.Standing;
import com.homer.type.history.HistoryStanding;
import com.homer.util.data.BaseRepository;
import com.homer.util.data.BaseVersionedRepository;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 7/30/16.
 */
public class StandingRepository extends BaseVersionedRepository<Standing, HistoryStanding> implements IStandingRepository {
    public StandingRepository() {
        super(Standing.class, HistoryStanding.class);
    }

    @Nullable
    @Override
    public Standing getByKey(long teamId, DateTime date) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("date", date.withMillisOfDay(0));
        filters.put("teamId", teamId);
        return this.get(filters);
    }

    @Override
    public List<Standing> getByDate(DateTime date) {
        return getMany("date", date.withMillisOfDay(0));
    }
}
