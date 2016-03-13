package com.homer.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.type.PlayerSeason;
import com.homer.type.Team;
import com.homer.util.data.Connector;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by arigolub on 3/12/16.
 */
public class PlayerSeasonService {

    private static final int SEASON = 2016;

    private List<Team> teams;

    public PlayerSeasonService() {
        TeamService teamService = new TeamService();
        teams = teamService.getTeams();
    }

    public List<PlayerSeason> getPlayerSeasons() {
        List<PlayerSeason> playerSeasons = getPlayerSeasonsImpl(Lists.newArrayList());
        addTeamsToPlayerSeasons(teams, playerSeasons);
        return playerSeasons;
    }

    public List<PlayerSeason> getPlayerSeasons(Collection<Long> playerIds) {
        List<PlayerSeason> playerSeasons = getPlayerSeasonsImpl(playerIds);
        addTeamsToPlayerSeasons(teams, playerSeasons);
        return playerSeasons;
    }

    public List<PlayerSeason> getPlayerSeasonsForTeam(long teamId) {
        List<PlayerSeason> playerSeasons = getPlayerSeasonsByTeamImpl(teamId);
        addTeamsToPlayerSeasons(teams, playerSeasons);
        return playerSeasons;
    }

    private List<PlayerSeason> getPlayerSeasonsByTeamImpl(long teamId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("teamId", teamId);
        map.put("season", SEASON);
        return Connector.get(PlayerSeason.class, map);
    }

    private List<PlayerSeason> getPlayerSeasonsImpl(Collection<Long> playerIds) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("playerId", playerIds);
        return Connector.get(PlayerSeason.class, map);
    }

    private static void addTeamsToPlayerSeasons(Collection<Team> teams, Collection<PlayerSeason> playerSeasons) {
        Map<Long, Team> teamMap = teams.stream().collect(Collectors.toMap(t -> t.getId(), t -> t));
        playerSeasons.forEach(ps -> {
            ps.setTeam(teamMap.get(ps.getTeamId()));
            if (ps.getKeeperTeamId() != null) {
                ps.setKeeperTeam(teamMap.get(ps.getTeamId()));
            }
        });
    }
}
