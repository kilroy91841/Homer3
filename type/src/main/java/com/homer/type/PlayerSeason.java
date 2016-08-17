package com.homer.type;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

import com.google.common.base.Objects;

/**
 * Created by arigolub on 2/14/16.
 */
@Table(name = "player_seasons", schema = "homer")
public class PlayerSeason extends BaseObject {

    @Column(updatable = false)
    private int season;
    @Column(updatable = false)
    private long playerId;
    @Column
    @Nullable
    private Long teamId;
    @Nullable
    @Column
    private Position fantasyPosition;
    @Column
    @Nullable
    private Long keeperTeamId;
    @Column
    private int keeperSeason;
    @Column
    private int salary;
    @Column
    private boolean isMinorLeaguer;
    @Column
    private Status mlbStatus;
    @Column
    private boolean vulturable;
    @Column
    private boolean hasRookieStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PlayerSeason that = (PlayerSeason) o;
        return season == that.season &&
                playerId == that.playerId &&
                keeperSeason == that.keeperSeason &&
                salary == that.salary &&
                isMinorLeaguer == that.isMinorLeaguer &&
                vulturable == that.vulturable &&
                Objects.equal(teamId, that.teamId) &&
                fantasyPosition == that.fantasyPosition &&
                Objects.equal(keeperTeamId, that.keeperTeamId) &&
                hasRookieStatus == that.hasRookieStatus &&
                mlbStatus == that.mlbStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), season, playerId, teamId, fantasyPosition, keeperTeamId, keeperSeason, salary, isMinorLeaguer, mlbStatus, vulturable, hasRookieStatus);
    }

    @Override
    public String toString() {
        return "PlayerSeason{" +
                "season=" + season +
                ", playerId=" + playerId +
                ", teamId=" + teamId +
                ", fantasyPosition=" + fantasyPosition +
                ", keeperTeamId=" + keeperTeamId +
                ", keeperSeason=" + keeperSeason +
                ", salary=" + salary +
                ", isMinorLeaguer=" + isMinorLeaguer +
                ", mlbStatus=" + mlbStatus +
                ", hasRookieStatus=" + hasRookieStatus +
                ", vulturable=" + vulturable +
                "} " + super.toString();
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Nullable
    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(@Nullable Long teamId) {
        this.teamId = teamId;
    }

    @Nullable
    public Position getFantasyPosition() {
        return fantasyPosition;
    }

    public void setFantasyPosition(@Nullable Position fantasyPosition) {
        this.fantasyPosition = fantasyPosition;
    }

    @Nullable
    public Long getKeeperTeamId() {
        return keeperTeamId;
    }

    public void setKeeperTeamId(@Nullable Long keeperTeamId) {
        this.keeperTeamId = keeperTeamId;
    }

    public int getKeeperSeason() {
        return keeperSeason;
    }

    public void setKeeperSeason(int keeperSeason) {
        this.keeperSeason = keeperSeason;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public boolean getIsMinorLeaguer() {
        return isMinorLeaguer;
    }

    public void setIsMinorLeaguer(boolean minorLeaguer) {
        this.isMinorLeaguer = minorLeaguer;
    }

    public Status getMlbStatus() {
        return mlbStatus;
    }

    public void setMlbStatus(Status mlbStatus) {
        this.mlbStatus = mlbStatus;
    }

    public boolean getVulturable() {
        return vulturable;
    }

    public void setVulturable(boolean vulturable) {
        this.vulturable = vulturable;
    }

    public boolean getHasRookieStatus() {
        return hasRookieStatus;
    }

    public void setHasRookieStatus(boolean hasRookieStatus) {
        this.hasRookieStatus = hasRookieStatus;
    }
}
