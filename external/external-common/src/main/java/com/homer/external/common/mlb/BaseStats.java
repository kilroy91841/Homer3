package com.homer.external.common.mlb;

import org.joda.time.DateTime;

/**
 * Created by arigolub on 4/19/16.
 */
public abstract class BaseStats {

    private DateTime gameDate;

    public DateTime getGameDate() {
        return gameDate;
    }

    public void setGameDate(DateTime gameDate) {
        this.gameDate = gameDate;
    }
}
