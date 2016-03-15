package com.homer.data;

import com.homer.data.common.IMinorLeaguePickRepository;
import com.homer.type.MinorLeaguePick;
import com.homer.util.data.BaseRepository;

/**
 * Created by arigolub on 3/17/16.
 */
public class MinorLeaguePickRepository extends BaseRepository<MinorLeaguePick> implements IMinorLeaguePickRepository {
    public MinorLeaguePickRepository() {
        super(MinorLeaguePick.class);
    }
}
