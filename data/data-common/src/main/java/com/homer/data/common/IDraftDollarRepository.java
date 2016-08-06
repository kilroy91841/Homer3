package com.homer.data.common;

import com.homer.type.DraftDollar;
import com.homer.type.history.HistoryDraftDollar;
import com.homer.util.core.data.IRepository;
import com.homer.util.core.data.IVersionedRepository;

/**
 * Created by arigolub on 3/17/16.
 */
public interface IDraftDollarRepository extends IVersionedRepository<DraftDollar, HistoryDraftDollar> {
}
