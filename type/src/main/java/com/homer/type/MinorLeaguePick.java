package com.homer.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.homer.util.HomerBeanUtil;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by arigolub on 2/14/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "minor_league_picks", schema = "homer")
public class MinorLeaguePick extends BaseObject {

    @Column(updatable = false)
    private int season;

    @Column(updatable = false)
    private long originalTeamId;

    @Nullable
    @Column
    private long owningTeamId;

    @Nullable
    @Column
    private Long swapTeamId;

    @Column(updatable = false)
    private int round;

    @Nullable
    @Column
    private Integer overallPick;

    @Nullable
    @Column
    private Long playerId;

    @Nullable
    @Column
    private Boolean isSkipped;

    @Nullable
    @Column
    private String note;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MinorLeaguePick that = (MinorLeaguePick) o;

        if (season != that.season) return false;
        if (originalTeamId != that.originalTeamId) return false;
        if (owningTeamId != that.owningTeamId) return false;
        if (round != that.round) return false;
        if (swapTeamId != null ? !swapTeamId.equals(that.swapTeamId) : that.swapTeamId != null) return false;
        if (overallPick != null ? !overallPick.equals(that.overallPick) : that.overallPick != null) return false;
        if (playerId != null ? !playerId.equals(that.playerId) : that.playerId != null) return false;
        if (isSkipped != null ? !isSkipped.equals(that.isSkipped) : that.isSkipped != null) return false;
        return note != null ? note.equals(that.note) : that.note == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + season;
        result = 31 * result + (int) (originalTeamId ^ (originalTeamId >>> 32));
        result = 31 * result + (int) (owningTeamId ^ (owningTeamId >>> 32));
        result = 31 * result + (swapTeamId != null ? swapTeamId.hashCode() : 0);
        result = 31 * result + round;
        result = 31 * result + (overallPick != null ? overallPick.hashCode() : 0);
        result = 31 * result + (playerId != null ? playerId.hashCode() : 0);
        result = 31 * result + (isSkipped != null ? isSkipped.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        return result;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public long getOriginalTeamId() {
        return originalTeamId;
    }

    public void setOriginalTeamId(long originalTeamId) {
        this.originalTeamId = originalTeamId;
    }

    public long getOwningTeamId() {
        return owningTeamId;
    }

    public void setOwningTeamId(long owningTeamId) {
        this.owningTeamId = owningTeamId;
    }

    @Nullable
    public Long getSwapTeamId() {
        return swapTeamId;
    }

    public void setSwapTeamId(@Nullable Long swapTeamId) {
        this.swapTeamId = swapTeamId;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    @Nullable
    public Integer getOverallPick() {
        return overallPick;
    }

    public void setOverallPick(@Nullable Integer overallPick) {
        this.overallPick = overallPick;
    }

    @Nullable
    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(@Nullable Long playerId) {
        this.playerId = playerId;
    }

    @Nullable
    public Boolean getIsSkipped() {
        return isSkipped;
    }

    public void setIsSkipped(@Nullable Boolean skipped) {
        isSkipped = skipped;
    }

    @Nullable
    public String getNote() {
        return note;
    }

    public void setNote(@Nullable String note) {
        this.note = note;
    }
}
