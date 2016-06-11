package com.homer.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.homer.util.HomerBeanUtil;
import org.joda.time.DateTime;

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

    @Nullable
    @Column
    private DateTime deadlineUtc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MinorLeaguePick that = (MinorLeaguePick) o;
        return season == that.season &&
                originalTeamId == that.originalTeamId &&
                owningTeamId == that.owningTeamId &&
                round == that.round &&
                Objects.equal(swapTeamId, that.swapTeamId) &&
                Objects.equal(overallPick, that.overallPick) &&
                Objects.equal(playerId, that.playerId) &&
                Objects.equal(isSkipped, that.isSkipped) &&
                Objects.equal(note, that.note) &&
                Objects.equal(deadlineUtc, that.deadlineUtc);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), season, originalTeamId, owningTeamId, swapTeamId, round, overallPick, playerId, isSkipped, note, deadlineUtc);
    }

    @Override
    public String toString() {
        return "MinorLeaguePick{" +
                "season=" + season +
                ", originalTeamId=" + originalTeamId +
                ", owningTeamId=" + owningTeamId +
                ", swapTeamId=" + swapTeamId +
                ", round=" + round +
                ", overallPick=" + overallPick +
                ", playerId=" + playerId +
                ", isSkipped=" + isSkipped +
                ", note='" + note + '\'' +
                ", deadlineUtc=" + deadlineUtc +
                "} " + super.toString();
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
    public DateTime getDeadlineUtc() {
        return deadlineUtc;
    }

    public void setDeadlineUtc(@Nullable  DateTime deadlineUtc) {
        this.deadlineUtc = deadlineUtc;
    }

    @Nullable
    public String getNote() {
        return note;
    }

    public void setNote(@Nullable String note) {
        this.note = note;
    }
}
