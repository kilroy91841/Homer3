package com.homer.data.common;

import com.homer.type.Standing;
import com.homer.type.history.HistoryStanding;
import com.homer.util.core.data.IRepository;
import com.homer.util.core.data.IVersionedRepository;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 7/30/16.
 */
public interface IStandingRepository extends IVersionedRepository<Standing, HistoryStanding> {

    @Nullable
    Standing getByKey(long teamId, DateTime date);

    List<Standing> getByDate(DateTime date);
}
