package com.homer.data;

import com.homer.data.common.ISeptemberStandingRepository;
import com.homer.type.SeptemberStanding;
import com.homer.type.history.HistorySeptemberStanding;
import com.homer.util.data.BaseVersionedRepository;

import java.util.List;

/**
 * Created by arigolub on 11/13/16.
 */
public class SeptemberStandingRepository extends BaseVersionedRepository<SeptemberStanding, HistorySeptemberStanding>
        implements ISeptemberStandingRepository{

    public SeptemberStandingRepository() {
        super(SeptemberStanding.class, HistorySeptemberStanding.class);
    }

    @Override
    public List<SeptemberStanding> getBySeason(int season) {
        return getMany("season", season);
    }
}
