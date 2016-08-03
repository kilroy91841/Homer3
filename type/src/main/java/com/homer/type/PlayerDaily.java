package com.homer.type;

import com.google.common.base.Objects;
import com.homer.util.core.data.DateOnly;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 7/29/16.
 */
@Table(name = "player_dailies", schema = "homer")
public class PlayerDaily extends BaseDaily {

    @Column(updatable = false)
    private long playerId;
    @Nullable
    @Column
    private Long teamId;
    @Nullable
    @Column
    private Position fantasyPosition;
    @Column
    private String gameId;

    @Nullable
    private Player player;

    // region equals/hashCode/toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PlayerDaily that = (PlayerDaily) o;
        return playerId == that.playerId &&
                Objects.equal(teamId, that.teamId) &&
                fantasyPosition == that.fantasyPosition &&
                Objects.equal(gameId, that.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), playerId, teamId, fantasyPosition, gameId);
    }

    @Override
    public String toString() {
        return "PlayerDaily{" +
                "playerId=" + playerId +
                ", teamId=" + teamId +
                ", fantasyPosition=" + fantasyPosition +
                ", gameId=" + gameId +
                "} " + super.toString();
    }

    // endregion

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Nullable
    public Position getFantasyPosition() {
        return fantasyPosition;
    }

    public void setFantasyPosition(@Nullable Position fantasyPosition) {
        this.fantasyPosition = fantasyPosition;
    }

    @Nullable
    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(@Nullable Long teamId) {
        this.teamId = teamId;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(@Nullable Player player) {
        this.player = player;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
