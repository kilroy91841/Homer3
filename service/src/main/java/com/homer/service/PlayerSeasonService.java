package com.homer.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.homer.data.common.IPlayerSeasonRepository;
import com.homer.exception.ObjectNotFoundException;
import com.homer.external.common.mlb.MLBPlayerStatus;
import com.homer.type.*;
import com.homer.type.history.HistoryPlayerSeason;
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
public class PlayerSeasonService extends BaseVersionedIdService<PlayerSeason, HistoryPlayerSeason> implements IPlayerSeasonService {

    private IPlayerSeasonRepository repo;
    private EventBus eventBus;

    public PlayerSeasonService(IPlayerSeasonRepository repo, EventBus eventBus) {
        super(repo);
        this.repo = repo;
        this.eventBus = eventBus;
    }

    @Override
    public PlayerSeason upsert(PlayerSeason obj) {
        PlayerSeason playerSeason = super.upsert(obj);
        eventBus.post(obj);
        return playerSeason;
    }

    @Override
    public PlayerSeason createPlayerSeason(long playerId, int season, boolean isMinorLeaguer) {
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
        playerSeason.setIsMinorLeaguer(isMinorLeaguer);
        playerSeason.setHasRookieStatus(isMinorLeaguer);
        playerSeason.setMlbStatus(Status.UNKNOWN);
        playerSeason.setVulturable(false);
        return super.upsert(playerSeason);
    }

    @Override
    public PlayerSeason createPlayerSeasonForKeeper(PlayerSeason previousPlayerSeason, Keeper keeper) {
        PlayerSeason existingPlayerSeason = getPlayerSeason(keeper.getPlayerId(), keeper.getSeason());
        if (existingPlayerSeason != null) {
            throw new IllegalArgumentException(String.format("PlayerSeason already exists for playerId/season combo %s/%s",
                    keeper.getPlayerId(), keeper.getSeason()));
        }
        PlayerSeason playerSeason = new PlayerSeason();
        playerSeason.setPlayerId(keeper.getPlayerId());
        playerSeason.setTeamId(keeper.getTeamId());
        playerSeason.setOldTeamId(keeper.getTeamId());
        playerSeason.setKeeperTeamId(keeper.getTeamId());
        playerSeason.setFantasyPosition(previousPlayerSeason.getFantasyPosition());
        playerSeason.setSeason(keeper.getSeason());
        playerSeason.setKeeperSeason(keeper.getKeeperSeason());
        playerSeason.setSalary(keeper.getSalary());
        playerSeason.setIsMinorLeaguer(keeper.getIsMinorLeaguer());
        playerSeason.setHasRookieStatus(previousPlayerSeason.getHasRookieStatus() && keeper.getIsMinorLeaguer());
        playerSeason.setMlbStatus(Status.UNKNOWN);
        playerSeason.setVulturable(false);
        return playerSeason;
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
        return switchTeam(existing, oldTeamId, newTeamId);
    }

    @Override
    public PlayerSeason switchTeam(PlayerSeason existing, @Nullable Long oldTeamId, @Nullable Long newTeamId) {
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

        return switchFantasyPosition(existing, oldFantasyPosition, newFantasyPosition);
    }

    @Override
    public PlayerSeason switchFantasyPosition(PlayerSeason existing, @Nullable Position oldFantasyPosition, @Nullable Position newFantasyPosition) {
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
        Boolean isVulturable = Vulture.isPlayerVulturable(playerSeason);
        if (isVulturable == null) {
            return;
        } else if (isVulturable) {
            playerSeason.setVulturable(true);
        } else {
            playerSeason.setVulturable(false);
        }
    }

    @Override
    public List<PlayerSeason> getVulturablePlayerSeasons() {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("season", LeagueUtil.SEASON);
        filters.put("vulturable", true);
        return $.of(repo.getMany(filters)).filterToList(ps -> ps.getTeamId() != null);
    }

    @Override
    public List<PlayerSeason> getMinorLeaguers(long teamId, int season) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("season", season);
        filters.put("teamId", teamId);
        filters.put("fantasyPosition", Position.MINORLEAGUES);
        return repo.getMany(filters);
    }

    @Override
    public PlayerSeason updateMinorLeaguerStatus(long playerId, boolean newMinorLeagueStatus) {
        PlayerSeason playerSeason = getCurrentPlayerSeason(playerId);
        playerSeason.setIsMinorLeaguer(newMinorLeagueStatus);
        updateVulturable(playerSeason);
        return repo.upsert(playerSeason);
    }

    @Override
    public PlayerSeason updateHasRookieStatus(long playerId, boolean newHasRookieStatus) {
        PlayerSeason playerSeason = getCurrentPlayerSeason(playerId);
        playerSeason.setHasRookieStatus(newHasRookieStatus);
        updateVulturable(playerSeason);
        return repo.upsert(playerSeason);
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
