package com.homer.service;

import com.homer.type.Vulture;
import com.homer.type.VultureStatus;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 5/4/16.
 */
public interface IVultureService extends IIdService<Vulture> {

    @Nullable
    Vulture getForPlayer(long playerId, boolean onlyInProgress);
}
