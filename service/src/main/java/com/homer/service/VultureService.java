package com.homer.service;

import com.google.common.collect.Maps;
import com.homer.data.common.IVultureRepository;
import com.homer.type.Vulture;
import com.homer.type.EventStatus;
import com.homer.type.history.HistoryVulture;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 5/5/16.
 */
//TODO Add season filter
public class VultureService extends BaseVersionedIdService<Vulture, HistoryVulture> implements IVultureService {

    private IVultureRepository repo;

    public VultureService(IVultureRepository repo) {
        super(repo);
        this.repo = repo;
    }

    @Nullable
    @Override
    public Vulture getForPlayer(long playerId, boolean onlyInProgress) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("playerId", playerId);
        if (onlyInProgress) {
            filters.put("vultureStatus", EventStatus.IN_PROGRESS);
        }
        return repo.get(filters);
    }

    @Override
    public List<Vulture> getInProgressVultures() {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("vultureStatus", EventStatus.IN_PROGRESS);
        return repo.getMany(filters);
    }
}
