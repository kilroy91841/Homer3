package com.homer.web.model;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 5/7/16.
 */
public class VultureRequest {

    private long vultureTeamId;
    private long playerId;
    @Nullable
    private Long dropPlayerId;
    private boolean isCommissionerVulture;

    public long getVultureTeamId() {
        return vultureTeamId;
    }

    public void setVultureTeamId(long vultureTeamId) {
        this.vultureTeamId = vultureTeamId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Nullable
    public Long getDropPlayerId() {
        return dropPlayerId;
    }

    public void setDropPlayerId(@Nullable Long dropPlayerId) {
        this.dropPlayerId = dropPlayerId;
    }

    public boolean getIsCommissionerVulture() {
        return isCommissionerVulture;
    }

    public void setIsCommissionerVulture(boolean commissionerVulture) {
        isCommissionerVulture = commissionerVulture;
    }
}
