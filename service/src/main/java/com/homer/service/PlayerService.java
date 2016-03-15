package com.homer.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.data.common.IPlayerRepository;
import com.homer.type.Player;
import com.homer.util.data.Matcher;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 2/15/16.
 */
public class PlayerService extends BaseIdService<Player> implements IPlayerService {

    private IPlayerRepository repo;

    public PlayerService(IPlayerRepository repo) {
        super(repo);
        this.repo = repo;
    }

    @Override
    public List<Player> getPlayers() {
        return repo.getMany(Maps.newHashMap());
    }

    @Override
    public List<Player> getPlayersByNames(Collection<String> names) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("name", names);
        return repo.getMany(map);
    }

    @Override
    public List<Player> searchPlayersByName(String name) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("nameMatcher", new Matcher("name", name));
        return repo.getMany(map);
    }
}
