package com.homer.service;

import com.google.common.collect.Maps;
import com.homer.data.common.IPlayerRepository;
import com.homer.type.Player;
import com.homer.type.history.HistoryPlayer;
import com.homer.util.core.data.Matcher;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by arigolub on 2/15/16.
 */
public class PlayerService extends BaseVersionedIdService<Player, HistoryPlayer> implements IPlayerService {

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
    public List<Player> getPlayersByEspnNames(Collection<String> names) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("espnName", names);
        return repo.getMany(map);
    }

    @Override
    public List<Player> getPlayersByMLBPlayerIds(Collection<Long> mlbPlayerIds) {
        return repo.getMany("mlbPlayerId", mlbPlayerIds);
    }

    @Override
    public List<Player> getPlayersByEspnPlayerIds(Collection<Integer> espnPlayerIds) {
        return repo.getMany("espnPlayerId", espnPlayerIds);
    }

    @Override
    public List<Player> searchPlayersByName(String name) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("nameMatcher", new Matcher("name", name));
        return repo.getMany(map);
    }

    @Override
    public Player createPlayer(Player player) {
        if (player.getFirstName() == null || player.getFirstName().isEmpty() ||
                player.getLastName() == null || player.getLastName().isEmpty() ||
                player.getName() == null || player.getName().isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Player was missing first name or last name or name, supplied %s and %s and %s", player.getFirstName(),
                            player.getLastName(), player.getName()));
        }
        if (player.getMlbTeamId() == 0) {
            throw new IllegalArgumentException("Player was missing mlb team id");
        }
        if (player.getPosition() == null) {
            throw new IllegalArgumentException("Player was missing position");
        }

        Player existingPlayer = getPlayerByName(player.getName());
        if (existingPlayer != null) {
            throw new IllegalArgumentException(String.format("Player with name %s already exists", existingPlayer.getName()));
        }

        return super.upsert(player);
    }

    @Override
    public Player updatePlayer(Player player) {
        Player existingPlayer = getById(player.getId());
        if (existingPlayer == null) {
            throw new IllegalArgumentException(
                    String.format("Could not find player with id %s", player.getId()));
        }

        if (Objects.equals(player.getFirstName(), existingPlayer.getFirstName()) &&
                Objects.equals(player.getLastName(), existingPlayer.getLastName()) &&
                Objects.equals(player.getMlbPlayerId(), existingPlayer.getMlbPlayerId()) &&
                Objects.equals(player.getPosition(), existingPlayer.getPosition()) &&
                Objects.equals(player.getEspnName(), existingPlayer.getEspnName())) {
            return existingPlayer;
        }

        existingPlayer.setFirstName(player.getFirstName());
        existingPlayer.setLastName(player.getLastName());
        existingPlayer.setName(player.getFirstName() + " " + player.getLastName());
        existingPlayer.setMlbPlayerId(player.getMlbPlayerId());
        existingPlayer.setPosition(player.getPosition());
        existingPlayer.setEspnName(player.getEspnName());

        return super.upsert(existingPlayer);
    }
}
