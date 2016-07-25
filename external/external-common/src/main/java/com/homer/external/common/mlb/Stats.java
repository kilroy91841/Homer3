package com.homer.external.common.mlb;

import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 7/23/16.
 */
public class Stats<T extends BaseStats> {
    @Nullable
    private T seasonStats;
    private List<T> gameLog;

    public Stats(T seasonStats, List<T> gameLog) {
        this.seasonStats = seasonStats;
        this.gameLog = gameLog;
    }

    public Stats() {
        gameLog = Lists.newArrayList();
    }

    @Nullable
    public T getSeasonStats() {
        return seasonStats;
    }

    public void setSeasonStats(@Nullable T seasonStats) {
        this.seasonStats = seasonStats;
    }

    public List<T> getGameLog() {
        return gameLog;
    }

    public void setGameLog(List<T> gameLog) {
        this.gameLog = gameLog;
    }
}
