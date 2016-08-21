package com.homer.type;

import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 8/11/16.
 */
@Table(name = "keepers", schema="homer")
public class Keeper extends BaseObject {
    @Column(updatable = false)
    private int season;
    @Column(updatable = false)
    private long teamId;
    @Column(updatable = false)
    private long playerId;
    @Column
    private int keeperSeason;
    @Column
    private int salary;
    @Column
    private boolean isMinorLeaguer;

    // region equals/hashCode/toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Keeper keeper = (Keeper) o;
        return season == keeper.season &&
                teamId == keeper.teamId &&
                playerId == keeper.playerId &&
                keeperSeason == keeper.keeperSeason &&
                salary == keeper.salary &&
                isMinorLeaguer == keeper.isMinorLeaguer;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), season, teamId, playerId, keeperSeason, salary, isMinorLeaguer);
    }

    @Override
    public String toString() {
        return "Keeper{" +
                "season=" + season +
                ", teamId=" + teamId +
                ", playerId=" + playerId +
                ", keeperSeason=" + keeperSeason +
                ", salary=" + salary +
                ", isMinorLeaguer=" + isMinorLeaguer +
                "} " + super.toString();
    }

    // endregion

    // region getters + setters

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
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
        isMinorLeaguer = minorLeaguer;
    }

    // endregion
}
