package com.homer.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.homer.util.EnumUtil;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 2/14/16.
 */
@Table(name = "player_season")
public class PlayerSeason extends BaseObject {

    @Column
    private int season;
    @Column
    private long playerId;
    @Column
    @JsonIgnore
    private long teamId;
    @Column
    @JsonIgnore
    private int positionId;
    @Column
    @JsonIgnore
    private Long keeperTeamId;
    @Column
    private int keeperSeason;
    @Column
    private int salary;
    @Column
    private boolean isMinorLeaguer;

    private Position fantasyPosition;
    private Team team;
    private Team keeperTeam;

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public Position getFantasyPosition() {
        if (fantasyPosition == null) {
            fantasyPosition = EnumUtil.from(Position.class, positionId);
        }
        return fantasyPosition;
    }

    public void setFantasyPosition(Position fantasyPosition) {
        this.fantasyPosition = fantasyPosition;
        this.positionId = fantasyPosition.getId();
    }

    public int getKeeperSeason() {
        return keeperSeason;
    }

    public void setKeeperSeason(int keeperSeason) {
        this.keeperSeason = keeperSeason;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public boolean isMinorLeaguer() {
        return isMinorLeaguer;
    }

    public void setIsMinorLeaguer(boolean minorLeaguer) {
        isMinorLeaguer = minorLeaguer;
    }

    public Long getKeeperTeamId() {
        return keeperTeamId;
    }

    public void setKeeperTeamId(Long keeperTeamId) {
        this.keeperTeamId = keeperTeamId;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getKeeperTeam() {
        return keeperTeam;
    }

    public void setKeeperTeam(Team keeperTeam) {
        this.keeperTeam = keeperTeam;
    }
}
