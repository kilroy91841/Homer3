package com.homer.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.data.common.IPlayerRepository;
import com.homer.type.Player;
import com.homer.type.Position;
import com.homer.util.data.Matcher;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Nullable
    @Override
    public Player getPlayerByMLBPlayerId(long mlbPlayerId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("mlbPlayerId", mlbPlayerId);
        return repo.get(map);
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
                player.getLastName() == null || player.getLastName().isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Player was missing first name or last name, supplied %s and %s", player.getFirstName(),
                            player.getLastName()));
        }
        if (player.getMlbTeamId() == 0) {
            throw new IllegalArgumentException("Player was missing mlb team id");
        }
        if (player.getPosition() == null) {
            throw new IllegalArgumentException("Player was missing position");
        }

        player.setName(player.getFirstName() + " " + player.getLastName());
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
                Objects.equals(player.getPosition(), existingPlayer.getPosition())) {
            return existingPlayer;
        }

        existingPlayer.setFirstName(player.getFirstName());
        existingPlayer.setLastName(player.getLastName());
        existingPlayer.setName(player.getFirstName() + " " + player.getLastName());
        existingPlayer.setMlbPlayerId(player.getMlbPlayerId());
        existingPlayer.setPosition(player.getPosition());

        return super.upsert(existingPlayer);
    }
}
