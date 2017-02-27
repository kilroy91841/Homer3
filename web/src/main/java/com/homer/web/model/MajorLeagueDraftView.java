package com.homer.web.model;

import com.google.common.collect.Lists;
import com.homer.type.MajorLeaguePick;
import com.homer.type.Player;
import com.homer.type.view.PlayerView;

import java.util.List;

/**
 * Created by arigolub on 2/25/17.
 */
public class MajorLeagueDraftView {
    private long playerId;
    private long teamId;
    private int salary;
    private Player currentPlayer;

    private List<PlayerView> players = Lists.newArrayList();
    private List<PlayerView> freeAgents = Lists.newArrayList();
    private List<MajorLeaguePick> picks = Lists.newArrayList();

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

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public List<PlayerView> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerView> players) {
        this.players = players;
    }

    public List<PlayerView> getFreeAgents() {
        return freeAgents;
    }

    public void setFreeAgents(List<PlayerView> freeAgents) {
        this.freeAgents = freeAgents;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<MajorLeaguePick> getPicks() {
        return picks;
    }

    public void setPicks(List<MajorLeaguePick> picks) {
        this.picks = picks;
    }
}
