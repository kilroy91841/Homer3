package com.homer.web.model;

import com.homer.type.MLBTeam;
import com.homer.type.Position;

import java.util.List;

/**
 * Created by arigolub on 4/17/16.
 */
public class MetadataView {

    private List<Position> positions;
    private List<MLBTeam> mlbTeams;
    private int season;
    private int nextSeason;

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public List<MLBTeam> getMlbTeams() {
        return mlbTeams;
    }

    public void setMlbTeams(List<MLBTeam> mlbTeams) {
        this.mlbTeams = mlbTeams;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getNextSeason() {
        return nextSeason;
    }

    public void setNextSeason(int nextSeason) {
        this.nextSeason = nextSeason;
    }
}
