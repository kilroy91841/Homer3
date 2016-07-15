package com.homer.type.view;

import com.homer.type.FreeAgentAuctionBid;
import com.homer.type.Team;
import com.homer.util.HomerBeanUtil;

/**
 * Created by arigolub on 6/13/16.
 */
public class FreeAgentAuctionBidView extends FreeAgentAuctionBid {

    private Team team;

    public static FreeAgentAuctionBidView from(FreeAgentAuctionBid bid) {
        FreeAgentAuctionBidView view = new FreeAgentAuctionBidView();
        HomerBeanUtil.copyProperties(view, bid);
        return view;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
