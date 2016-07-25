package com.homer.external.common.mlb;

import java.time.LocalDate;

/**
 * Created by arigolub on 2/1/15.
 */
public class HittingStats extends BaseStats {

    private Integer atBats;
    private Integer homeRuns;
    private Integer runs;
    private Integer rbi;
    private Integer stolenBases;
    private Double battingAverage;
    private Double onBasePercentage;
    private Double onBasePlusSlugging;
    private Double sluggingPercentage;

    public HittingStats() { }

    public Integer getAtBats() {
        return atBats;
    }

    public void setAtBats(Integer atBats) {
        this.atBats = atBats;
    }

    public Integer getHomeRuns() {
        return homeRuns;
    }

    public void setHomeRuns(Integer homeRuns) {
        this.homeRuns = homeRuns;
    }

    public Integer getRuns() {
        return runs;
    }

    public void setRuns(Integer runs) {
        this.runs = runs;
    }

    public Integer getRbi() {
        return rbi;
    }

    public void setRbi(Integer rbi) {
        this.rbi = rbi;
    }

    public Integer getStolenBases() {
        return stolenBases;
    }

    public void setStolenBases(Integer stolenBases) {
        this.stolenBases = stolenBases;
    }

    public Double getBattingAverage() {
        return battingAverage;
    }

    public void setBattingAverage(Double battingAverage) {
        this.battingAverage = battingAverage;
    }

    public Double getOnBasePercentage() {
        return onBasePercentage;
    }

    public void setOnBasePercentage(Double onBasePercentage) {
        this.onBasePercentage = onBasePercentage;
    }

    public Double getOnBasePlusSlugging() {
        return onBasePlusSlugging;
    }

    public void setOnBasePlusSlugging(Double onBasePlusSlugging) {
        this.onBasePlusSlugging = onBasePlusSlugging;
    }

    public Double getSluggingPercentage() {
        return sluggingPercentage;
    }

    public void setSluggingPercentage(Double sluggingPercentage) {
        this.sluggingPercentage = sluggingPercentage;
    }
}
