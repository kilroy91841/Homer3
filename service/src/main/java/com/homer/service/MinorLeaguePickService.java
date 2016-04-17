package com.homer.service;

import com.google.common.collect.Maps;
import com.homer.data.common.IMinorLeaguePickRepository;
import com.homer.exception.ObjectNotFoundException;
import com.homer.type.MinorLeaguePick;

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
    public List<MinorLeaguePick> getMinorLeaguePicksByTeams(Collection<Long> teamIds) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("owningTeamId", teamIds);
        List<MinorLeaguePick> picks = repo.getMany(filters);
        sort(picks);
        return picks;
    }

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

    @Nullable
    private MinorLeaguePick getPick(long originalTeamId, int round, int season) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("originalTeamId", originalTeamId);
        filters.put("round", round);
        filters.put("season", season);
        return repo.get(filters);
    }

    private static void sort(List<MinorLeaguePick> picks) {
        picks.sort((p1, p2) ->
                p1.getSeason() < p2.getSeason() ? -1 :
                p1.getSeason() > p2.getSeason() ? 1 :
                        p1.getRound() < p2.getRound() ? -1 : 1

        );
    }
}
