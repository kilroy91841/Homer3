package com.homer.type;

import com.google.common.base.Objects;
import com.homer.util.core.data.DateOnly;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 7/30/16.
 */
@Table(name = "standings", schema = "homer")
public class Standing extends BaseObject implements Comparable<Standing> {

    @DateOnly
    @Column(updatable = false)
    private DateTime date;

    @Column(updatable = false)
    private long teamId;

    @Column
    private double totalPoints;

    @Column
    private int place;

    // region batting stats

    @Column
    private double runPoints;
    @Column
    private int runTotal;

    @Column
    private double hrPoints;
    @Column
    private int hrTotal;

    @Column
    private double rbiPoints;
    @Column
    private int rbiTotal;

    @Column
    private double sbPoints;
    @Column
    private int sbTotal;

    @Column
    private double obpPoints;
    @Nullable
    @Column
    private Double obpTotal;

    @Column
    private int hitsTotal;
    @Column
    private int hitByPitchesTotal;
    @Column
    private int sacFliesTotal;
    @Column
    private int totalBasesTotal;
    @Column
    private int atBatsTotal;
    @Column
    private int walksTotal;

    //endregion

    // region pitching stats

    @Column
    private double kPoints;
    @Column
    private int kTotal;

    @Column
    private double winPoints;
    @Column
    private int winTotal;

    @Column
    private double savePoints;
    @Column
    private int saveTotal;

    @Column
    private double eraPoints;
    @Nullable
    @Column
    private Double eraTotal;

    @Column
    private double whipPoints;
    @Nullable
    @Column
    private Double whipTotal;

    @Column
    private int earnedRunsTotal;
    @Column
    private int pitcherWalksTotal;
    @Column
    private int pitcherHitsTotal;
    @Column
    private double inningsPitchedTotal;

    // endregion
    
    // region equals/hashCode/toString

