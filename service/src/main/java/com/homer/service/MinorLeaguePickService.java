package com.homer.service;

import com.google.common.collect.Maps;
import com.homer.data.common.IMinorLeaguePickRepository;
import com.homer.exception.ObjectNotFoundException;
import com.homer.type.MinorLeaguePick;
import com.homer.util.core.$;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 3/17/16.
 */
public class MinorLeaguePickService extends BaseIdService<MinorLeaguePick> implements IMinorLeaguePickService {

    private IMinorLeaguePickRepository repo;

    public MinorLeaguePickService(IMinorLeaguePickRepository repo) {
        super(repo);
        this.repo = repo;
    }

    // region get

    @Override
    public List<MinorLeaguePick> getMinorLeaguePicks() {
        List<MinorLeaguePick> picks = repo.getAll();
        sort(picks);
        return picks;
    }

    @Override
    public List<MinorLeaguePick> getMinorLeaguePicksBySeason(int season) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("season", season);
        List<MinorLeaguePick> picks = repo.getMany(filters);
        sort(picks);
        return picks;
    }

    @Override
    public List<MinorLeaguePick> getMinorLeaguePicksByTeams(Collection<Long> teamIds, boolean includeUsedPicks) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("owningTeamId", teamIds);
        List<MinorLeaguePick> picks = $.of(repo.getMany(filters))
                .filterToList(mlp ->
                        includeUsedPicks ||
                                (mlp.getPlayerId() == null &&
                                (mlp.getIsSkipped() == null || Boolean.FALSE.equals(mlp.getIsSkipped()))));
        sort(picks);
        return picks;
    }

    @Nullable
    @Override
    public MinorLeaguePick getMinorLeaguePickByOverallAndSeason(int overallPick, int season) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("season", season);
        filters.put("overallPick", overallPick);
        return repo.get(filters);
    }

    // endregion

    // region update

    @Override
    public MinorLeaguePick modifyPick(long fromTeamId, long toTeamId, long originalTeamId, int round, int season,
                                      boolean onlySwapRightTransfer) {
        MinorLeaguePick pickToTransfer = getPick(originalTeamId, round, season);
        if (pickToTransfer == null) {
            throw new ObjectNotFoundException("Could not find minor league pick to transfer");
        }
        if (onlySwapRightTransfer) {
            if (pickToTransfer.getSwapTeamId() != null && pickToTransfer.getSwapTeamId() != fromTeamId) {
                throw new IllegalArgumentException("Illegal attempt to transfer swap rights for minor league pick from team that does " +
                        "not have right to do so");
            }
            pickToTransfer.setSwapTeamId(toTeamId);
        } else {
            if (pickToTransfer.getOwningTeamId() != fromTeamId) {
                throw new IllegalArgumentException("Illegal attempt to transfer minor league pick from team that does not " +
                        "presently own pick");
            }
            pickToTransfer.setOwningTeamId(toTeamId);
        }
        return pickToTransfer;
    }

    // endregion

    // region draft setup

    @Override
    public List<MinorLeaguePick> orderMinorLeaguePicksForDraft(List<Long> teamIdsInOrder, int season) {
        List<MinorLeaguePick> minorLeaguePicks = getMinorLeaguePicksBySeason(season);
        Map<Long, List<MinorLeaguePick>> minorLeaguePicksByTeam = $.of(minorLeaguePicks).groupBy(MinorLeaguePick::getOriginalTeamId);
        for (List<MinorLeaguePick> pickList : minorLeaguePicksByTeam.values()) {
            sort(pickList);
        }

        boolean isReverse = false;
        int overallPick = 1;
        while ($.of(minorLeaguePicksByTeam.values()).first().size() > 0) {
            if (isReverse) {
                for (int i = teamIdsInOrder.size() - 1; i >= 0; i--) {
                    Long teamId = teamIdsInOrder.get(i);
                    MinorLeaguePick pick = minorLeaguePicksByTeam.get(teamId).remove(0);
                    pick.setOverallPick(overallPick++);
                }
            } else {
                for (int i = 0; i < teamIdsInOrder.size(); i++) {
                    Long teamId = teamIdsInOrder.get(i);
                    MinorLeaguePick pick = minorLeaguePicksByTeam.get(teamId).remove(0);
                    pick.setOverallPick(overallPick++);
                }
            }
            isReverse = !isReverse;
        }
        for (MinorLeaguePick pick : minorLeaguePicks) {
            repo.upsert(pick);
        }
        minorLeaguePicks.sort((p1, p2) -> p1.getOverallPick() < p2.getOverallPick() ? -1 : 1);
        return minorLeaguePicks;
    }

    @Nullable
    private MinorLeaguePick getPick(long originalTeamId, int round, int season) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("originalTeamId", originalTeamId);
        filters.put("round", round);
        filters.put("season", season);
        return repo.get(filters);
    }

    private static void sort(List<MinorLeaguePick> picks) {
        if ($.of(picks).allMatch(mlp -> mlp.getOverallPick() != null)) {
            picks.sort((p1, p2) -> p1.getOverallPick() < p2.getOverallPick() ? -1 : 1);
        } else {
            picks.sort((p1, p2) ->
                    p1.getSeason() < p2.getSeason() ? -1 :
                            p1.getSeason() > p2.getSeason() ? 1 :
                                    p1.getRound() < p2.getRound() ? -1 : 1

            );
        }
    }
}
