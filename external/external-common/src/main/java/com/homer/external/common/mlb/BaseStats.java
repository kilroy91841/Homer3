package com.homer.external.common.mlb;

import org.joda.time.DateTime;

/**
 * Created by arigolub on 4/19/16.
 */
public abstract class BaseStats {

    private DateTime gameDate;
    private String gameId;

    public DateTime getGameDate() {
        return gameDate;
    }

    public void setGameDate(DateTime gameDate) {
        this.gameDate = gameDate;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
