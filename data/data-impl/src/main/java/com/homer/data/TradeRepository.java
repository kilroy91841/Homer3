package com.homer.data;

import com.homer.data.common.ITradeRepository;
import com.homer.type.Trade;
import com.homer.util.data.BaseRepository;

/**
 * Created by arigolub on 3/18/16.
 */
public class TradeRepository extends BaseRepository<Trade> implements ITradeRepository {
    public TradeRepository() {
        super(Trade.class);
    }
}
