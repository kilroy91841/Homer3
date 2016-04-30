package com.homer.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.data.common.IPlayerSeasonRepository;
import com.homer.exception.ObjectNotFoundException;
import com.homer.external.common.mlb.MLBPlayerStatus;
import com.homer.type.*;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by arigolub on 3/12/16.
 */
public class PlayerSeasonService extends BaseIdService<PlayerSeason> implements IPlayerSeasonService {

    private IPlayerSeasonRepository repo;

    public PlayerSeasonService(IPlayerSeasonRepository repo) {
        super(repo);
        this.repo = repo;
    }

    @Override
    public PlayerSeason createPlayerSeason(long playerId, int season) {
        if (playerId == 0) {
            throw new IllegalArgumentException("Cannot create a player season for id 0");
        }
        PlayerSeason existingPlayerSeason = getPlayerSeason(playerId, season);
        if (existingPlayerSeason != null) {
            throw new IllegalArgumentException(String.format("PlayerSeason already exists for playerId/season combo %s/%s",
                    playerId, season));
        }
        PlayerSeason playerSeason = new PlayerSeason();
        playerSeason.setPlayerId(playerId);
        playerSeason.setSeason(season);
        playerSeason.setKeeperSeason(0);
        playerSeason.setSalary(0);
        playerSeason.setIsMinorLeaguer(false);
        playerSeason.setMlbStatus(Status.UNKNOWN);
        playerSeason.setVulturable(false);
        return super.upsert(playerSeason);
    }

    @Override
    public List<PlayerSeason> getPlayerSeasons(Collection<Long> playerIds) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("playerId", playerIds);
        return repo.getMany(filters);
    }

    @Override
    public List<PlayerSeason> getPlayerSeasonsByTeamIds(Collection<Long> teamIds, int season) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("teamId", teamIds);
        filters.put("season", season);
        return repo.getMany(filters);
    }

    @Override
    public List<PlayerSeason> getActivePlayers(int season) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("season", season);
        return repo.getMany(filters);
    }

    @Override
    public PlayerSeason switchTeam(long playerId, int season, @Nullable Long oldTeamId, @Nullable Long newTeamId) {
        if (oldTeamId == newTeamId) {
            throw new IllegalArgumentException("Old team and new team must be different");
        }
        PlayerSeason existing = getPlayerSeasonOrThrow(playerId, season);
        if (newTeamId == existing.getTeamId()) {
            return existing;
        }
        if (oldTeamId != existing.getTeamId()) {
            throw new IllegalArgumentException("Supplied old team does not match existing team");
        }
        existing.setTeamId(newTeamId);

        //Free agents should have fantasy position removed and vulturable status set to false
        if (newTeamId == null) {
            existing.setFantasyPosition(null);
            existing.setVulturable(false);
        }
        return existing;
    }

    @Override
    public PlayerSeason switchFantasyPosition(long playerId, int season, @Nullable Position oldFantasyPosition, @Nullable Position newFantasyPosition) {
        if (oldFantasyPosition == newFantasyPosition) {
            throw new IllegalArgumentException("Old position and new position must be different");
        }
        PlayerSeason existing = getPlayerSeasonOrThrow(playerId, season);
        if (newFantasyPosition == existing.getFantasyPosition()) {
            return existing;
        }
        if (oldFantasyPosition != existing.getFantasyPosition()) {
            throw new IllegalArgumentException("Supplied old position does not match existing position");
        }
        existing.setFantasyPosition(newFantasyPosition);

        //Remove minorLeaguer status if player is being added to any position other than Position.MINORLEAGUES
        if (!Position.MINORLEAGUES.equals(newFantasyPosition)) {
            existing.setIsMinorLeaguer(false);
        }

        updateVulturable(existing);

        return existing;
    }

    @Override
    public void updateVulturable(PlayerSeason playerSeason) {
        if (playerSeason.getMlbStatus() == Status.UNKNOWN) {
            return;
        }

        Position position = playerSeason.getFantasyPosition();
        if (
                (position != Position.DISABLEDLIST && playerSeason.getMlbStatus() == Status.DISABLEDLIST) ||
                        (position != Position.MINORLEAGUES && playerSeason.getMlbStatus() == Status.MINORS) ||
                        (position == Position.DISABLEDLIST && playerSeason.getMlbStatus() != Status.DISABLEDLIST) ||
                        (position == Position.MINORLEAGUES && playerSeason.getMlbStatus() != Status.MINORS && !playerSeason.getIsMinorLeaguer())
                )
        {
            playerSeason.setVulturable(true);
            return;
        }
        playerSeason.setVulturable(false);
    }

    @Override
    public List<PlayerSeason> getVulturablePlayerSeasons() {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("season", LeagueUtil.SEASON);
        filters.put("vulturable", true);
        return $.of(repo.getMany(filters)).filterToList(ps -> ps.getTeamId() != null);
    }

    private PlayerSeason getPlayerSeasonOrThrow(long playerId, int season) {
        PlayerSeason existing = getPlayerSeason(playerId, season);
        if (existing == null) {
            throw new ObjectNotFoundException(
                    String.format("Could not find PlayerSeason for player/season combo %s/%s", playerId, season));
        }
        return existing;
    }

    private PlayerSeason getPlayerSeason(long playerId, int season) {
        return $.of(this.getPlayerSeasons(playerId)).filter(ps -> ps.getSeason() == season).first();
    }
}
