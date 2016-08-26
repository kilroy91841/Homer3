package com.homer.data;

import com.homer.data.common.ITradeRepository;
import com.homer.type.Trade;
import com.homer.type.history.HistoryTrade;
import com.homer.util.data.BaseRepository;
import com.homer.util.data.BaseVersionedRepository;

/**
 * Created by arigolub on 3/18/16.
 */
public class TradeRepository extends BaseVersionedRepository<Trade, HistoryTrade> implements ITradeRepository {
    public TradeRepository() {
        super(Trade.class, HistoryTrade.class);
    }
}
