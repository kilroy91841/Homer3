package com.homer.service;

import com.homer.type.Keeper;
import com.homer.type.PlayerSeason;

import java.util.List;

/**
 * Created by arigolub on 8/14/16.
 */
public interface IKeeperService {
    List<Keeper> getKeepers(long teamId);
    List<Keeper> replaceKeepers(List<Keeper> keepers, long teamId);
    void deselectKeeper(long playerId);
    List<PlayerSeason> finalizeKeepers(long teamId);
}