    @Override
    public int compareTo(Standing o) {
        if (this.getTotalPoints() > o.getTotalPoints()) {
            return -1;
        } else if (this.getTotalPoints() < o.getTotalPoints()) {
            return 1;
        } else if (this.getTeamId() > o.getTeamId()) {
            return -1;
        } else if (this.getTeamId() < o.getTeamId()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Standing standing = (Standing) o;
        return teamId == standing.teamId &&
                Double.compare(standing.runPoints, runPoints) == 0 &&
                runTotal == standing.runTotal &&
                Double.compare(standing.hrPoints, hrPoints) == 0 &&
                hrTotal == standing.hrTotal &&
                Double.compare(standing.rbiPoints, rbiPoints) == 0 &&
                rbiTotal == standing.rbiTotal &&
                Double.compare(standing.sbPoints, sbPoints) == 0 &&
                sbTotal == standing.sbTotal &&
                Double.compare(standing.obpPoints, obpPoints) == 0 &&
                Double.compare(standing.kPoints, kPoints) == 0 &&
                kTotal == standing.kTotal &&
                Double.compare(standing.winPoints, winPoints) == 0 &&
                winTotal == standing.winTotal &&
                Double.compare(standing.savePoints, savePoints) == 0 &&
                saveTotal == standing.saveTotal &&
                Double.compare(standing.eraPoints, eraPoints) == 0 &&
                Double.compare(standing.whipPoints, whipPoints) == 0 &&
                Double.compare(standing.totalPoints, totalPoints) == 0 &&
                place == standing.place &&
                hitByPitchesTotal == standing.hitByPitchesTotal &&
                sacFliesTotal == standing.sacFliesTotal &&
                totalBasesTotal == standing.totalBasesTotal &&
                earnedRunsTotal == standing.earnedRunsTotal &&
                pitcherWalksTotal == standing.pitcherWalksTotal &&
                pitcherHitsTotal == standing.pitcherHitsTotal &&
                hitsTotal == standing.hitsTotal &&
                atBatsTotal == standing.atBatsTotal &&
                inningsPitchedTotal == standing.inningsPitchedTotal &&
                walksTotal == standing.walksTotal &&
                Objects.equal(date, standing.date) &&
                Objects.equal(obpTotal, standing.obpTotal) &&
                Objects.equal(eraTotal, standing.eraTotal) &&
                Objects.equal(whipTotal, standing.whipTotal);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), date, teamId, runPoints, runTotal, hrPoints, hrTotal, rbiPoints, rbiTotal, sbPoints, sbTotal, obpPoints, obpTotal, kPoints, kTotal, winPoints, winTotal, savePoints, saveTotal, eraPoints, eraTotal, whipPoints, whipTotal, totalPoints, place, hitByPitchesTotal, sacFliesTotal, totalBasesTotal, earnedRunsTotal, pitcherWalksTotal, pitcherHitsTotal, hitsTotal, atBatsTotal, inningsPitchedTotal, walksTotal);
    }

    @Override
    public String toString() {
        return "Standing{" +
                "date=" + date +
                ", teamId=" + teamId +
                ", runPoints=" + runPoints +
                ", runTotal=" + runTotal +
                ", hrPoints=" + hrPoints +
                ", hrTotal=" + hrTotal +
                ", rbiPoints=" + rbiPoints +
                ", rbiTotal=" + rbiTotal +
                ", sbPoints=" + sbPoints +
                ", sbTotal=" + sbTotal +
                ", obpPoints=" + obpPoints +
                ", obpTotal=" + obpTotal +
                ", kPoints=" + kPoints +
                ", kTotal=" + kTotal +
                ", winPoints=" + winPoints +
                ", winTotal=" + winTotal +
                ", savePoints=" + savePoints +
                ", saveTotal=" + saveTotal +
                ", eraPoints=" + eraPoints +
                ", eraTotal=" + eraTotal +
                ", whipPoints=" + whipPoints +
                ", whipTotal=" + whipTotal +
                ", totalPoints=" + totalPoints +
                ", place=" + place +
                ", hitByPitchesTotal=" + hitByPitchesTotal +
                ", atBatsTotal=" + atBatsTotal +
                ", sacFliesTotal=" + sacFliesTotal +
                ", totalBasesTotal=" + totalBasesTotal +
                ", pitcherWalksTotal=" + pitcherWalksTotal +
                ", pitcherHitsTotal=" + pitcherHitsTotal +
                ", inningsPitchedTotal=" + inningsPitchedTotal +
                ", hitsTotal=" + hitsTotal +
                ", walksTotal=" + walksTotal +
                ", earnedRunsTotal=" + earnedRunsTotal +
                "} " + super.toString();
    }

    // endregion

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public double getRunPoints() {
        return runPoints;
    }

    public void setRunPoints(double runPoints) {
        this.runPoints = runPoints;
    }

    public int getRunTotal() {
        return runTotal;
    }

    public void setRunTotal(int runTotal) {
        this.runTotal = runTotal;
    }

    public double getHrPoints() {
        return hrPoints;
    }

    public void setHrPoints(double hrPoints) {
        this.hrPoints = hrPoints;
    }

    public int getHrTotal() {
        return hrTotal;
    }

    public void setHrTotal(int hrTotal) {
        this.hrTotal = hrTotal;
    }

    public double getRbiPoints() {
        return rbiPoints;
    }

    public void setRbiPoints(double rbiPoints) {
        this.rbiPoints = rbiPoints;
    }

    public int getRbiTotal() {
        return rbiTotal;
    }

    public void setRbiTotal(int rbiTotal) {
        this.rbiTotal = rbiTotal;
    }

    public double getSbPoints() {
        return sbPoints;
    }

    public void setSbPoints(double sbPoints) {
        this.sbPoints = sbPoints;
    }

    public int getSbTotal() {
        return sbTotal;
    }

    public void setSbTotal(int sbTotal) {
        this.sbTotal = sbTotal;
    }

    public double getObpPoints() {
        return obpPoints;
    }

    public void setObpPoints(double obpPoints) {
        this.obpPoints = obpPoints;
    }

    @Nullable
    public Double getObpTotal() {
        return obpTotal;
    }

    public void setObpTotal(@Nullable Double obpTotal) {
        this.obpTotal = obpTotal;
    }

    public double getkPoints() {
        return kPoints;
    }

    public void setkPoints(double kPoints) {
        this.kPoints = kPoints;
    }

    public int getkTotal() {
        return kTotal;
    }

    public void setkTotal(int kTotal) {
        this.kTotal = kTotal;
    }

    public double getWinPoints() {
        return winPoints;
    }

    public void setWinPoints(double winPoints) {
        this.winPoints = winPoints;
    }

    public int getWinTotal() {
        return winTotal;
    }

    public void setWinTotal(int winTotal) {
        this.winTotal = winTotal;
    }

    public double getSavePoints() {
        return savePoints;
    }

    public void setSavePoints(double savePoints) {
        this.savePoints = savePoints;
    }

    public int getSaveTotal() {
        return saveTotal;
    }

    public void setSaveTotal(int saveTotal) {
        this.saveTotal = saveTotal;
    }

    public double getEraPoints() {
        return eraPoints;
    }

    public void setEraPoints(double eraPoints) {
        this.eraPoints = eraPoints;
    }

    @Nullable
    public Double getEraTotal() {
        return eraTotal;
    }

    public void setEraTotal(@Nullable Double eraTotal) {
        this.eraTotal = eraTotal;
    }

    public double getWhipPoints() {
        return whipPoints;
    }

    public void setWhipPoints(double whipPoints) {
        this.whipPoints = whipPoints;
    }

    @Nullable
    public Double getWhipTotal() {
        return whipTotal;
    }

    public void setWhipTotal(@Nullable Double whipTotal) {
        this.whipTotal = whipTotal;
    }

    public double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getHitByPitchesTotal() {
        return hitByPitchesTotal;
    }

    public void setHitByPitchesTotal(int hitByPitchesTotal) {
        this.hitByPitchesTotal = hitByPitchesTotal;
    }

    public int getSacFliesTotal() {
        return sacFliesTotal;
    }

    public void setSacFliesTotal(int sacFliesTotal) {
        this.sacFliesTotal = sacFliesTotal;
    }

    public int getTotalBasesTotal() {
        return totalBasesTotal;
    }

    public void setTotalBasesTotal(int totalBasesTotal) {
        this.totalBasesTotal = totalBasesTotal;
    }

    public int getEarnedRunsTotal() {
        return earnedRunsTotal;
    }

    public void setEarnedRunsTotal(int earnedRunsTotal) {
        this.earnedRunsTotal = earnedRunsTotal;
    }

    public int getPitcherWalksTotal() {
        return pitcherWalksTotal;
    }

    public void setPitcherWalksTotal(int pitcherWalksTotal) {
        this.pitcherWalksTotal = pitcherWalksTotal;
    }

    public int getPitcherHitsTotal() {
        return pitcherHitsTotal;
    }

    public void setPitcherHitsTotal(int pitcherHitsTotal) {
        this.pitcherHitsTotal = pitcherHitsTotal;
    }

    public int getHitsTotal() {
        return hitsTotal;
    }

    public void setHitsTotal(int hitsTotal) {
        this.hitsTotal = hitsTotal;
    }

    public int getAtBatsTotal() {
        return atBatsTotal;
    }

    public void setAtBatsTotal(int atBatsTotal) {
        this.atBatsTotal = atBatsTotal;
    }

    public double getInningsPitchedTotal() {
        return inningsPitchedTotal;
    }

    public void setInningsPitchedTotal(double inningsPitchedTotal) {
        this.inningsPitchedTotal = inningsPitchedTotal;
    }

    public int getWalksTotal() {
        return walksTotal;
    }

    public void setWalksTotal(int walksTotal) {
        this.walksTotal = walksTotal;
    }
}
