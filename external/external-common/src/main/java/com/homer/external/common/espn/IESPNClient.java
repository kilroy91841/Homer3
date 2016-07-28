package com.homer.external.common.espn;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 7/25/16.
 */
public interface IESPNClient {

    List<ESPNPlayer> getRosterPage();

    List<ESPNTransaction> getTransactions(@Nullable Integer teamId, ESPNTransaction.Type tranType, String startDate, String endDate);

    default List<ESPNTransaction> getTransactions(ESPNTransaction.Type tranType, String startDate, String endDate) {
        return getTransactions(null, tranType, startDate, endDate);
    }
}
