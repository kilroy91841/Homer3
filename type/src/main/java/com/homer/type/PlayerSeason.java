package com.homer.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PlayerSeason that = (PlayerSeason) o;

        if (season != that.season) return false;
        if (playerId != that.playerId) return false;
        if (keeperSeason != that.keeperSeason) return false;
        if (salary != that.salary) return false;
        if (isMinorLeaguer != that.isMinorLeaguer) return false;
        if (teamId != null ? !teamId.equals(that.teamId) : that.teamId != null) return false;
        if (fantasyPosition != null ? !fantasyPosition.equals(that.fantasyPosition) : that.fantasyPosition != null) return false;
        return keeperTeamId != null ? keeperTeamId.equals(that.keeperTeamId) : that.keeperTeamId == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + season;
        result = 31 * result + (int) (playerId ^ (playerId >>> 32));
        result = 31 * result + (teamId != null ? teamId.hashCode() : 0);
        result = 31 * result + (fantasyPosition != null ? fantasyPosition.hashCode() : 0);
        result = 31 * result + (keeperTeamId != null ? keeperTeamId.hashCode() : 0);
        result = 31 * result + keeperSeason;
        result = 31 * result + salary;
        result = 31 * result + (isMinorLeaguer ? 1 : 0);
        return result;
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
}
