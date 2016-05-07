package com.homer.service;

import com.google.common.collect.Maps;
import com.homer.data.common.IVultureRepository;
import com.homer.type.Vulture;
import com.homer.type.VultureStatus;
import com.homer.util.core.data.IRepository;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Created by arigolub on 5/5/16.
 */
public class VultureService extends BaseIdService<Vulture> implements IVultureService {

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
            filters.put("vultureStatus", VultureStatus.IN_PROGRESS);
        }
        return repo.get(filters);
    }
}
