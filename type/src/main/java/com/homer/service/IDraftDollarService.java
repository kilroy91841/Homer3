package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.exception.ObjectNotFoundException;
import com.homer.type.DraftDollar;
import com.homer.type.DraftDollarType;
import com.homer.util.core.Tuple;

import java.util.Collection;
import java.util.List;

/**
 * Created by arigolub on 3/17/16.
 */
public interface IDraftDollarService extends IIdService<DraftDollar> {

    List<DraftDollar> getDraftDollars();

    List<DraftDollar> getDraftDollarsBySeason(int season);

    List<DraftDollar> getDraftDollarsByTeams(Collection<Long> teamIds);
    default List<DraftDollar> getDraftDollarsByTeam(long teamId) {
        return this.getDraftDollarsByTeams(Lists.newArrayList(teamId));
    }

    Tuple<DraftDollar> transferMoney(long fromTeamId, long toTeamId, int season, DraftDollarType draftDollarType, int amount);
    default Tuple<DraftDollar> transferMoney(long fromTeamId, long toTeamId, long draftDollarId, int amount) {
        DraftDollar dd = getById(draftDollarId);
        if (dd == null) {
            throw new ObjectNotFoundException("Could not find draft dollars by id");
        }
        return transferMoney(fromTeamId, toTeamId, dd.getSeason(), dd.getDraftDollarType(), amount);
    }
}
