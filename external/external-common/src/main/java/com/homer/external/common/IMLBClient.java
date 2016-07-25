package com.homer.external.common;

import com.homer.external.common.mlb.*;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 4/17/16.
 */
public interface IMLBClient {

    @Nullable
    MLBPlayer getPlayer(long playerId);

    Stats getStats(long playerId, boolean isBatter);

    List<MLBPlayer> get40ManRoster(long teamId);
//
//    List<Game> getSchedule(LocalDate date);
}

