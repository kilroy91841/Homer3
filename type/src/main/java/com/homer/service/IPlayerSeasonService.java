package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.type.Keeper;
import com.homer.type.PlayerSeason;
import com.homer.util.LeagueUtil;
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

    @Nullable
    default PlayerSeason getCurrentPlayerSeason(long playerId) {
        return $.of(this.getPlayerSeasons(playerId)).filter(ps -> LeagueUtil.SEASON == ps.getSeason()).first();
    }

    List<PlayerSeason> getPlayerSeasonsByTeamIds(Collection<Long> teamIds, int season);
    default List<PlayerSeason> getPlayerSeasonsByTeamId(long teamId, int season) {
        return getPlayerSeasonsByTeamIds(Lists.newArrayList(teamId), season);
    }

    List<PlayerSeason> getActivePlayers(int season);
    default List<PlayerSeason> getActivePlayers() {
        return this.getActivePlayers(LeagueUtil.SEASON);
    }

    default PlayerSeason createPlayerSeason(long playerId, int season) {
        return createPlayerSeason(playerId, season, false);
    }
    PlayerSeason createPlayerSeason(long playerId, int season, boolean isMinorLeaguer);

    PlayerSeason createPlayerSeasonForKeeper(PlayerSeason previousPlayerSeason, Keeper keeper);
    PlayerSeason createPlayerSeasonForNonKeeper(PlayerSeason previousPlayerSeason);

    //region Vulture

    List<PlayerSeason> getVulturablePlayerSeasons();

    //endregion

    // region minor leaguers

    List<PlayerSeason> getMinorLeaguers(long teamId, int season);

    // endregion
}
