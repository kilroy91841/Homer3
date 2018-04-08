package com.homer.service;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.homer.data.common.IPlayerSeasonRepository;
import com.homer.type.*;
import com.homer.type.history.HistoryPlayerSeason;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

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
        if (existingPlayerSeason != null)
        {
            throw new IllegalArgumentException(String.format("PlayerSeason already exists for playerId/season combo %s/%s", keeper.getPlayerId(), keeper.getSeason()));
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

    public PlayerSeason createPlayerSeasonForNonKeeper(PlayerSeason previousPlayerSeason)
    {
        PlayerSeason existingPlayerSeason = getPlayerSeason(previousPlayerSeason.getPlayerId(), previousPlayerSeason.getSeason() + 1);
        if (existingPlayerSeason != null) {
            return existingPlayerSeason;
        }
        PlayerSeason playerSeason = new PlayerSeason();
        playerSeason.setPlayerId(previousPlayerSeason.getPlayerId());
        playerSeason.setTeamId(null);
        playerSeason.setOldTeamId(null);
        playerSeason.setKeeperTeamId(null);
        playerSeason.setFantasyPosition(null);
        playerSeason.setSeason(previousPlayerSeason.getSeason() + 1);
        playerSeason.setKeeperSeason(0);
        playerSeason.setSalary(0);
        playerSeason.setIsMinorLeaguer(previousPlayerSeason.getIsMinorLeaguer());
        playerSeason.setHasRookieStatus(previousPlayerSeason.getHasRookieStatus());
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

    private PlayerSeason getPlayerSeason(long playerId, int season) {
        return $.of(this.getPlayerSeasons(playerId)).filter(ps -> ps.getSeason() == season).first();
    }
}
