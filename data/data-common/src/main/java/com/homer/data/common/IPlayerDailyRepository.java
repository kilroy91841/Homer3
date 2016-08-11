package com.homer.data.common;

import com.homer.type.PlayerDaily;
import com.homer.type.history.HistoryPlayerDaily;
import com.homer.util.core.data.IRepository;
import com.homer.util.core.data.IVersionedRepository;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 7/29/16.
 */
public interface IPlayerDailyRepository extends IVersionedRepository<PlayerDaily, HistoryPlayerDaily> {

    @Nullable
    PlayerDaily getByKey(long playerId, DateTime date);

    List<PlayerDaily> getByDate(int teamId, DateTime date);

    List<PlayerDaily> getByTeam(long teamId);
}
