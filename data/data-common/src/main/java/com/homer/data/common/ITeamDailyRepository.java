package com.homer.data.common;

import com.homer.type.TeamDaily;
import com.homer.type.history.HistoryTeamDaily;
import com.homer.util.core.data.IRepository;
import com.homer.util.core.data.IVersionedRepository;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 7/30/16.
 */
public interface ITeamDailyRepository extends IVersionedRepository<TeamDaily, HistoryTeamDaily> {
    @Nullable
    TeamDaily getByKey(long teamId, DateTime date);

    List<TeamDaily> getByDate(DateTime date);
}
