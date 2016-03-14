package com.homer.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.type.PlayerSeason;
import com.homer.type.Team;
import com.homer.util.data.Connector;
import com.homer.util.data.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by arigolub on 3/12/16.
 */
public class PlayerSeasonService {

    private static final int SEASON = 2016;

    public PlayerSeasonService() {

    }

    public List<PlayerSeason> getPlayerSeasons() {
        List<PlayerSeason> playerSeasons = getPlayerSeasonsImpl(Lists.newArrayList());
        addTeamsToPlayerSeasons(TeamService.getTeams(), playerSeasons);
        return playerSeasons;
    }

    public List<PlayerSeason> getPlayerSeasons(Collection<Long> playerIds) {
        List<PlayerSeason> playerSeasons = getPlayerSeasonsImpl(playerIds);
        addTeamsToPlayerSeasons(TeamService.getTeams(), playerSeasons);
        return playerSeasons;
    }

    public List<PlayerSeason> getPlayerSeasonsForTeam(long teamId) {
        List<PlayerSeason> playerSeasons = getPlayerSeasonsByTeamImpl(teamId);
        addTeamsToPlayerSeasons(TeamService.getTeams(), playerSeasons);
        return playerSeasons;
    }

    private List<PlayerSeason> getPlayerSeasonsByTeamImpl(long teamId, int season) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("teamId", teamId);
        map.put("season", season);
        return Repository.get(PlayerSeason.class, map);
    }

    private List<PlayerSeason> getPlayerSeasonsByTeamImpl(long teamId) {
        return getPlayerSeasonsByTeamImpl(teamId, SEASON);
    }

    private List<PlayerSeason> getPlayerSeasonsImpl(Collection<Long> playerIds) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("playerId", playerIds);
        return Repository.get(PlayerSeason.class, map);
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
