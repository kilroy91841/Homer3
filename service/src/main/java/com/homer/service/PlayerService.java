package com.homer.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.type.Player;
import com.homer.type.PlayerSeason;
import com.homer.type.Position;
import com.homer.type.Team;
import com.homer.type.view.PlayerView;
import com.homer.util.EnumUtil;
import com.homer.util.data.Connector;
import com.homer.util.data.Matcher;
import com.homer.util.data.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by arigolub on 2/15/16.
 */
public class PlayerService {

    private static final int SEASON = 2016;

    private PlayerSeasonService playerSeasonService;

    public PlayerService() {
        this.playerSeasonService = new PlayerSeasonService();
    }

    public List<PlayerView> getAllPlayers() {
        return getPlayers(Lists.newArrayList());
    }

    public List<PlayerView> getPlayersById(List<Long> ids) {
        return getPlayers(ids);
    }

    public List<PlayerView> getPlayersByTeam(long teamId) {
        List<PlayerSeason> playerSeasons = playerSeasonService.getPlayerSeasonsForTeam(teamId);
        return getPlayers(playerSeasons.stream().map(PlayerSeason::getPlayerId).collect(Collectors.toList()));
    }

    public List<PlayerView> searchPlayersByName(String name) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("nameMatcher", new Matcher("name", name));

        List<Player> players = getPlayersImpl(map);
        List<PlayerSeason> playerSeasons =
                playerSeasonService.getPlayerSeasons(players.stream().map(Player::getId).collect(Collectors.toList()));

        return mergePlayersAndPlayerSeasons(players, playerSeasons);
    }

    //region helpers

    private List<PlayerView> mergePlayersAndPlayerSeasons(Collection<Player> players,
                                                          List<PlayerSeason> playerSeasons)
    {
        Map<Long, List<PlayerSeason>> playerSeasonsMap =
                playerSeasons
                        .stream()
                        .collect(Collectors.groupingBy(PlayerSeason::getPlayerId));

        List<PlayerView> results = Lists.newArrayList();
        for(Player p : players) {
            PlayerView pView = new PlayerView(p, playerSeasonsMap.get(p.getId()));
            Optional<PlayerSeason> currentSeason = pView.getPlayerSeasons().stream()
                    .filter(ps -> ps.getSeason() == SEASON).findFirst();
            if (currentSeason.isPresent()) {
                pView.setCurrentSeason(currentSeason.get());
            }
            results.add(pView);
        }
        return results;
    }

    private List<PlayerView> getPlayers(Collection<Long> playerIds) {
        List<Player> players = getPlayersByIdImpl(playerIds);
        List<PlayerSeason> playerSeasons = playerSeasonService.getPlayerSeasons(playerIds);

        return mergePlayersAndPlayerSeasons(players, playerSeasons);
    }

    private List<Player> getPlayersByIdImpl(Collection<Long> ids) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", ids);
        return getPlayersImpl(map);
    }

    private List<Player> getPlayersImpl(Map<String, Object> filters) {
        return Repository.get(Player.class, filters);
    }

    //endregion

}
