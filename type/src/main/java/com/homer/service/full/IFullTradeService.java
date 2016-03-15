package com.homer.service.full;

import com.homer.type.view.TradeView;

/**
 * Created by arigolub on 3/20/16.
 */
public interface IFullTradeService {

    boolean validateAndProcess(TradeView tradeView);

}
