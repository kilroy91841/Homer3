package com.homer.service.full;

import com.homer.service.IFreeAgentAuctionBidService;
import com.homer.service.IFreeAgentAuctionService;
import com.homer.service.IPlayerSeasonService;
import com.homer.service.IPlayerService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Created by arigolub on 5/8/16.
 */
public class FullFreeAgentAuctionServiceTest {

    private FullFreeAgentAuctionService service;

    @Before
    public void setup() {
        IFreeAgentAuctionService freeAgentAuctionService = mock(IFreeAgentAuctionService.class);
        IFreeAgentAuctionBidService freeAgentAuctionBidService = mock(IFreeAgentAuctionBidService.class);
        IPlayerSeasonService playerSeasonService = mock(IPlayerSeasonService.class);
        IPlayerService playerService = mock(IPlayerService.class);

        service = new FullFreeAgentAuctionService(freeAgentAuctionService, freeAgentAuctionBidService, playerSeasonService,
                playerService);
    }

    @Test
    public void test_requestFreeAgentAuction_playerId() {

    }

    @Test
    public void test_requestFreeAgentAuction_playerName_exists() {

    }

    @Test
    public void test_requestFreeAgentAuction_playerName_doesNotExist() {

    }

    @Test
    public void test_startFreeAgentAuction() {

    }

    @Test
    public void test_denyFreeAgentAuctionRequest() {

    }

    @Test
    public void test_endFreeAgentAuction_NoBids() {

    }

    @Test
    public void test_endFreeAgentAuction_Tie() {

    }

    @Test
    public void test_endFreeAgentAuction() {

    }

    @Test
    public void test_bidOnAuction_insufficientFunds() {

    }

    @Test
    public void test_bidOnAuction_existingBid() {

    }

    @Test
    public void test_bidOnAuction_newBid() {
        
    }
}
