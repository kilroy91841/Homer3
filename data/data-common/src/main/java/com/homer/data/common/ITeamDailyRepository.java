package com.homer.data.common;

import com.homer.type.TeamDaily;
import com.homer.util.core.data.IRepository;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 7/30/16.
 */
public interface ITeamDailyRepository extends IRepository<TeamDaily> {
    @Nullable
    TeamDaily getByKey(long teamId, DateTime date);

    List<TeamDaily> getByDate(DateTime date);
}
