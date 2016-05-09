package com.homer.type;

import com.google.common.base.Objects;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 5/7/16.
 */
@Table(name = "free_agent_auctions", schema = "homer")
public class FreeAgentAuction extends BaseObject {

    @Column
    @Nullable
    private Long playerId;

    @Column(updatable = false)
    private DateTime expirationDateUTC;

    @Column(updatable = false)
    private int season;

    @Column
    private EventStatus auctionStatus;

    @Column
    private long requestingTeamid;

    @Column
    @Nullable
    private String requestedPlayerName;

    @Column
    @Nullable
    private Long winningTeamId;

    @Column
    @Nullable
    private Integer winningAmount;

    private Player player;
    private Team winningTeam;
    private Team requestingTeam;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FreeAgentAuction that = (FreeAgentAuction) o;
        return Objects.equal(playerId, that.playerId) &&
                Objects.equal(expirationDateUTC, that.expirationDateUTC) &&
                auctionStatus == that.auctionStatus &&
                Objects.equal(winningTeamId, that.winningTeamId) &&
                Objects.equal(winningAmount, that.winningAmount) &&
                season == that.season &&
                Objects.equal(requestedPlayerName, that.requestedPlayerName) &&
                requestingTeam == that.requestingTeam;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), playerId, expirationDateUTC, auctionStatus, winningTeamId, winningAmount, season, requestingTeam, requestedPlayerName);
    }

    @Override
    public String toString() {
        return "FreeAgentAuction{" +
                "playerId=" + playerId +
                ", expirationDateUTC=" + expirationDateUTC +
                ", auctionStatus=" + auctionStatus +
                ", winningTeamId=" + winningTeamId +
                ", winningAmount=" + winningAmount +
                ", player=" + player +
                ", winningTeam=" + winningTeam +
                ", season=" + season +
                ", requestingTeamId=" + requestingTeamid +
                ", requestingTeam=" + requestingTeam +
                ", requestedPlayerName=" + requestedPlayerName +
                "} " + super.toString();
    }

    @Nullable
    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(@Nullable  Long playerId) {
        this.playerId = playerId;
    }

    public DateTime getExpirationDateUTC() {
        return expirationDateUTC;
    }

    public void setExpirationDateUTC(DateTime expirationDateUTC) {
        this.expirationDateUTC = expirationDateUTC;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public EventStatus getAuctionStatus() {
        return auctionStatus;
    }

    public void setAuctionStatus(EventStatus auctionStatus) {
        this.auctionStatus = auctionStatus;
    }

    @Nullable
    public Long getWinningTeamId() {
        return winningTeamId;
    }

    public void setWinningTeamId(@Nullable Long winningTeamId) {
        this.winningTeamId = winningTeamId;
    }

    public long getRequestingTeamid() {
        return requestingTeamid;
    }

    public void setRequestingTeamid(long requestingTeamid) {
        this.requestingTeamid = requestingTeamid;
    }

    @Nullable
    public String getRequestedPlayerName() {
        return requestedPlayerName;
    }

    public void setRequestedPlayerName(@Nullable String requestedPlayerName) {
        this.requestedPlayerName = requestedPlayerName;
    }

    @Nullable
    public Integer getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(@Nullable Integer winningAmount) {
        this.winningAmount = winningAmount;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    public Team getRequestingTeam() {
        return requestingTeam;
    }

    public void setRequestingTeam(Team requestingTeam) {
        this.requestingTeam = requestingTeam;
    }
}