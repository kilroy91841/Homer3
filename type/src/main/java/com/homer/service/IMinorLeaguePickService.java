package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.exception.ObjectNotFoundException;
import com.homer.type.MinorLeaguePick;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * Created by arigolub on 3/17/16.
 */
public interface IMinorLeaguePickService extends IIdService<MinorLeaguePick> {

    List<MinorLeaguePick> getMinorLeaguePicks();

    List<MinorLeaguePick> getMinorLeaguePicksBySeason(int season);

    List<MinorLeaguePick> getMinorLeaguePicksByTeams(Collection<Long> teamIds);
    default List<MinorLeaguePick> getMinorLeaguePicksByTeam(long teamId) {
        return this.getMinorLeaguePicksByTeams(Lists.newArrayList(teamId));
    }

    @Nullable
    MinorLeaguePick getMinorLeaguePickByOverallAndSeason(int overallPick, int season);

    MinorLeaguePick modifyPick(long fromTeamId, long toTeamId, long originalTeamId, int round, int season,
                                          boolean onlySwapRightTransfer);
    default MinorLeaguePick modifyPick(long fromTeamId, long toTeamId, long pickId, boolean onlySwapRightTransfer) {
        MinorLeaguePick mlp = this.getById(pickId);
        if (mlp == null) {
            throw new ObjectNotFoundException("Could not find pick by id");
        }
        return modifyPick(fromTeamId, toTeamId, mlp.getOriginalTeamId(), mlp.getRound(), mlp.getSeason(), onlySwapRightTransfer);
    }
    default MinorLeaguePick transferPick(long fromTeamId, long toTeamId, long originalTeamId, int round, int season) {
        return modifyPick(fromTeamId, toTeamId, originalTeamId, round, season, false);
    }
    default MinorLeaguePick transferPick(long fromTeamId, long toTeamId, long pickId) {
        return modifyPick(fromTeamId, toTeamId, pickId, false);
    }
    default MinorLeaguePick transferSwapRights(long fromTeamId, long toTeamId, long originalTeamId, int round, int season) {
        return modifyPick(fromTeamId, toTeamId, originalTeamId, round, season, true);
    }
    default MinorLeaguePick transferSwapRights(long fromTeamId, long toTeamId, long pickId) {
        return modifyPick(fromTeamId, toTeamId, pickId, true);
    }

    List<MinorLeaguePick> orderMinorLeaguePicksForDraft(List<Long> teamIdsInOrder, int season);
}
