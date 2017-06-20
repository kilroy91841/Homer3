package com.homer.util.core;

import org.joda.time.DateTime;

/**
 * Created by arigolub on 6/18/16.
 */
public interface ISchedulable {
    DateTime getDeadlineUTC();
    void setDeadlineUTC(DateTime deadlineUTC);
}
