package com.homer.type.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.joda.time.DateTime;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 6/11/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MinorLeagueDraftAdminView {

    //required
    private long pickId;

    //one of these must be set
    private boolean undoPick;
    private boolean reschedulePick;
    private boolean assignPlayerToPick;
    private boolean skipPick;
    private boolean stopSkipper;

    //for "reschedulePick"
    @Nullable
    private DateTime deadlineUtc;
    //for "assignPlayerToPick"
    @Nullable
    private Long playerId;

    public long getPickId() {
        return pickId;
    }

    public void setPickId(long pickId) {
        this.pickId = pickId;
    }

    public boolean getUndoPick() {
        return undoPick;
    }

    public void setUndoPick(boolean undoPick) {
        this.undoPick = undoPick;
    }

    public boolean getReschedulePick() {
        return reschedulePick;
    }

    public void setReschedulePick(boolean reschedulePick) {
        this.reschedulePick = reschedulePick;
    }

    public boolean getAssignPlayerToPick() {
        return assignPlayerToPick;
    }

    public void setAssignPlayerToPick(boolean assignPlayerToPick) {
        this.assignPlayerToPick = assignPlayerToPick;
    }

    @Nullable
    public DateTime getDeadlineUtc() {
        return deadlineUtc;
    }

    public void setDeadlineUtc(@Nullable DateTime deadlineUtc) {
        this.deadlineUtc = deadlineUtc;
    }

    @Nullable
    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(@Nullable Long playerId) {
        this.playerId = playerId;
    }

    public boolean getSkipPick() {
        return skipPick;
    }

    public void setSkipPick(boolean skipPick) {
        this.skipPick = skipPick;
    }

    public boolean getStopSkipper() {
        return stopSkipper;
    }

    public void setStopSkipper(boolean stopSkipper) {
        this.stopSkipper = stopSkipper;
    }
}
