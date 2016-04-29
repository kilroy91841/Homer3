package com.homer.external.common.mlb;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 2/1/15.
 */
public class MLBPlayer {

    private String name;
    private String firstName;
    private String lastName;
    private long id;
    private int positionId;
    @Nullable
    private MLBPlayerStatus mlbPlayerStatus;
    private int teamId;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    @Nullable
    public MLBPlayerStatus getMlbPlayerStatus() {
        return mlbPlayerStatus;
    }

    public void setMlbPlayerStatus(@Nullable MLBPlayerStatus mlbPlayerStatus) {
        this.mlbPlayerStatus = mlbPlayerStatus;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
