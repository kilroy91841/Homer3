package com.homer.service;

import com.google.common.collect.Maps;
import com.homer.type.Team;
import com.homer.util.data.Connector;

import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 3/6/16.
 */
public class TeamService {

    public Team getTeamById(long teamId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", teamId);
        return getTeams(map).stream().findFirst().orElse(null);
    }

    public List<Team> getTeams() {
        return getTeams(Maps.newHashMap());
    }

    private List<Team> getTeams(Map<String, Object> filters) {
        return Connector.get(Team.class, filters);
    }
}
