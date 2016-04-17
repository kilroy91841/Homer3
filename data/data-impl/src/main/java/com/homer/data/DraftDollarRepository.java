package com.homer.data;

import com.homer.data.common.IDraftDollarRepository;
import com.homer.type.DraftDollar;
import com.homer.type.history.HistoryDraftDollar;
import com.homer.util.data.BaseRepository;
import com.homer.util.data.BaseVersionedRepository;

/**
 * Created by arigolub on 3/17/16.
 */
public class DraftDollarRepository extends BaseVersionedRepository<DraftDollar, HistoryDraftDollar> implements IDraftDollarRepository {
    public DraftDollarRepository() {
        super(DraftDollar.class, HistoryDraftDollar.class);
    }
}
