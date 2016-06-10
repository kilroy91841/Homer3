package com.homer.service.full;

import com.google.common.collect.Lists;
import com.homer.email.EmailRequest;
import com.homer.email.HtmlObject;
import com.homer.email.IEmailService;
import com.homer.exception.IllegalFreeAgentAuctionBidException;
import com.homer.service.*;
import com.homer.type.*;
import com.homer.util.LeagueUtil;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * Created by arigolub on 5/8/16.
 */
public class FullFreeAgentAuctionServiceTest {

    private FullFreeAgentAuctionService service;
    private IEmailService emailService;
    private IPlayerSeasonService playerSeasonService;
    private IPlayerService playerService;
    private IFreeAgentAuctionBidService freeAgentAuctionBidService;
    private IFreeAgentAuctionService freeAgentAuctionService;
    private IDraftDollarService draftDollarService;

    private static final Long PLAYER_ID = 1L;
    private static final long REQUESTING_TEAM_ID = 2;
    private static final String EXISTING_PLAYER_NAME = "Ari Golub";
    private static final String NEW_PLAYER_NAME = "Lindsay Young";
    private static final long EXISTING_FREE_AGENT_AUCTION_ID = 3L;

    @Before
    public void setup() {
        freeAgentAuctionService = mock(IFreeAgentAuctionService.class);
        freeAgentAuctionBidService = mock(IFreeAgentAuctionBidService.class);
        playerSeasonService = mock(IPlayerSeasonService.class);
        playerService = mock(IPlayerService.class);
        emailService = mock(IEmailService.class);
        draftDollarService = mock(IDraftDollarService.class);

        Player player = new Player();
        player.setId(PLAYER_ID);
        player.setName(EXISTING_PLAYER_NAME);
        when(playerService.getPlayerByName(EXISTING_PLAYER_NAME)).thenReturn(player);
        when(playerService.getById(PLAYER_ID)).thenReturn(player);

        FreeAgentAuction faa = new FreeAgentAuction();
        faa.setRequestedPlayerName(NEW_PLAYER_NAME);
        faa.setAuctionStatus(EventStatus.REQUESTED);
        when(freeAgentAuctionService.getById(EXISTING_FREE_AGENT_AUCTION_ID)).thenReturn(faa);

        when(freeAgentAuctionService.upsert(any(FreeAgentAuction.class))).thenAnswer(x -> x.getArguments()[0]);
        when(freeAgentAuctionBidService.upsert(any(FreeAgentAuctionBid.class))).thenAnswer(x -> x.getArguments()[0]);
        when(playerSeasonService.upsert(any(PlayerSeason.class))).thenAnswer(x -> x.getArguments()[0]);
        when(playerService.upsert(any(Player.class))).thenAnswer(x -> x.getArguments()[0]);
        when(draftDollarService.upsert(any(DraftDollar.class))).thenAnswer(x -> x.getArguments()[0]);

        service = new FullFreeAgentAuctionService(freeAgentAuctionService, freeAgentAuctionBidService, playerSeasonService,
                playerService, emailService, draftDollarService);
    }

//    @Test
//    public void test_requestFreeAgentAuction_playerId() {
//        FreeAgentAuction faa = new FreeAgentAuction();
//        faa.setPlayerId(PLAYER_ID);
//        faa.setRequestingTeamId(REQUESTING_TEAM_ID);
//        faa.setAuctionStatus(EventStatus.REQUESTED);
//        faa.setSeason(LeagueUtil.SEASON);
//
//        FreeAgentAuction freeAgentAuction = service.requestFreeAgentAuction(REQUESTING_TEAM_ID, PLAYER_ID, null);
//        assertEquals(faa, freeAgentAuction);
//        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
//    }
//
//    @Test
//    public void test_requestFreeAgentAuction_playerName_exists() {
//        FreeAgentAuction faa = new FreeAgentAuction();
//        faa.setPlayerId(PLAYER_ID);
//        faa.setRequestingTeamId(REQUESTING_TEAM_ID);
//        faa.setAuctionStatus(EventStatus.REQUESTED);
//        faa.setSeason(LeagueUtil.SEASON);
//
//        FreeAgentAuction freeAgentAuction = service.requestFreeAgentAuction(REQUESTING_TEAM_ID, null, EXISTING_PLAYER_NAME);
//        assertEquals(faa, freeAgentAuction);
//        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
//    }
//
//    @Test
//    public void test_requestFreeAgentAuction_playerName_doesNotExist() {
//        FreeAgentAuction faa = new FreeAgentAuction();
//        faa.setRequestedPlayerName(NEW_PLAYER_NAME);
//        faa.setRequestingTeamId(REQUESTING_TEAM_ID);
//        faa.setAuctionStatus(EventStatus.REQUESTED);
//        faa.setSeason(LeagueUtil.SEASON);
//
//        FreeAgentAuction freeAgentAuction = service.requestFreeAgentAuction(REQUESTING_TEAM_ID, null, NEW_PLAYER_NAME);
//        assertEquals(faa, freeAgentAuction);
//        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
//    }
//
//    @Test
//    public void test_updateFreeAgentAuctionPlayerId() {
//        FreeAgentAuction faa = new FreeAgentAuction();
//        faa.setRequestedPlayerName(NEW_PLAYER_NAME);
//        faa.setPlayerId(PLAYER_ID);
//
//        FreeAgentAuction freeAgentAuction = service.updateFreeAgentAuctionPlayerId(EXISTING_FREE_AGENT_AUCTION_ID, PLAYER_ID);
//        assertEquals(faa, freeAgentAuction);
//        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
//    }
//
//    @Test
//    public void test_startFreeAgentAuction() {
//        FreeAgentAuction freeAgentAuction = service.startFreeAgentAuction(EXISTING_FREE_AGENT_AUCTION_ID);
//        assertEquals(EventStatus.IN_PROGRESS, freeAgentAuction.getAuctionStatus());
//        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
//    }
//
//    @Test
//    public void test_denyFreeAgentAuctionRequest() {
//        FreeAgentAuction freeAgentAuction = service.denyFreeAgentAuctionRequest(EXISTING_FREE_AGENT_AUCTION_ID);
//        assertEquals(EventStatus.DENIED, freeAgentAuction.getAuctionStatus());
//        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
//    }
//
//    @Test
//    public void test_endFreeAgentAuction_NoBids() {
//        when(freeAgentAuctionBidService.getForFreeAgentAuction(EXISTING_FREE_AGENT_AUCTION_ID)).thenReturn(Lists.newArrayList());
//        FreeAgentAuction freeAgentAuction = service.endFreeAgentAuction(EXISTING_FREE_AGENT_AUCTION_ID);
//
//        assertEquals(EventStatus.COMPLETE, freeAgentAuction.getAuctionStatus());
//        assertEquals(0, (long)freeAgentAuction.getPlayerSeason().getTeamId());
//        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
//    }
//
//    @Test
//    public void test_endFreeAgentAuction_Tie() {
//        FreeAgentAuctionBid faab1 = new FreeAgentAuctionBid();
//        faab1.setTeamId(1);
//        faab1.setAmount(100);
//        FreeAgentAuctionBid faab2 = new FreeAgentAuctionBid();
//        faab2.setTeamId(2);
//        faab2.setAmount(100);
//
//        when(freeAgentAuctionBidService.getForFreeAgentAuction(EXISTING_FREE_AGENT_AUCTION_ID)).thenReturn(Lists.newArrayList(faab1, faab2));
//        FreeAgentAuction freeAgentAuction = service.endFreeAgentAuction(EXISTING_FREE_AGENT_AUCTION_ID);
//        assertEquals(EventStatus.COMPLETE, freeAgentAuction.getAuctionStatus());
//        assertNull(freeAgentAuction.getPlayerSeason().getTeamId());
//        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
//    }
//
//    @Test
//    public void test_resolveTiedAuction() {
//        long playerId = 2;
//        long winningTeamId = 1;
//        int startingAmount = 98;
//        int winningBid = 50;
//
//        FreeAgentAuction faa = new FreeAgentAuction();
//        faa.setPlayerId(playerId);
//        faa.setAuctionStatus(EventStatus.COMPLETE);
//        when(freeAgentAuctionService.getById(EXISTING_FREE_AGENT_AUCTION_ID)).thenReturn(faa);
//
//        DraftDollar draftDollar = new DraftDollar();
//        draftDollar.setAmount(startingAmount);
//        draftDollar.setSeason(LeagueUtil.SEASON);
//        draftDollar.setDraftDollarType(DraftDollarType.FREEAGENTAUCTION);
//        when(draftDollarService.getDraftDollarsByTeam(winningTeamId)).thenReturn(Lists.newArrayList(draftDollar));
//
//        when(playerSeasonService.switchTeam(playerId, LeagueUtil.SEASON, null, winningTeamId)).thenAnswer(x -> x.getArguments()[0]);
//
//        FreeAgentAuction freeAgentAuction = service.resolveTiedFreeAgentAuction(EXISTING_FREE_AGENT_AUCTION_ID, winningTeamId, winningBid);
//        assertEquals(EventStatus.COMPLETE, freeAgentAuction.getAuctionStatus());
//        assertEquals(winningTeamId, (long) freeAgentAuction.getWinningTeamId());
//        assertEquals(winningBid, (int) freeAgentAuction.getWinningAmount());
//        assertEquals(winningTeamId, (long)freeAgentAuction.getPlayerSeason().getTeamId());
//
//        draftDollar.setAmount(draftDollar.getAmount() - winningBid);
//
//        verify(draftDollarService, times(1)).upsert(draftDollar);
//        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
//        verify(playerSeasonService, times(1)).switchTeam(playerId, LeagueUtil.SEASON, null, winningTeamId);
//    }
//
//    @Test
//    public void test_endFreeAgentAuction() {
//        long playerId = 2;
//        long winningTeamId = 1;
//        int startingAmount = 98;
//        int winningBid = 50;
//
//        FreeAgentAuction faa = new FreeAgentAuction();
//        faa.setPlayerId(playerId);
//        faa.setAuctionStatus(EventStatus.IN_PROGRESS);
//        when(freeAgentAuctionService.getById(EXISTING_FREE_AGENT_AUCTION_ID)).thenReturn(faa);
//
//        DraftDollar draftDollar = new DraftDollar();
//        draftDollar.setAmount(startingAmount);
//        draftDollar.setSeason(LeagueUtil.SEASON);
//        draftDollar.setDraftDollarType(DraftDollarType.FREEAGENTAUCTION);
//        when(draftDollarService.getDraftDollarsByTeam(winningTeamId)).thenReturn(Lists.newArrayList(draftDollar));
//
//        when(playerSeasonService.switchTeam(playerId, LeagueUtil.SEASON, null, winningTeamId)).thenAnswer(x -> x.getArguments()[0]);
//
//        FreeAgentAuction freeAgentAuction = service.endFreeAgentAuction(EXISTING_FREE_AGENT_AUCTION_ID);
//        assertEquals(EventStatus.COMPLETE, freeAgentAuction.getAuctionStatus());
//        assertEquals(winningTeamId, (long) freeAgentAuction.getWinningTeamId());
//        assertEquals(winningBid, (int) freeAgentAuction.getWinningAmount());
//        assertEquals(winningTeamId, (long)freeAgentAuction.getPlayerSeason().getTeamId());
//
//        draftDollar.setAmount(draftDollar.getAmount() - winningBid);
//
//        verify(draftDollarService, times(1)).upsert(draftDollar);
//        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
//        verify(playerSeasonService, times(1)).switchTeam(playerId, LeagueUtil.SEASON, null, winningTeamId);
//    }
//
//    @Test(expected = IllegalFreeAgentAuctionBidException.class)
//    public void test_bidOnAuction_auctionComplete() {
//        long freeAgentAuctionId = 1;
//        FreeAgentAuction faa = new FreeAgentAuction();
//        faa.setAuctionStatus(EventStatus.COMPLETE);
//        when(freeAgentAuctionService.getById(freeAgentAuctionId)).thenReturn(faa);
//
//        service.bidOnFreeAgentAuction(freeAgentAuctionId, 1, 0);
//    }
//
//    @Test(expected = IllegalFreeAgentAuctionBidException.class)
//    public void test_bidOnAuction_insufficientFunds() {
//        long freeAgentAuctionId = 1;
//        long teamId = 1;
//        int amount = 50;
//        FreeAgentAuction faa = new FreeAgentAuction();
//        faa.setAuctionStatus(EventStatus.COMPLETE);
//        when(freeAgentAuctionService.getById(freeAgentAuctionId)).thenReturn(faa);
//
//        DraftDollar draftDollar = new DraftDollar();
//        draftDollar.setAmount(amount);
//        draftDollar.setSeason(LeagueUtil.SEASON);
//        draftDollar.setDraftDollarType(DraftDollarType.FREEAGENTAUCTION);
//        when(draftDollarService.getDraftDollarsByTeam(teamId)).thenReturn(Lists.newArrayList(draftDollar));
//
//        service.bidOnFreeAgentAuction(freeAgentAuctionId, teamId, amount + 1);
//    }
//
//    @Test
//    public void test_bidOnAuction_existingBid() {
//        long freeAgentAuctionId = 1;
//        long teamId = 1;
//        int amount = 50;
//        int newAmount = amount + 1;
//        FreeAgentAuction faa = new FreeAgentAuction();
//        faa.setAuctionStatus(EventStatus.COMPLETE);
//        when(freeAgentAuctionService.getById(freeAgentAuctionId)).thenReturn(faa);
//
//        DraftDollar draftDollar = new DraftDollar();
//        draftDollar.setAmount(amount);
//        draftDollar.setSeason(LeagueUtil.SEASON);
//        draftDollar.setDraftDollarType(DraftDollarType.FREEAGENTAUCTION);
//        when(draftDollarService.getDraftDollarsByTeam(teamId)).thenReturn(Lists.newArrayList(draftDollar));
//
//        FreeAgentAuctionBid faab = new FreeAgentAuctionBid();
//        faab.setTeamId(teamId);
//        faab.setAmount(amount);
//        faab.setFreeAgentAuctionId(freeAgentAuctionId);
//        when(freeAgentAuctionBidService.getForFreeAgentAuctions(Lists.newArrayList(freeAgentAuctionId))).thenReturn(Lists.newArrayList(faab));
//
//        FreeAgentAuctionBid freeAgentAuctionBid = service.bidOnFreeAgentAuction(freeAgentAuctionId, teamId, newAmount);
//        faab.setAmount(newAmount);
//        assertEquals(faab, freeAgentAuctionBid);
//        verify(freeAgentAuctionBidService, times(1)).upsert(freeAgentAuctionBid);
//        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
//    }
//
//    @Test
//    public void test_bidOnAuction_newBid() {
//        long freeAgentAuctionId = 1;
//        long teamId = 1;
//        int amount = 50;
//        FreeAgentAuction faa = new FreeAgentAuction();
//        faa.setAuctionStatus(EventStatus.COMPLETE);
//        when(freeAgentAuctionService.getById(freeAgentAuctionId)).thenReturn(faa);
//
//        DraftDollar draftDollar = new DraftDollar();
//        draftDollar.setAmount(amount);
//        draftDollar.setSeason(LeagueUtil.SEASON);
//        draftDollar.setDraftDollarType(DraftDollarType.FREEAGENTAUCTION);
//        when(draftDollarService.getDraftDollarsByTeam(teamId)).thenReturn(Lists.newArrayList(draftDollar));
//
//        when(freeAgentAuctionBidService.getForFreeAgentAuctions(Lists.newArrayList(freeAgentAuctionId))).thenReturn(Lists.newArrayList());
//
//        FreeAgentAuctionBid freeAgentAuctionBid = service.bidOnFreeAgentAuction(freeAgentAuctionId, teamId, amount);
//
//        FreeAgentAuctionBid faab = new FreeAgentAuctionBid();
//        faab.setTeamId(teamId);
//        faab.setAmount(amount);
//        faab.setFreeAgentAuctionId(freeAgentAuctionId);
//
//        assertEquals(faab, freeAgentAuctionBid);
//        verify(freeAgentAuctionBidService, times(1)).upsert(freeAgentAuctionBid);
//        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
//    }


}
