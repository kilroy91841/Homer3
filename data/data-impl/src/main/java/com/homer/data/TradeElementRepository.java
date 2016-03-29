package com.homer.data;

import com.homer.data.common.ITradeElementRepository;
import com.homer.type.TradeElement;
import com.homer.util.data.BaseRepository;

/**
 * Created by arigolub on 3/18/16.
 */
public class TradeElementRepository extends BaseRepository<TradeElement> implements ITradeElementRepository {
    public TradeElementRepository() {
        super(TradeElement.class);
    }
}
