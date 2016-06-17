package com.homer.type;

import com.google.common.base.Objects;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 5/4/16.
 */
@Table(name = "vultures", schema="homer")
public class Vulture extends BaseObject {

    @Column(updatable = false)
    private long playerId;

    @Nullable
    @Column(updatable = false)
    private Long dropPlayerId;

    @Column(updatable = false)
    private long teamId;

    @Column(updatable = false)
    private DateTime expirationDateUTC;

    @Column
    private EventStatus vultureStatus;

    @Column(updatable = false)
    private boolean isCommisionerVulture;

    private Player player;
    @Nullable
    private Player dropPlayer;
    private Team vultureTeam;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Vulture vulture = (Vulture) o;
        return playerId == vulture.playerId &&
                teamId == vulture.teamId &&
                isCommisionerVulture == vulture.isCommisionerVulture &&
                Objects.equal(dropPlayerId, vulture.dropPlayerId) &&
                Objects.equal(expirationDateUTC, vulture.expirationDateUTC) &&
                vultureStatus == vulture.vultureStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), playerId, dropPlayerId, teamId, expirationDateUTC, vultureStatus, isCommisionerVulture);
    }

    @Override
    public String toString() {
        return "Vulture{" +
                "playerId=" + playerId +
                ", dropPlayerId=" + dropPlayerId +
                ", teamId=" + teamId +
                ", expirationDateUTC=" + expirationDateUTC +
                ", vultureStatus=" + vultureStatus +
                ", isCommisionerVulture=" + isCommisionerVulture +
                "} " + super.toString();
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Nullable
    public Long getDropPlayerId() {
        return dropPlayerId;
    }

    public void setDropPlayerId(@Nullable Long dropPlayerId) {
        this.dropPlayerId = dropPlayerId;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public DateTime getExpirationDateUTC() {
        return expirationDateUTC;
    }

    public void setExpirationDateUTC(DateTime expirationDateUTC) {
        this.expirationDateUTC = expirationDateUTC;
    }

    public EventStatus getVultureStatus() {
        return vultureStatus;
    }

    public void setVultureStatus(EventStatus vultureStatus) {
        this.vultureStatus = vultureStatus;
    }

    public boolean getIsCommisionerVulture() {
        return isCommisionerVulture;
    }

    public void setIsCommisionerVulture(boolean commisionerVulture) {
        isCommisionerVulture = commisionerVulture;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Nullable
    public Player getDropPlayer() {
        return dropPlayer;
    }

    public void setDropPlayer(@Nullable Player dropPlayer) {
        this.dropPlayer = dropPlayer;
    }

    public Team getVultureTeam() {
        return vultureTeam;
    }

    public void setVultureTeam(Team vultureTeam) {
        this.vultureTeam = vultureTeam;
    }

    /**
     * Is a player vulturable or not
     * @param playerSeason
     * @return true if the player is vulturable, false if the player is not vulturable, null if the status is unknown
     */
    @Nullable
    public static Boolean isPlayerVulturable(PlayerSeason playerSeason) {
        if (playerSeason.getMlbStatus() == Status.UNKNOWN) {
            return null;
        }

        Position position = playerSeason.getFantasyPosition();

        //TODO when integration sets ab/ips, change this
        if (playerSeason.getIsMinorLeaguer()) {
            return false;
        }

        if (
                (position != Position.DISABLEDLIST && playerSeason.getMlbStatus() == Status.DISABLEDLIST) ||
                        (position != Position.MINORLEAGUES && playerSeason.getMlbStatus() == Status.MINORS) ||
                        (position == Position.DISABLEDLIST && playerSeason.getMlbStatus() != Status.DISABLEDLIST) ||
                        (position == Position.MINORLEAGUES && playerSeason.getMlbStatus() != Status.MINORS && !playerSeason.getIsMinorLeaguer())
                )
        {
            return true;
        }
        return false;
    }
}
