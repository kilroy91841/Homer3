package com.homer.data;

import com.google.common.collect.Maps;
import com.homer.data.common.ITeamDailyRepository;
import com.homer.type.TeamDaily;
import com.homer.type.history.HistoryTeamDaily;
import com.homer.util.data.BaseVersionedRepository;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 7/29/16.
 */
public class TeamDailyRepository extends BaseVersionedRepository<TeamDaily, HistoryTeamDaily>
        implements ITeamDailyRepository {
    public TeamDailyRepository() {
        super(TeamDaily.class, HistoryTeamDaily.class);
    }

    @Nullable
    @Override
    public TeamDaily getByKey(long teamId, DateTime date) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("date", date);
        filters.put("teamId", teamId);
        return this.get(filters);
    }

    @Override
    public List<TeamDaily> getByDate(DateTime date) {
        return getMany("date", date.withMillisOfDay(0));
    }
}
