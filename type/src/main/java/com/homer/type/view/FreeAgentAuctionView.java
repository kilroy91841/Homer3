package com.homer.type.view;

import com.google.common.collect.Lists;
import com.homer.type.*;
import com.homer.util.HomerBeanUtil;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 6/13/16.
 */
public class FreeAgentAuctionView extends FreeAgentAuction {

    @Nullable
    private Player player;
    @Nullable
    private Team winningTeam;
    private Team requestingTeam;
    private List<FreeAgentAuctionBidView> bids = Lists.newArrayList();

    public static FreeAgentAuctionView from(FreeAgentAuction faa) {
        FreeAgentAuctionView view = new FreeAgentAuctionView();
        HomerBeanUtil.copyProperties(view, faa);
        return view;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(@Nullable Player player) {
        this.player = player;
    }

    @Nullable
    public Team getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(@Nullable Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    public Team getRequestingTeam() {
        return requestingTeam;
    }

    public void setRequestingTeam(Team requestingTeam) {
        this.requestingTeam = requestingTeam;
    }

    public List<FreeAgentAuctionBidView> getBids() {
        return bids;
    }

    public void setBids(List<FreeAgentAuctionBidView> bids) {
        this.bids = bids;
    }
}
