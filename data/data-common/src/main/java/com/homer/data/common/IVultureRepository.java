package com.homer.data.common;

import com.homer.type.Vulture;
import com.homer.type.history.HistoryVulture;
import com.homer.util.core.data.IRepository;
import com.homer.util.core.data.IVersionedRepository;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 5/4/16.
 */
public interface IVultureRepository extends IVersionedRepository<Vulture, HistoryVulture> {

    @Nullable
    Vulture getInProgressVulturesForPlayer(long playerId);
}
