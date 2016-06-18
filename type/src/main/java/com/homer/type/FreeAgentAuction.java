package com.homer.type;

import com.google.common.base.Objects;
import com.homer.util.core.ISchedulable;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 5/7/16.
 */
@Table(name = "free_agent_auctions", schema = "homer")
public class FreeAgentAuction extends BaseObject implements ISchedulable {

    @Column
    @Nullable
    private Long playerId;

    @Column(updatable = false)
    private DateTime deadlineUTC;

    @Column(updatable = false)
    private int season;

    @Column
    private EventStatus auctionStatus;

    @Column
    private long requestingTeamId;

    @Column
    @Nullable
    private String requestedPlayerName;

    @Column
    @Nullable
    private Long winningTeamId;

    @Column
    @Nullable
    private Integer winningAmount;

    @Nullable
    private Player player;
    @Nullable
    private PlayerSeason playerSeason;
    @Nullable
    private Team winningTeam;
    private Team requestingTeam;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FreeAgentAuction that = (FreeAgentAuction) o;
        return Objects.equal(playerId, that.playerId) &&
                Objects.equal(deadlineUTC, that.deadlineUTC) &&
                auctionStatus == that.auctionStatus &&
                Objects.equal(winningTeamId, that.winningTeamId) &&
                Objects.equal(winningAmount, that.winningAmount) &&
                season == that.season &&
                Objects.equal(requestedPlayerName, that.requestedPlayerName) &&
                requestingTeam == that.requestingTeam;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), playerId, deadlineUTC, auctionStatus, winningTeamId, winningAmount, season, requestingTeam, requestedPlayerName);
    }

    @Override
    public String toString() {
        return "FreeAgentAuction{" +
                "playerId=" + playerId +
                ", expirationDateUTC=" + deadlineUTC +
                ", auctionStatus=" + auctionStatus +
                ", winningTeamId=" + winningTeamId +
                ", winningAmount=" + winningAmount +
                ", player=" + player +
                ", playerSeason=" + playerSeason +
                ", winningTeam=" + winningTeam +
                ", season=" + season +
                ", requestingTeamId=" + requestingTeamId +
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

    public DateTime getDeadlineUTC() {
        return deadlineUTC;
    }

    public void setDeadlineUTC(DateTime deadlineUTC) {
        this.deadlineUTC = deadlineUTC;
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

    public long getRequestingTeamId() {
        return requestingTeamId;
    }

    public void setRequestingTeamId(long requestingTeamId) {
        this.requestingTeamId = requestingTeamId;
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

    @Nullable
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(@Nullable Player player) {
        this.player = player;
    }

    @Nullable
    public PlayerSeason getPlayerSeason() {
        return playerSeason;
    }

    public void setPlayerSeason(@Nullable PlayerSeason playerSeason) {
        this.playerSeason = playerSeason;
    }

    @Nullable
    public Team getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(@Nullable Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    public Team getRequestingTeam() {
        return requestingTeam;
    }

    public void setRequestingTeam(Team requestingTeam) {
        this.requestingTeam = requestingTeam;
    }
}
