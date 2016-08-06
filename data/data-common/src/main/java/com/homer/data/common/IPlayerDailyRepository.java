package com.homer.data.common;

import com.homer.type.PlayerDaily;
import com.homer.util.core.data.IRepository;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 7/29/16.
 */
public interface IPlayerDailyRepository extends IRepository<PlayerDaily> {

    @Nullable
    PlayerDaily getByKey(long playerId, DateTime date);

    List<PlayerDaily> getByDate(int teamId, DateTime date);

    List<PlayerDaily> getByTeam(long teamId);
}
