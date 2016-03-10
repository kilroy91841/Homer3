package com.homer.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.homer.util.EnumUtil;
import org.apache.commons.collections.EnumerationUtils;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 2/14/16.
 */
@Table(name = "player")
public class Player extends BaseObject {

    @Column
    private String name;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    @JsonIgnore
    private int positionId;
    @Column
    @JsonIgnore
    private int mlbTeamId;

    private MLBTeam mlbTeam;
    private Position position;

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

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public Position getPosition() {
        if (position == null) {
            position = EnumUtil.from(Position.class, positionId);
        }
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
        this.positionId = position.getId();
    }

    public MLBTeam getMlbTeam() {
        if (mlbTeam == null) {
            mlbTeam = EnumUtil.from(MLBTeam.class, mlbTeamId);
        }
        return mlbTeam;
    }

    public void setMlbTeam(MLBTeam mlbTeam) {
        this.mlbTeam = mlbTeam;
        this.mlbTeamId = mlbTeam.getId();
    }

    public int getMlbTeamId() {
        return mlbTeamId;
    }

    public void setMlbTeamId(int mlbTeamId) {
        this.mlbTeamId = mlbTeamId;
    }
}
