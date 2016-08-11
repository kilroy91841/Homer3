package com.homer.service.full;

import com.homer.type.Trade;

import java.util.Collection;
import java.util.List;

/**
 * Created by arigolub on 3/20/16.
 */
public interface IFullTradeService {

    boolean validateAndProcess(Trade tradeView);

    List<Trade> getFullTrade(Collection<Long> tradeIds);

}
