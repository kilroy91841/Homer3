package com.homer.type;

import com.google.common.base.Objects;
import com.homer.util.core.data.DateOnly;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 7/30/16.
 */
@Table(name = "standings", schema = "homer")
public class Standing extends BaseObject {

    @DateOnly
    @Column(updatable = false)
    private DateTime date;

    @Column(updatable = false)
    private long teamId;

    @Column
    private double runsPoints;
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
    @Column
    private double obpTotal;

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
    @Column
    private double eraTotal;

    @Column
    private double whipPoints;
    @Column
    private double whipTotal;

    @Column
    private double totalPoints;

    @Column
    private int place;
    
    // region equals/hashCode/toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Standing standing = (Standing) o;
        return teamId == standing.teamId &&
                Double.compare(standing.runsPoints, runsPoints) == 0 &&
                runTotal == standing.runTotal &&
                Double.compare(standing.hrPoints, hrPoints) == 0 &&
                hrTotal == standing.hrTotal &&
                Double.compare(standing.rbiPoints, rbiPoints) == 0 &&
                rbiTotal == standing.rbiTotal &&
                Double.compare(standing.sbPoints, sbPoints) == 0 &&
                sbTotal == standing.sbTotal &&
                Double.compare(standing.obpPoints, obpPoints) == 0 &&
                Double.compare(standing.obpTotal, obpTotal) == 0 &&
                Double.compare(standing.kPoints, kPoints) == 0 &&
                kTotal == standing.kTotal &&
                Double.compare(standing.winPoints, winPoints) == 0 &&
                winTotal == standing.winTotal &&
                Double.compare(standing.savePoints, savePoints) == 0 &&
                saveTotal == standing.saveTotal &&
                Double.compare(standing.eraPoints, eraPoints) == 0 &&
                Double.compare(standing.eraTotal, eraTotal) == 0 &&
                Double.compare(standing.whipPoints, whipPoints) == 0 &&
                Double.compare(standing.whipTotal, whipTotal) == 0 &&
                Double.compare(standing.totalPoints, totalPoints) == 0 &&
                place == standing.place &&
                Objects.equal(date, standing.date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), date, teamId, runsPoints, runTotal, hrPoints, hrTotal, rbiPoints, rbiTotal, sbPoints, sbTotal, obpPoints, obpTotal, kPoints, kTotal, winPoints, winTotal, savePoints, saveTotal, eraPoints, eraTotal, whipPoints, whipTotal, totalPoints, place);
    }

    @Override
    public String toString() {
        return "Standing{" +
                "date=" + date +
                "\n, teamId=" + teamId +
                "\n, runsPoints=" + runsPoints +
                "\n, runTotal=" + runTotal +
                "\n, hrPoints=" + hrPoints +
                "\n, hrTotal=" + hrTotal +
                "\n, rbiPoints=" + rbiPoints +
                "\n, rbiTotal=" + rbiTotal +
                "\n, sbPoints=" + sbPoints +
                "\n, sbTotal=" + sbTotal +
                "\n, obpPoints=" + obpPoints +
                "\n, obpTotal=" + obpTotal +
                "\n, kPoints=" + kPoints +
                "\n, kTotal=" + kTotal +
                "\n, winPoints=" + winPoints +
                "\n, winTotal=" + winTotal +
                "\n, savePoints=" + savePoints +
                "\n, saveTotal=" + saveTotal +
                "\n, eraPoints=" + eraPoints +
                "\n, eraTotal=" + eraTotal +
                "\n, whipPoints=" + whipPoints +
                "\n, whipTotal=" + whipTotal +
                "\n, totalPoints=" + totalPoints +
                "\n, place=" + place +
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

    public double getRunsPoints() {
        return runsPoints;
    }

    public void setRunsPoints(double runsPoints) {
        this.runsPoints = runsPoints;
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

    public double getObpTotal() {
        return obpTotal;
    }

    public void setObpTotal(double obpTotal) {
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

    public double getEraTotal() {
        return eraTotal;
    }

    public void setEraTotal(double eraTotal) {
        this.eraTotal = eraTotal;
    }

    public double getWhipPoints() {
        return whipPoints;
    }

    public void setWhipPoints(double whipPoints) {
        this.whipPoints = whipPoints;
    }

    public double getWhipTotal() {
        return whipTotal;
    }

    public void setWhipTotal(double whipTotal) {
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
}
