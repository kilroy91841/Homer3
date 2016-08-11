package com.homer.service;

import com.homer.type.Keeper;

import java.util.List;

/**
 * Created by arigolub on 8/14/16.
 */
public interface IKeeperService {
    List<Keeper> getKeepers(long teamId);
    List<Keeper> replaceKeepers(List<Keeper> keepers, long teamId);
}
