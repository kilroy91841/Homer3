package com.homer.external.common.espn;

import org.joda.time.DateTime;

/**
 * Created by arigolub on 7/25/16.
 */
public class ESPNTransaction {

    public enum Type {
        MOVE(1),
        ADD(2),
        DROP(3),
        TRADE(4),
        ;

        private final int id;

        Type(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    private DateTime transDate;
    private String playerName;
    private Type type;
    private int teamId;
    private String text;
    private String oldPosition;
    private String newPosition;

    @Override
    public String toString() {
        return "ESPNTransaction{" +
                "transDate=" + transDate +
                ", playerName='" + playerName + '\'' +
                ", type=" + type +
                ", teamId=" + teamId +
                ", text='" + text + '\'' +
                ", oldPosition='" + oldPosition + '\'' +
                ", newPosition='" + newPosition + '\'' +
                '}';
    }

    public DateTime getTransDate() {
        return transDate;
    }

    public void setTransDate(DateTime transDate) {
        this.transDate = transDate;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(String oldPosition) {
        this.oldPosition = oldPosition;
    }

    public String getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(String newPosition) {
        this.newPosition = newPosition;
    }
}
