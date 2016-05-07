package com.homer.data;

import com.google.common.collect.Maps;
import com.homer.data.common.IVultureRepository;
import com.homer.type.Player;
import com.homer.type.Vulture;
import com.homer.type.VultureStatus;
import com.homer.type.history.HistoryPlayer;
import com.homer.type.history.HistoryVulture;
import com.homer.util.data.BaseVersionedRepository;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Created by arigolub on 5/4/16.
 */
public class VultureRepository extends BaseVersionedRepository<Vulture, HistoryVulture> implements IVultureRepository {

    public VultureRepository() {
        super(Vulture.class, HistoryVulture.class);
    }

    @Nullable
    @Override
    public Vulture getInProgressVulturesForPlayer(long playerId) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("playerId", playerId);
        filters.put("vultureStatus", VultureStatus.IN_PROGRESS);
    }
}
