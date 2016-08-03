package com.homer.type;

import com.google.common.base.Objects;
import com.homer.util.core.data.DateOnly;
import org.joda.time.DateTime;

import javax.persistence.Column;

/**
 * Created by arigolub on 7/30/16.
 */
public class BaseDaily extends BaseObject {

    @DateOnly
    @Column
    private DateTime date;
    @Column
    private int scoringPeriodId;

    //shared stats
    @Column
    private int walks;

    //hitting stats
    @Column
    private int atBats;
    @Column
    private int homeRuns;
    @Column
    private int runs;
    @Column
    private int rbi;
    @Column
    private int stolenBases;
    @Column
    private int hitByPitches;
    @Column
    private int sacFlies;
    @Column
    private int totalBases;

    //pitching stats
    @Column
    private int strikeouts;
    @Column
    private int wins;
    @Column
    private int saves;
    @Column
    private double inningsPitched;
    @Column
    private int hits;
    @Column
    private int earnedRuns;

    // region equals/hashCode/toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BaseDaily baseDaily = (BaseDaily) o;
        return scoringPeriodId == baseDaily.scoringPeriodId &&
                walks == baseDaily.walks &&
                atBats == baseDaily.atBats &&
                homeRuns == baseDaily.homeRuns &&
                runs == baseDaily.runs &&
                rbi == baseDaily.rbi &&
                stolenBases == baseDaily.stolenBases &&
                hitByPitches == baseDaily.hitByPitches &&
                sacFlies == baseDaily.sacFlies &&
                totalBases == baseDaily.totalBases &&
                strikeouts == baseDaily.strikeouts &&
                wins == baseDaily.wins &&
                saves == baseDaily.saves &&
                Double.compare(baseDaily.inningsPitched, inningsPitched) == 0 &&
                hits == baseDaily.hits &&
                earnedRuns == baseDaily.earnedRuns &&
                Objects.equal(date, baseDaily.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), date, scoringPeriodId, walks, atBats, homeRuns, runs, rbi, stolenBases, hitByPitches, sacFlies, totalBases, strikeouts, wins, saves, inningsPitched, hits, earnedRuns);
    }

    @Override
    public String toString() {
        return "BaseDaily{" +
                "date=" + date +
                ", scoringPeriodId=" + scoringPeriodId +
                ", walks=" + walks +
                ", atBats=" + atBats +
                ", homeRuns=" + homeRuns +
                ", runs=" + runs +
                ", rbi=" + rbi +
                ", stolenBases=" + stolenBases +
                ", hitByPitches=" + hitByPitches +
                ", sacFlies=" + sacFlies +
                ", totalBases=" + totalBases +
                ", strikeouts=" + strikeouts +
                ", wins=" + wins +
                ", saves=" + saves +
                ", inningsPitched=" + inningsPitched +
                ", hits=" + hits +
                ", earnedRuns=" + earnedRuns +
                "} " + super.toString();
    }

    // endregion

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public int getScoringPeriodId() {
        return scoringPeriodId;
    }

    public void setScoringPeriodId(int scoringPeriodId) {
        this.scoringPeriodId = scoringPeriodId;
    }

    public int getWalks() {
        return walks;
    }

    public void setWalks(int walks) {
        this.walks = walks;
    }

    public int getAtBats() {
        return atBats;
    }

    public void setAtBats(int atBats) {
        this.atBats = atBats;
    }

    public int getHomeRuns() {
        return homeRuns;
    }

    public void setHomeRuns(int homeRuns) {
        this.homeRuns = homeRuns;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getRbi() {
        return rbi;
    }

    public void setRbi(int rbi) {
        this.rbi = rbi;
    }

    public int getStolenBases() {
        return stolenBases;
    }

    public void setStolenBases(int stolenBases) {
        this.stolenBases = stolenBases;
    }

    public int getHitByPitches() {
        return hitByPitches;
    }

    public void setHitByPitches(int hitByPitches) {
        this.hitByPitches = hitByPitches;
    }

    public int getSacFlies() {
        return sacFlies;
    }

    public void setSacFlies(int sacFlies) {
        this.sacFlies = sacFlies;
    }

    public int getTotalBases() {
        return totalBases;
    }

    public void setTotalBases(int totalBases) {
        this.totalBases = totalBases;
    }

    public int getStrikeouts() {
        return strikeouts;
    }

    public void setStrikeouts(int strikeouts) {
        this.strikeouts = strikeouts;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getSaves() {
        return saves;
    }

    public void setSaves(int saves) {
        this.saves = saves;
    }

    public double getInningsPitched() {
        return inningsPitched;
    }

    public void setInningsPitched(double inningsPitched) {
        this.inningsPitched = inningsPitched;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getEarnedRuns() {
        return earnedRuns;
    }

    public void setEarnedRuns(int earnedRuns) {
        this.earnedRuns = earnedRuns;
    }
}
