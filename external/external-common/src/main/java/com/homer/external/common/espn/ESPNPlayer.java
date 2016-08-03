package com.homer.external.common.espn;

/**
 * Created by arigolub on 7/25/16.
 */
public class ESPNPlayer {
    private String name;
    private String position;
    private int teamId;
    private int espnPlayerId;

    // region constructor/equals/hashCode/toString

    public ESPNPlayer(String name, String position, int teamId, int espnPlayerId) {
        this.name = name;
        this.position = position;
        this.teamId = teamId;
        this.espnPlayerId = espnPlayerId;
    }

    // endregion

    // region getters + setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getEspnPlayerId() {
        return espnPlayerId;
    }

    public void setEspnPlayerId(int espnPlayerId) {
        this.espnPlayerId = espnPlayerId;
    }

    // endregion
}
