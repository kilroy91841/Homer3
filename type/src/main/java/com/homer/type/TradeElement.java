package com.homer.type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 3/15/16.
 */
@Table(name = "trade_elements", schema = "homer")
public class TradeElement extends BaseObject {

    @JsonIgnore
    @Column
    private long tradeId;
    @JsonIgnore
    @Column
    private long teamFromId;
    @JsonIgnore
    @Column
    private long teamToId;

    //Player
    @Nullable
    @JsonIgnore
    @Column(updatable = false)
    private Long playerId;

    //DraftDollar
    @Nullable
    @JsonIgnore
    @Column(updatable = false)
    private Long draftDollarId;
    @Nullable
    @Column(updatable = false)
    private Integer draftDollarAmount;

    //MinorLeaguePick
    @Nullable
    @JsonIgnore
    @Column(updatable = false)
    private Long minorLeaguePickId;
    @Nullable
    @Column(updatable = false)
    private Boolean swapTrade;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TradeElement that = (TradeElement) o;

        if (tradeId != that.tradeId) return false;
        if (teamFromId != that.teamFromId) return false;
        if (teamToId != that.teamToId) return false;
        if (playerId != null ? !playerId.equals(that.playerId) : that.playerId != null) return false;
        if (draftDollarId != null ? !draftDollarId.equals(that.draftDollarId) : that.draftDollarId != null)
            return false;
        if (draftDollarAmount != null ? !draftDollarAmount.equals(that.draftDollarAmount) : that.draftDollarAmount != null)
            return false;
        if (swapTrade != null ? !swapTrade.equals(that.swapTrade) : that.swapTrade != null) return false;
        return minorLeaguePickId != null ? minorLeaguePickId.equals(that.minorLeaguePickId) : that.minorLeaguePickId == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (tradeId ^ (tradeId >>> 32));
        result = 31 * result + (int) (teamFromId ^ (teamFromId >>> 32));
        result = 31 * result + (int) (teamToId ^ (teamToId >>> 32));
        result = 31 * result + (playerId != null ? playerId.hashCode() : 0);
        result = 31 * result + (draftDollarId != null ? draftDollarId.hashCode() : 0);
        result = 31 * result + (draftDollarAmount != null ? draftDollarAmount.hashCode() : 0);
        result = 31 * result + (minorLeaguePickId != null ? minorLeaguePickId.hashCode() : 0);
        result = 31 * result + (swapTrade != null ? swapTrade.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TradeElement{" +
                "tradeId=" + tradeId +
                ", teamFromId=" + teamFromId +
                ", teamToId=" + teamToId +
                ", playerId=" + playerId +
                ", draftDollarId=" + draftDollarId +
                ", draftDollarAmount=" + draftDollarAmount +
                ", minorLeaguePickId=" + minorLeaguePickId +
                ", swapTrade=" + swapTrade +
                "} " + super.toString();
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public long getTeamFromId() {
        return teamFromId;
    }

    public void setTeamFromId(long teamFromId) {
        this.teamFromId = teamFromId;
    }

    public long getTeamToId() {
        return teamToId;
    }

    public void setTeamToId(long teamToId) {
        this.teamToId = teamToId;
    }

    @Nullable
    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(@Nullable Long playerId) {
        this.playerId = playerId;
    }

    @Nullable
    public Long getDraftDollarId() {
        return draftDollarId;
    }

    public void setDraftDollarId(@Nullable Long draftDollarId) {
        this.draftDollarId = draftDollarId;
    }

    @Nullable
    public Integer getDraftDollarAmount() {
        return draftDollarAmount;
    }

    public void setDraftDollarAmount(@Nullable Integer draftDollarAmount) {
        this.draftDollarAmount = draftDollarAmount;
    }

    @Nullable
    public Long getMinorLeaguePickId() {
        return minorLeaguePickId;
    }

    public void setMinorLeaguePickId(@Nullable Long minorLeaguePickId) {
        this.minorLeaguePickId = minorLeaguePickId;
    }

    @Nullable
    public Boolean getSwapTrade() {
        return swapTrade;
    }

    public void setSwapTrade(@Nullable Boolean swapTrade) {
        this.swapTrade = swapTrade;
    }
}
