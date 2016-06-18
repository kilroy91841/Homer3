package com.homer.type.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.joda.time.DateTime;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 6/13/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FreeAgentAuctionAdminView {

    //required
    private long freeAgentAuctionId;

    private boolean denyAuction;
    private boolean startAuction;
    private boolean cancelAuction;
    private boolean winningTeam;

    @Nullable
    private String denyReason;
    @Nullable
    private Long teamId;

    public long getFreeAgentAuctionId() {
        return freeAgentAuctionId;
    }

    public void setFreeAgentAuctionId(long freeAgentAuctionId) {
        this.freeAgentAuctionId = freeAgentAuctionId;
    }

    public boolean getDenyAuction() {
        return denyAuction;
    }

    public void setDenyAuction(boolean denyAuction) {
        this.denyAuction = denyAuction;
    }

    public boolean getStartAuction() {
        return startAuction;
    }

    public void setStartAuction(boolean startAuction) {
        this.startAuction = startAuction;
    }

    public boolean getCancelAuction() {
        return cancelAuction;
    }

    public void setCancelAuction(boolean cancelAuction) {
        this.cancelAuction = cancelAuction;
    }

    @Nullable
    public String getDenyReason() {
        return denyReason;
    }

    public void setDenyReason(@Nullable String denyReason) {
        this.denyReason = denyReason;
    }

    public boolean getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(boolean winningTeam) {
        this.winningTeam = winningTeam;
    }

    @Nullable
    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(@Nullable Long teamId) {
        this.teamId = teamId;
    }
}
