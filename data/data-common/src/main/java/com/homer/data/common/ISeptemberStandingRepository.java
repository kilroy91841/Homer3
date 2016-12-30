package com.homer.data.common;

import com.homer.type.SeptemberStanding;
import com.homer.type.history.HistorySeptemberStanding;
import com.homer.util.core.data.IVersionedRepository;

import java.util.List;

/**
 * Created by arigolub on 11/13/16.
 */
public interface ISeptemberStandingRepository extends IVersionedRepository<SeptemberStanding, HistorySeptemberStanding> {
    List<SeptemberStanding> getBySeason(int season);
}
