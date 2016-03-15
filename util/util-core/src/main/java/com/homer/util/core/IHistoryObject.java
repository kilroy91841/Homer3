package com.homer.util.core;

import org.joda.time.DateTime;

/**
 * Created by arigolub on 2/15/16.
 */
public interface IHistoryObject extends IId, IDated {
    long getHistoryId();
    void setHistoryId(long objectId);

    DateTime getHistoryCreatedDateUTC();
    void setHistoryCreatedDateUTC(DateTime historyCreatedDateUTC);

    boolean getIsDeleted();
    void setIsDeleted(boolean isDeleted);
}
