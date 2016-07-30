package com.homer.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 2/14/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "players", schema = "homer")
public class Player extends BaseObject {

    @Column
    private String name;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private Position position;
    @Column
    private int mlbTeamId;
    @Column
    @Nullable
    private Long mlbPlayerId;
    @Column
    @Nullable
    private Long espnPlayerId;

    public boolean isBatter() {
        return !(position.equals(Position.PITCHER) || position.equals(Position.RELIEFPITCHER));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Player player = (Player) o;
        return mlbTeamId == player.mlbTeamId &&
                Objects.equal(name, player.name) &&
                Objects.equal(firstName, player.firstName) &&
                Objects.equal(lastName, player.lastName) &&
                position == player.position &&
                Objects.equal(mlbPlayerId, player.mlbPlayerId) &&
                Objects.equal(espnPlayerId, player.espnPlayerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), name, firstName, lastName, position, mlbTeamId, mlbPlayerId, espnPlayerId);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", position=" + position +
                ", mlbTeamId=" + mlbTeamId +
                ", mlbPlayerId=" + mlbPlayerId +
                ", espnPlayerId=" + espnPlayerId +
                "} " + super.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getMlbTeamId() {
        return mlbTeamId;
    }

    public void setMlbTeamId(int mlbTeamId) {
        this.mlbTeamId = mlbTeamId;
    }

    @Nullable
    public Long getMlbPlayerId() {
        return mlbPlayerId;
    }

    public void setMlbPlayerId(@Nullable Long mlbPlayerId) {
        this.mlbPlayerId = mlbPlayerId;
    }

    @Nullable
    public Long getEspnPlayerId() {
        return espnPlayerId;
    }

    public void setEspnPlayerId(@Nullable Long espnPlayerId) {
        this.espnPlayerId = espnPlayerId;
    }
}
