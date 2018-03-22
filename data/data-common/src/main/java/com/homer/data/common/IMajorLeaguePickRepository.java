package com.homer.data.common;

import com.homer.type.MajorLeaguePick;
import com.homer.util.core.data.IRepository;

import java.util.List;

/**
 * Created by arigolub on 2/26/17.
 */
public interface IMajorLeaguePickRepository extends IRepository<MajorLeaguePick> {
    List<MajorLeaguePick> getBySeason(int season);
}
