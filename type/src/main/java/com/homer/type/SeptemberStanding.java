package com.homer.type;

import com.google.common.base.Objects;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 11/13/16.
 */
@Table(name = "september_standings", schema = "homer")
public class SeptemberStanding extends BaseObject {

    @Column(updatable = false)
    private long teamId;
    @Column(updatable = false)
    private int season;
    @Nullable
    @Column
    private Integer place;
    @Column
    private double points;
    @Nullable
    @Column
    private Integer dollarsAwarded;

    // region equals/hashCode/toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SeptemberStanding that = (SeptemberStanding) o;
        return teamId == that.teamId &&
                season == that.season &&
                Double.compare(that.points, points) == 0 &&
                Objects.equal(place, that.place) &&
                Objects.equal(dollarsAwarded, that.dollarsAwarded);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), teamId, season, place, points, dollarsAwarded);
    }

    @Override
    public String toString() {
        return "SeptemberStanding{" +
                "teamId=" + teamId +
                ", season=" + season +
                ", place=" + place +
                ", points=" + points +
                ", dollarsAwarded=" + dollarsAwarded +
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

    @Nullable
    public Integer getPlace() {
        return place;
    }

    public void setPlace(@Nullable Integer place) {
        this.place = place;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    @Nullable
    public Integer getDollarsAwarded() {
        return dollarsAwarded;
    }

    public void setDollarsAwarded(@Nullable Integer dollarsAwarded) {
        this.dollarsAwarded = dollarsAwarded;
    }
}
