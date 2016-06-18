package com.homer.type;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 5/7/16.
 */
@Table(name = "free_agent_auction_bids", schema = "homer")
public class FreeAgentAuctionBid extends BaseObject {

    @Column(updatable = false)
    private long freeAgentAuctionId;

    @Column(updatable = false)
    private long teamId;

    @Column
    private int amount;

    public long getFreeAgentAuctionId() {
        return freeAgentAuctionId;
    }

    public void setFreeAgentAuctionId(long freeAgentAuctionId) {
        this.freeAgentAuctionId = freeAgentAuctionId;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
