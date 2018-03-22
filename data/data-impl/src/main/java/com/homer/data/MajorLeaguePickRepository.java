package com.homer.data;

import com.homer.data.common.IMajorLeaguePickRepository;
import com.homer.type.MajorLeaguePick;
import com.homer.util.data.BaseRepository;

import java.util.List;

/**
 * Created by arigolub on 2/26/17.
 */
public class MajorLeaguePickRepository extends BaseRepository<MajorLeaguePick> implements IMajorLeaguePickRepository {
    public MajorLeaguePickRepository() {
        super(MajorLeaguePick.class);
    }

    @Override
    public List<MajorLeaguePick> getBySeason(int season) {
        return getMany("season", season);
    }
}
