package com.homer.service;

import com.google.common.collect.Maps;
import com.homer.type.Player;
import com.homer.type.PlayerSeason;
import com.homer.type.Position;
import com.homer.type.Team;
import com.homer.type.view.PlayerView;
import com.homer.util.EnumUtil;
import com.homer.util.data.Connector;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by arigolub on 2/15/16.
 */
public class PlayerService {

    private TeamService teamService;

    public PlayerService() {
        teamService = new TeamService();
    }

    public List<Player> getPlayersByName(String name) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("name", name);
        return getPlayers(map);
    }

    public List<PlayerView> hydratePlayers(List<PlayerSeason> playerSeasons) {
        List<Team> teams = teamService.getTeams();
        Map<Long, Team> teamMap =
                teams.stream().collect(Collectors.toMap(t -> t.getId(), t -> t));
        List<Long> playerIds = playerSeasons.stream().map(PlayerSeason::getPlayerId).collect(Collectors.toList());
        Map map = Maps.newHashMap();
        map.put("id", playerIds);
        List<Player> players = getPlayers(map);
        Map<Long, Player> playerMap = players.stream().collect(Collectors.toMap(p -> p.getId(), p -> p));
        List<PlayerView> pvs = playerSeasons.stream().map(p -> {
            PlayerView pv = new PlayerView();
            pv.setPlayerSeason(p);
            pv.setPlayer(playerMap.get(p.getPlayerId()));
            pv.setFantasyPosition(p.getFantasyPosition());
            pv.setFantasyTeam(teamMap.get(p.getTeamId()));
            return pv;
        }).collect(Collectors.toList());
        return pvs;
    }

    public List<Player> getPlayers() {
        return getPlayers(Maps.newHashMap());
    }

    public List<Player> getPlayersById(List<Long> ids) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", ids);
        return getPlayers(map);
    }

    public List<PlayerView> getPlayersByTeam(long teamId) {
        return getPlayersByTeam(teamId, null);
    }

    public List<PlayerView> getPlayersByTeam(long teamId, Integer season) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("teamId", teamId);
        if(season == null) {
            season = 2016;
        }
        map.put("season", season);
        List<PlayerSeason> playerSeasons = Connector.get(PlayerSeason.class, map);
        return hydratePlayers(playerSeasons);
    }

    private List<Player> getPlayers(Map<String, Object> filters) {
        return Connector.get(Player.class, filters);
    }

}
