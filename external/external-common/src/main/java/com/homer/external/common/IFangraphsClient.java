package com.homer.external.common;

import com.homer.external.common.mlb.Stats;

/**
 * Created by arigolub on 5/2/18.
 */
public interface IFangraphsClient {
    Stats getProjections(long playerId, boolean isBatter);
}
