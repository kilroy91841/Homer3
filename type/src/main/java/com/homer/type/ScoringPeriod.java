package com.homer.type;

import org.joda.time.DateTime;

/**
 * Created by arigolub on 7/30/16.
 */
public class ScoringPeriod {

    private int scoringPeriodId;
    private DateTime date;

    public ScoringPeriod(int scoringPeriodId, DateTime date) {
        this.scoringPeriodId = scoringPeriodId;
        this.date = date;
    }

    public int getScoringPeriodId() {
        return scoringPeriodId;
    }

    public void setScoringPeriodId(int scoringPeriodId) {
        this.scoringPeriodId = scoringPeriodId;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }
}
