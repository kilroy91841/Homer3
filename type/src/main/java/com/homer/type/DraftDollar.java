package com.homer.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;
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
    private long teamId;
    @Column(updatable = false)
    private int season;
    @Column(updatable = false)
    private DraftDollarType draftDollarType;
    @Column
    private int amount;
    @Nullable
    @Column
    private Long tradeId;

    // region equals/hashCode/toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DraftDollar that = (DraftDollar) o;
        return teamId == that.teamId &&
                season == that.season &&
                amount == that.amount &&
                draftDollarType == that.draftDollarType &&
                Objects.equal(tradeId, that.tradeId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), teamId, season, draftDollarType, amount, tradeId);
    }

    @Override
    public String toString() {
        return "DraftDollar{" +
                "teamId=" + teamId +
                ", season=" + season +
                ", draftDollarType=" + draftDollarType +
                ", amount=" + amount +
                ", tradeId=" + tradeId +
                "} " + super.toString();
    }

    // endregion

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

    @Nullable
    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(@Nullable Long tradeId) {
        this.tradeId = tradeId;
    }
}
