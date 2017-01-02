package com.homer.data.common;

import com.homer.type.Keeper;
import com.homer.type.history.HistoryKeeper;
import com.homer.util.core.data.IVersionedRepository;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 8/14/16.
 */
public interface IKeeperRepository extends IVersionedRepository<Keeper, HistoryKeeper> {

    List<Keeper> getForTeam(long teamId, int season);
    @Nullable
    Keeper getByPlayerId(long playerId, int season);
}
