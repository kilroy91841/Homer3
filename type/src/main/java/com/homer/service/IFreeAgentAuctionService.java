package com.homer.service;

import com.homer.type.FreeAgentAuction;
import com.homer.type.FreeAgentAuctionBid;

import java.util.List;

/**
 * Created by arigolub on 5/8/16.
 */
public interface IFreeAgentAuctionService extends IIdService<FreeAgentAuction> {

    List<FreeAgentAuction> getForSeason(int season);
}
