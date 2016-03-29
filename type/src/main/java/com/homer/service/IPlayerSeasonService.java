package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.type.PlayerSeason;
import com.homer.type.Position;
import com.homer.util.core.$;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * Created by arigolub on 3/15/16.
 */
public interface IPlayerSeasonService extends IIdService<PlayerSeason> {

    List<PlayerSeason> getPlayerSeasons(Collection<Long> playerIds);
    default List<PlayerSeason> getPlayerSeasons(long playerId) {
        return this.getPlayerSeasons(Lists.newArrayList(playerId));
    }

    List<PlayerSeason> getPlayerSeasonsByTeamIds(Collection<Long> teamIds, int season);
    default List<PlayerSeason> getPlayerSeasonsByTeamId(long teamId, int season) {
        return getPlayerSeasonsByTeamIds(Lists.newArrayList(teamId), season);
    }

    PlayerSeason switchTeam(long playerId, int season, @Nullable Long oldTeamId, @Nullable Long newTeamId);

    PlayerSeason switchFantasyPosition(long playerId, int season, @Nullable Position oldFantasyPosition,
                                       @Nullable Position newFantasyPosition);
}
