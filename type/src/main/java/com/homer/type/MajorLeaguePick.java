package com.homer.type;

import com.google.common.base.Objects;
import com.homer.type.view.PlayerView;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 2/26/17.
 */
@Table(name = "major_league_picks", schema = "homer")
public class MajorLeaguePick extends BaseObject {

    @Column(updatable = false)
    private int season;

    @Column(updatable =  false)
    private long teamId;

    @Column(updatable = false)
    private long playerId;

    @Column(updatable = false)
    private int amount;

    private PlayerView playerView;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MajorLeaguePick that = (MajorLeaguePick) o;
        return season == that.season &&
                teamId == that.teamId &&
                playerId == that.playerId &&
                amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), season, teamId, playerId, amount);
    }

    @Override
    public String toString() {
        return "MajorLeaguePick{" +
                "season=" + season +
                ", teamId=" + teamId +
                ", playerId=" + playerId +
                ", amount=" + amount +
                "} " + super.toString();
    }

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public PlayerView getPlayerView() {
        return playerView;
    }

    public void setPlayerView(PlayerView playerView) {
        this.playerView = playerView;
    }
}
