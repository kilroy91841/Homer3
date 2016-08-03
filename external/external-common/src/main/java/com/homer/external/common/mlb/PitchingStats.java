package com.homer.external.common.mlb;

/**
 * Created by arigolub on 7/23/16.
 */
public class PitchingStats extends BaseStats {

    private Double whip;
    private Double era;
    private Integer strikeouts;
    private Integer wins;
    private Integer saves;
    private Double inningsPitched;
    private Integer walks;
    private Integer hits;
    private Integer earnedRuns;

    public PitchingStats() {}

    public Double getWhip() {
        return whip;
    }

    public void setWhip(Double whip) {
        this.whip = whip;
    }

    public Double getEra() {
        return era;
    }

    public void setEra(Double era) {
        this.era = era;
    }

    public Integer getStrikeouts() {
        return strikeouts;
    }

    public void setStrikeouts(Integer strikeouts) {
        this.strikeouts = strikeouts;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getSaves() {
        return saves;
    }

    public void setSaves(Integer saves) {
        this.saves = saves;
    }

    public Double getInningsPitched() {
        return inningsPitched;
    }

    public void setInningsPitched(Double inningsPitched) {
        this.inningsPitched = inningsPitched;
    }

    public Integer getWalks() {
        return walks;
    }

    public void setWalks(Integer walks) {
        this.walks = walks;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Integer getEarnedRuns() {
        return earnedRuns;
    }

    public void setEarnedRuns(Integer earnedRuns) {
        this.earnedRuns = earnedRuns;
    }
}
