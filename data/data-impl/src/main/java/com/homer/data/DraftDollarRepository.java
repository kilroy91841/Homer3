package com.homer.data;

import com.homer.data.common.IDraftDollarRepository;
import com.homer.type.DraftDollar;
import com.homer.util.data.BaseRepository;

/**
 * Created by arigolub on 3/17/16.
 */
public class DraftDollarRepository extends BaseRepository<DraftDollar> implements IDraftDollarRepository {
    public DraftDollarRepository() {
        super(DraftDollar.class);
    }
}
