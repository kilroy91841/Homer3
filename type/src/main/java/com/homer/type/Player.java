package com.homer.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Objects;

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
    @JsonIgnore
    private int mlbTeamId;
    @Column
    @Nullable
    private Long mlbPlayerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Player player = (Player) o;

        if (mlbTeamId != player.mlbTeamId) return false;
        if (!name.equals(player.name)) return false;
        if (!firstName.equals(player.firstName)) return false;
        if (!lastName.equals(player.lastName)) return false;
        if (mlbPlayerId != null ? !mlbPlayerId.equals(player.mlbPlayerId) : player.mlbPlayerId!= null) return false;
        return position == player.position;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + mlbTeamId;
        result = 31 * result + (mlbPlayerId != null ? mlbPlayerId.hashCode() : 0);
        return result;
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
}
