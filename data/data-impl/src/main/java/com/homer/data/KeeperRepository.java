package com.homer.data;

import com.google.common.collect.Maps;
import com.homer.data.common.IKeeperRepository;
import com.homer.type.Keeper;
import com.homer.type.history.HistoryKeeper;
import com.homer.util.data.BaseVersionedRepository;

import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 8/14/16.
 */
public class KeeperRepository extends BaseVersionedRepository<Keeper, HistoryKeeper> implements IKeeperRepository
{
    public KeeperRepository() {
        super(Keeper.class, HistoryKeeper.class);
    }

    @Override
    public List<Keeper> getForTeam(long teamId, int season) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("teamId", teamId);
        filters.put("season", season);
        return getMany("teamId", teamId);
    }
}
