package com.homer.type;

import com.google.common.base.Objects;
import com.homer.util.core.data.DateOnly;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 7/30/16.
 */
@Table(name = "team_dailies", schema = "homer")
public class TeamDaily extends BaseDaily {

    @Column
    private long teamId;
    @Column
    private int pitcherWalks;
    @Column
    private int pitcherHits;
    @Column
    private int pitcherStrikeouts;

    // region equals/hashCode/toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TeamDaily teamDaily = (TeamDaily) o;
        return teamId == teamDaily.teamId &&
                pitcherWalks == teamDaily.pitcherWalks &&
                pitcherHits == teamDaily.pitcherHits &&
                pitcherStrikeouts == teamDaily.pitcherStrikeouts;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), teamId, pitcherWalks, pitcherHits, pitcherStrikeouts);
    }

    @Override
    public String toString() {
        return "TeamDaily{" +
                "teamId=" + teamId +
                ", pitcherWalks=" + pitcherWalks +
                ", pitcherHits=" + pitcherHits +
                ", pitcherStrikeouts=" + pitcherStrikeouts +
                "} " + super.toString();
    }

    // endregion


    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public int getPitcherWalks() {
        return pitcherWalks;
    }

    public void setPitcherWalks(int pitcherWalks) {
        this.pitcherWalks = pitcherWalks;
    }

    public int getPitcherHits() {
        return pitcherHits;
    }

    public void setPitcherHits(int pitcherHits) {
        this.pitcherHits = pitcherHits;
    }

    public int getPitcherStrikeouts() {
        return pitcherStrikeouts;
    }

    public void setPitcherStrikeouts(int pitcherStrikeouts) {
        this.pitcherStrikeouts = pitcherStrikeouts;
    }
}
