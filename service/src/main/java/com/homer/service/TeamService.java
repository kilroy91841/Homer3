package com.homer.service;

import com.google.common.collect.Maps;
import com.homer.type.Team;
import com.homer.type.view.PlayerView;
import com.homer.type.view.TeamView;
import com.homer.util.data.Connector;
import com.homer.util.data.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by arigolub on 3/6/16.
 */
public class TeamService {

    private PlayerService playerService;

    public TeamService() {
        this.playerService = new PlayerService();
    }

    public TeamView getTeamById(long teamId) {
        TeamView teamView = new TeamView();
        List<PlayerView> allPlayers = playerService.getPlayersByTeam(teamId);
        teamView.setTeam(this.getTeamByIdImpl(teamId));
        teamView.setMajorLeaguers(allPlayers.stream().filter(p -> !p.getCurrentSeason().isMinorLeaguer()).collect(Collectors.toList()));
        teamView.setMinorLeaguers(allPlayers.stream().filter(p -> p.getCurrentSeason().isMinorLeaguer()).collect(Collectors.toList()));
        teamView.setSalary(
                teamView.getMajorLeaguers()
                        .stream()
                        .map(pv -> pv.getCurrentSeason().getSalary())
                        .reduce(Integer::sum)
                        .get()
        );
        return teamView;
    }

    public static List<Team> getTeams() {
        return getTeams(Maps.newHashMap());
    }

    private Team getTeamByIdImpl(long teamId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", teamId);
        return getTeams(map).stream().findFirst().orElse(null);
    }

    private static List<Team> getTeams(Map<String, Object> filters) {
        return Repository.get(Team.class, filters);
    }
}
