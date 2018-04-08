package com.homer.service;

import com.google.common.collect.Maps;
import com.homer.data.common.IDraftDollarRepository;
import com.homer.exception.ObjectNotFoundException;
import com.homer.type.DraftDollar;
import com.homer.type.DraftDollarType;
import com.homer.type.history.HistoryDraftDollar;
import com.homer.util.LeagueUtil;
import com.homer.util.core.Tuple;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 3/17/16.
 */
public class DraftDollarService extends BaseVersionedIdService<DraftDollar, HistoryDraftDollar> implements IDraftDollarService {

    private IDraftDollarRepository repo;

    public static final int MLB_DRAFT_DOLLAR_MIN = 220;
    public static final int MLB_DRAFT_DOLLAR_MAX = 325;

    public DraftDollarService(IDraftDollarRepository repo) {
        super(repo);
        this.repo = repo;
    }

    @Override
    public List<DraftDollar> getDraftDollars() {
        return repo.getAll();
    }

    @Override
    public List<DraftDollar> getDraftDollarsBySeason(int season) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("season", season);
        return repo.getMany(filters);
    }

    @Override
    public List<DraftDollar> getDraftDollarsByTeams(Collection<Long> teamIds) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("teamId", teamIds);
        filters.put("expired", 0);
        return repo.getMany(filters);
    }

    @Override
    public Tuple<DraftDollar> transferMoney(long fromTeamId, long toTeamId, int season, DraftDollarType draftDollarType, int amount) {
        DraftDollar fromDollar = getDraftDollar(fromTeamId, draftDollarType, season);
        DraftDollar toDollar = getDraftDollar(toTeamId, draftDollarType, season);
        if (fromDollar == null || toDollar == null) {
            throw new ObjectNotFoundException("Could not find from or to dollars");
        }
        if (fromDollar.getAmount() < amount) {
            throw new IllegalArgumentException(String.format("Attempt to transfer %s dollar(s) from teamId %s failed due" +
                    " to insufficient funds (available funds: %s)", amount, fromTeamId, fromDollar.getAmount()));
        }
        fromDollar.setAmount(fromDollar.getAmount() - amount);
        toDollar.setAmount(toDollar.getAmount() + amount);

        if (DraftDollarType.MLBAUCTION.equals(draftDollarType)) {
            if (MLB_DRAFT_DOLLAR_MIN > fromDollar.getAmount() && season != LeagueUtil.SEASON) {
                throw new IllegalArgumentException(String.format("Attempt to transfer %s dollar(s) from teamId %s failed due" +
                " to going below dollar floor (new value: %s)", amount, fromTeamId, fromDollar.getAmount()));
            }
            if (MLB_DRAFT_DOLLAR_MAX < toDollar.getAmount()) {
                throw new IllegalArgumentException(String.format("Attempt to transfer %s dollar(s) to teamId %s failed due" +
                        " to going above dollar limit (new value: %s)", amount, toTeamId, toDollar.getAmount()));
            }
        }

        return new Tuple<>(fromDollar, toDollar);
    }

    @Override
    public List<HistoryDraftDollar> getHistories(long id) {
        return repo.getHistories(id);
    }

    @Nullable
    private DraftDollar getDraftDollar(long teamId, DraftDollarType draftDollarType, int season) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("teamId", teamId);
        filters.put("season", season);
        filters.put("draftDollarType", draftDollarType);
        return repo.get(filters);
    }
}
