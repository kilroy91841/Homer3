package com.homer.data.common;

import com.homer.type.Vulture;
import com.homer.util.core.data.IRepository;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 5/4/16.
 */
public interface IVultureRepository extends IRepository<Vulture> {

    @Nullable
    Vulture getInProgressVulturesForPlayer(long playerId);
}
