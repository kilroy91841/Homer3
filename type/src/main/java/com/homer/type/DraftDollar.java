package com.homer.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.homer.util.EnumUtil;
import com.homer.util.HomerBeanUtil;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 2/14/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "draft_dollars", schema = "homer")
public class DraftDollar extends BaseObject {

    @Column(updatable = false)
    @JsonIgnore
    private long teamId;
    @Column(updatable = false)
    private int season;
    @Column(updatable = false)
    private DraftDollarType draftDollarType;
    @Column
    private int amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DraftDollar that = (DraftDollar) o;

        if (teamId != that.teamId) return false;
        if (season != that.season) return false;
        if (amount != that.amount) return false;
        return draftDollarType == that.draftDollarType;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (teamId ^ (teamId >>> 32));
        result = 31 * result + season;
        result = 31 * result + draftDollarType.hashCode();
        result = 31 * result + amount;
        return result;
    }

    @Override
    public String toString() {
        return "DraftDollar{" +
                "teamId=" + teamId +
                ", season=" + season +
                ", draftDollarType=" + draftDollarType +
                ", amount=" + amount +
                "} " + super.toString();
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public DraftDollarType getDraftDollarType() {
        return draftDollarType;
    }

    public void setDraftDollarType(DraftDollarType draftDollarType) {
        this.draftDollarType = draftDollarType;
    }
}
