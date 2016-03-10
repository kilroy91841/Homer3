package com.homer.type.view;

import com.homer.type.Team;

import java.util.List;

/**
 * Created by arigolub on 2/15/16.
 */
public class TeamView {

    private Team team;
    private List<PlayerView> majorLeaguers;
    private List<PlayerView> minorLeaguers;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<PlayerView> getMajorLeaguers() {
        return majorLeaguers;
    }

    public void setMajorLeaguers(List<PlayerView> majorLeaguers) {
        this.majorLeaguers = majorLeaguers;
    }

    public List<PlayerView> getMinorLeaguers() {
        return minorLeaguers;
    }

    public void setMinorLeaguers(List<PlayerView> minorLeaguers) {
        this.minorLeaguers = minorLeaguers;
    }
}
