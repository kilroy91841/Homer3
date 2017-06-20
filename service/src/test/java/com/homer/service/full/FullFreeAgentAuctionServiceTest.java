package com.homer.service.full;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.email.EmailRequest;

import com.homer.email.IEmailService;
import com.homer.exception.FreeAgentAuctionBidException;
import com.homer.external.common.IMLBClient;
import com.homer.external.common.mlb.MLBPlayer;
import com.homer.service.*;
import com.homer.service.auth.IUserService;
import com.homer.service.schedule.Scheduler;
import com.homer.type.*;
import com.homer.type.view.FreeAgentAuctionView;
import com.homer.type.view.PlayerView;
import com.homer.util.LeagueUtil;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

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
    private IMLBClient mlbClient;
    private IFullPlayerService fullPlayerService;
    private ITeamService teamService;

    private static final Long PLAYER_ID = 1L;
    private static final Long MLB_PLAYER_ID = 123456L;
    private static final Long NEW_PLAYER_ID = 23L;
    private static final long REQUESTING_TEAM_ID = 2;
    private static final long EXISTING_FREE_AGENT_AUCTION_ID = 3L;

    @Before
    public void setup() {
        freeAgentAuctionService = mock(IFreeAgentAuctionService.class);
        freeAgentAuctionBidService = mock(IFreeAgentAuctionBidService.class);
        playerSeasonService = mock(IPlayerSeasonService.class);
        playerService = mock(IPlayerService.class);
        emailService = mock(IEmailService.class);
        draftDollarService = mock(IDraftDollarService.class);
        mlbClient = mock(IMLBClient.class);
        fullPlayerService = mock(IFullPlayerService.class);
        teamService = mock(ITeamService.class);
        IUserService userService = mock(IUserService.class);

        Player player = new Player();
        player.setId(PLAYER_ID);
        when(playerService.getById(PLAYER_ID)).thenReturn(player);

        FreeAgentAuction faa = new FreeAgentAuction();
        faa.setAuctionStatus(EventStatus.REQUESTED);
        when(freeAgentAuctionService.getById(EXISTING_FREE_AGENT_AUCTION_ID)).thenReturn(faa);

        when(freeAgentAuctionService.upsert(any(FreeAgentAuction.class))).thenAnswer(x -> x.getArguments()[0]);
        when(freeAgentAuctionBidService.upsert(any(FreeAgentAuctionBid.class))).thenAnswer(x -> x.getArguments()[0]);
        when(playerSeasonService.upsert(any(PlayerSeason.class))).thenAnswer(x -> x.getArguments()[0]);
        when(playerSeasonService.getCurrentPlayerSeason(PLAYER_ID)).thenReturn(new PlayerSeason());
        when(playerService.upsert(any(Player.class))).thenAnswer(x -> x.getArguments()[0]);
        when(playerService.getPlayerByMLBPlayerId(any(Long.class))).thenAnswer(x -> {
            long mlbPlayerId = (Long)x.getArguments()[0];
            Player player1 = new Player();
            if (mlbPlayerId == MLB_PLAYER_ID) {
                player1.setId(PLAYER_ID);
                return player1;
            }
            return null;
        });
        when(draftDollarService.upsert(any(DraftDollar.class))).thenAnswer(x -> x.getArguments()[0]);
        when(fullPlayerService.createPlayer(any(Player.class))).thenAnswer(x -> {
            PlayerView player1 = PlayerView.from((Player)x.getArguments()[0]);
            player1.setId(NEW_PLAYER_ID);
            return player1;
        });
        when(mlbClient.getPlayer(any(Long.class))).thenAnswer(x -> {
            long mlbPlayerId = (Long)x.getArguments()[0];
            if (mlbPlayerId == MLB_PLAYER_ID) {
                MLBPlayer mlbPlayer = new MLBPlayer();
                mlbPlayer.setFirstName("Mike");
                mlbPlayer.setLastName("Trout");
                mlbPlayer.setPositionId(Position.OUTFIELD.getId());
                mlbPlayer.setTeamId(MLBTeam.LOSANGELESANGELS.getId());
                return mlbPlayer;
            }
            return null;
        });
        when(playerService.getById(anyLong())).thenReturn(new Player());
        Map<Long, Team> teamMap = Maps.newHashMap();
        Team team = new Team();
        team.setName("Team1");
        teamMap.put(1L, team);
        teamMap.put(2L, team);
        when(teamService.getFantasyTeamMap()).thenReturn(teamMap);

        service = new FullFreeAgentAuctionService(freeAgentAuctionService, freeAgentAuctionBidService, playerSeasonService,
                playerService, fullPlayerService, emailService, draftDollarService, mlbClient, teamService, new Scheduler(),
                userService);
    }

    @Test
    public void test_requestFreeAgentAuction_mlbPlayerId() throws FreeAgentAuctionBidException {
        FreeAgentAuction faa = new FreeAgentAuction();
        faa.setPlayerId(PLAYER_ID);
        faa.setRequestingTeamId(REQUESTING_TEAM_ID);
        faa.setAuctionStatus(EventStatus.REQUESTED);
        faa.setSeason(LeagueUtil.SEASON);

        Player player = new Player();
        player.setMlbPlayerId(MLB_PLAYER_ID);

        FreeAgentAuctionView freeAgentAuctionView = new FreeAgentAuctionView();
        freeAgentAuctionView.setRequestingTeamId(faa.getRequestingTeamId());
        freeAgentAuctionView.setPlayer(player);

        FreeAgentAuctionView freeAgentAuction = service.requestFreeAgentAuction(freeAgentAuctionView);
        assertEquals(FreeAgentAuctionView.from(faa), freeAgentAuction);
        verify(freeAgentAuctionService, times(1)).upsert(faa);
        verifyZeroInteractions(fullPlayerService);
        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
    }

    @Test(expected = RuntimeException.class)
    public void test_requestFreeAgentAuction_badMlbPlayerId() throws FreeAgentAuctionBidException {
        FreeAgentAuction faa = new FreeAgentAuction();
        faa.setPlayerId(PLAYER_ID);
        faa.setRequestingTeamId(REQUESTING_TEAM_ID);
        faa.setAuctionStatus(EventStatus.REQUESTED);
        faa.setSeason(LeagueUtil.SEASON);

        Player player = new Player();
        player.setMlbPlayerId(MLB_PLAYER_ID + 1);

        FreeAgentAuctionView freeAgentAuctionView = new FreeAgentAuctionView();
        freeAgentAuctionView.setRequestingTeamId(faa.getRequestingTeamId());
        freeAgentAuctionView.setPlayer(player);

        service.requestFreeAgentAuction(freeAgentAuctionView);
    }

    @Test
    public void test_startFreeAgentAuction() {
        FreeAgentAuction freeAgentAuction = service.startFreeAgentAuction(EXISTING_FREE_AGENT_AUCTION_ID);
        assertEquals(EventStatus.IN_PROGRESS, freeAgentAuction.getAuctionStatus());
        verify(freeAgentAuctionService, times(1)).upsert(freeAgentAuction);
        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
    }

    @Test
    public void test_denyFreeAgentAuctionRequest() {
        String denyReason = "Denial Reason";
        FreeAgentAuction freeAgentAuction = service.denyFreeAgentAuctionRequest(EXISTING_FREE_AGENT_AUCTION_ID, denyReason);
        assertEquals(EventStatus.DENIED, freeAgentAuction.getAuctionStatus());
        assertEquals(denyReason, freeAgentAuction.getDenyReason());
        verify(freeAgentAuctionService, times(1)).upsert(freeAgentAuction);
        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
    }

    @Test
    public void test_endFreeAgentAuction_NoBids() throws FreeAgentAuctionBidException {
        FreeAgentAuction faa = new FreeAgentAuction();
        faa.setId(1);
        faa.setAuctionStatus(EventStatus.IN_PROGRESS);
        faa.setDeadlineUTC(DateTime.now().plusHours(3));
        when(freeAgentAuctionService.getById(faa.getId())).thenReturn(faa);
        when(freeAgentAuctionBidService.getForFreeAgentAuction(faa.getId())).thenReturn(Lists.newArrayList());
        FreeAgentAuction freeAgentAuction = service.endFreeAgentAuction(faa.getId());

        assertEquals(EventStatus.COMPLETE, freeAgentAuction.getAuctionStatus());
        assertNull(freeAgentAuction.getWinningTeamId());
        verifyZeroInteractions(playerSeasonService);
        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
    }

    @Test
    public void test_endFreeAgentAuction_Tie() throws FreeAgentAuctionBidException {
        FreeAgentAuctionBid faab1 = new FreeAgentAuctionBid();
        faab1.setTeamId(1);
        faab1.setAmount(100);
        FreeAgentAuctionBid faab2 = new FreeAgentAuctionBid();
        faab2.setTeamId(2);
        faab2.setAmount(100);

        FreeAgentAuction faa = new FreeAgentAuction();
        faa.setId(1);
        faa.setAuctionStatus(EventStatus.IN_PROGRESS);
        faa.setDeadlineUTC(DateTime.now().plusHours(3));
        when(freeAgentAuctionService.getById(faa.getId())).thenReturn(faa);
        when(freeAgentAuctionBidService.getForFreeAgentAuction(faa.getId())).thenReturn(Lists.newArrayList(faab1, faab2));
        FreeAgentAuction freeAgentAuction = service.endFreeAgentAuction(faa.getId());
        assertEquals(EventStatus.COMPLETE, freeAgentAuction.getAuctionStatus());
        assertNull(freeAgentAuction.getWinningTeamId());
        assertNull(freeAgentAuction.getWinningAmount());
        verify(freeAgentAuctionService, times(1)).upsert(freeAgentAuction);
        verifyZeroInteractions(playerSeasonService);
        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
    }

    @Test
    public void test_resolveTiedAuction() {
        long playerId = 2;
        long winningTeamId = 1;
        int startingAmount = 98;
        int winningBid = 50;

        FreeAgentAuction faa = new FreeAgentAuction();
        faa.setId(1);
        faa.setPlayerId(playerId);
        when(freeAgentAuctionService.getById(faa.getId())).thenReturn(faa);

        DraftDollar draftDollar = new DraftDollar();
        draftDollar.setAmount(startingAmount);
        draftDollar.setSeason(LeagueUtil.SEASON);
        draftDollar.setDraftDollarType(DraftDollarType.FREEAGENTAUCTION);
        when(draftDollarService.getDraftDollarsByTeam(winningTeamId)).thenReturn(Lists.newArrayList(draftDollar));

        FreeAgentAuctionBid faab1 = new FreeAgentAuctionBid();
        faab1.setId(5);
        faab1.setTeamId(winningTeamId);
        faab1.setAmount(winningBid);
        FreeAgentAuctionBid faab2 = new FreeAgentAuctionBid();
        faab2.setTeamId(2);
        faab2.setAmount(winningBid - 1);

        when(freeAgentAuctionBidService.getForFreeAgentAuction(faa.getId())).thenReturn(Lists.newArrayList(faab1, faab2));

        FreeAgentAuction freeAgentAuction = service.setWinningBid(faa.getId(), faab1.getTeamId());
        assertEquals(EventStatus.COMPLETE, freeAgentAuction.getAuctionStatus());
        assertEquals(winningTeamId, (long) freeAgentAuction.getWinningTeamId());
        assertEquals(winningBid, (int) freeAgentAuction.getWinningAmount());

        draftDollar.setAmount(draftDollar.getAmount() - winningBid);

        verify(draftDollarService, times(1)).upsert(draftDollar);
        verify(freeAgentAuctionService, times(1)).upsert(freeAgentAuction);
        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
        verify(playerSeasonService, times(1)).switchTeam(playerId, LeagueUtil.SEASON, null, winningTeamId);
        verify(playerSeasonService, times(1)).upsert(any(PlayerSeason.class));
    }

    @Test
    public void test_endFreeAgentAuction() throws FreeAgentAuctionBidException {
        long playerId = 2;
        long winningTeamId = 1;
        int startingAmount = 98;
        int winningBid = 50;

        FreeAgentAuction faa = new FreeAgentAuction();
        faa.setId(1);
        faa.setPlayerId(playerId);
        faa.setAuctionStatus(EventStatus.IN_PROGRESS);
        faa.setDeadlineUTC(DateTime.now().plusHours(3));
        when(freeAgentAuctionService.getById(faa.getId())).thenReturn(faa);

        FreeAgentAuctionBid faab1 = new FreeAgentAuctionBid();
        faab1.setTeamId(winningTeamId);
        faab1.setAmount(winningBid);
        FreeAgentAuctionBid faab2 = new FreeAgentAuctionBid();
        faab2.setTeamId(2);
        faab2.setAmount(winningBid - 1);

        when(freeAgentAuctionBidService.getForFreeAgentAuction(faa.getId())).thenReturn(Lists.newArrayList(faab1, faab2));

        DraftDollar draftDollar = new DraftDollar();
        draftDollar.setAmount(startingAmount);
        draftDollar.setSeason(LeagueUtil.SEASON);
        draftDollar.setDraftDollarType(DraftDollarType.FREEAGENTAUCTION);
        when(draftDollarService.getDraftDollarsByTeam(winningTeamId)).thenReturn(Lists.newArrayList(draftDollar));

        FreeAgentAuction freeAgentAuction = service.endFreeAgentAuction(faa.getId());
        assertEquals(EventStatus.COMPLETE, freeAgentAuction.getAuctionStatus());
        assertEquals(winningTeamId, (long) freeAgentAuction.getWinningTeamId());
        assertEquals(winningBid, (int) freeAgentAuction.getWinningAmount());

        draftDollar.setAmount(draftDollar.getAmount() - winningBid);

        verify(draftDollarService, times(1)).upsert(draftDollar);
        verify(freeAgentAuctionService, times(1)).upsert(freeAgentAuction);
        verify(emailService, times(1)).sendEmail(any(EmailRequest.class));
        verify(playerSeasonService, times(1)).switchTeam(playerId, LeagueUtil.SEASON, null, winningTeamId);
        verify(playerSeasonService, times(1)).upsert(any(PlayerSeason.class));
    }

    @Test(expected = FreeAgentAuctionBidException.AuctionNotInProgress.class)
    public void test_bidOnAuction_auctionComplete() throws FreeAgentAuctionBidException {
        long freeAgentAuctionId = 1;
        FreeAgentAuction faa = new FreeAgentAuction();
        faa.setAuctionStatus(EventStatus.COMPLETE);
        faa.setDeadlineUTC(DateTime.now().plusHours(3));
        when(freeAgentAuctionService.getById(freeAgentAuctionId)).thenReturn(faa);

        service.bidOnFreeAgentAuction(freeAgentAuctionId, 1, 0);
    }

    @Test(expected = FreeAgentAuctionBidException.InsufficientFunds.class)
    public void test_bidOnAuction_insufficientFunds() throws FreeAgentAuctionBidException {
        long freeAgentAuctionId = 1;
        long teamId = 1;
        int amount = 50;
        FreeAgentAuction faa = new FreeAgentAuction();
        faa.setAuctionStatus(EventStatus.IN_PROGRESS);
        faa.setDeadlineUTC(DateTime.now().plusHours(3));
        when(freeAgentAuctionService.getById(freeAgentAuctionId)).thenReturn(faa);

        DraftDollar draftDollar = new DraftDollar();
        draftDollar.setAmount(amount);
        draftDollar.setSeason(LeagueUtil.SEASON);
        draftDollar.setDraftDollarType(DraftDollarType.FREEAGENTAUCTION);
        when(draftDollarService.getDraftDollarsByTeam(teamId)).thenReturn(Lists.newArrayList(draftDollar));

        service.bidOnFreeAgentAuction(freeAgentAuctionId, teamId, amount + 1);
    }

    @Test
    public void test_bidOnAuction_existingBid() throws FreeAgentAuctionBidException {
        long freeAgentAuctionId = 1;
        long teamId = 1;
        int amount = 50;
        int newAmount = amount + 1;
        FreeAgentAuction faa = new FreeAgentAuction();
        faa.setAuctionStatus(EventStatus.IN_PROGRESS);
        faa.setDeadlineUTC(DateTime.now().plusHours(3));
        when(freeAgentAuctionService.getById(freeAgentAuctionId)).thenReturn(faa);

        DraftDollar draftDollar = new DraftDollar();
        draftDollar.setAmount(75);
        draftDollar.setSeason(LeagueUtil.SEASON);
        draftDollar.setDraftDollarType(DraftDollarType.FREEAGENTAUCTION);
        when(draftDollarService.getDraftDollarsByTeam(teamId)).thenReturn(Lists.newArrayList(draftDollar));

        FreeAgentAuctionBid faab = new FreeAgentAuctionBid();
        faab.setTeamId(teamId);
        faab.setAmount(amount);
        faab.setFreeAgentAuctionId(freeAgentAuctionId);
        when(freeAgentAuctionBidService.getForFreeAgentAuctions(Lists.newArrayList(freeAgentAuctionId))).thenReturn(Lists.newArrayList(faab));

        FreeAgentAuctionBid freeAgentAuctionBid = service.bidOnFreeAgentAuction(freeAgentAuctionId, teamId, newAmount);
        faab.setAmount(newAmount);
        assertEquals(faab, freeAgentAuctionBid);
        verify(freeAgentAuctionBidService, times(1)).upsert(freeAgentAuctionBid);
    }

    @Test
    public void test_bidOnAuction_newBid() throws FreeAgentAuctionBidException {
        long freeAgentAuctionId = 1;
        long teamId = 1;
        int amount = 50;
        FreeAgentAuction faa = new FreeAgentAuction();
        faa.setAuctionStatus(EventStatus.IN_PROGRESS);
        faa.setDeadlineUTC(DateTime.now().plusHours(3));
        when(freeAgentAuctionService.getById(freeAgentAuctionId)).thenReturn(faa);

        DraftDollar draftDollar = new DraftDollar();
        draftDollar.setAmount(amount);
        draftDollar.setSeason(LeagueUtil.SEASON);
        draftDollar.setDraftDollarType(DraftDollarType.FREEAGENTAUCTION);
        when(draftDollarService.getDraftDollarsByTeam(teamId)).thenReturn(Lists.newArrayList(draftDollar));

        when(freeAgentAuctionBidService.getForFreeAgentAuctions(Lists.newArrayList(freeAgentAuctionId))).thenReturn(Lists.newArrayList());

        FreeAgentAuctionBid freeAgentAuctionBid = service.bidOnFreeAgentAuction(freeAgentAuctionId, teamId, amount);

        FreeAgentAuctionBid faab = new FreeAgentAuctionBid();
        faab.setTeamId(teamId);
        faab.setAmount(amount);
        faab.setFreeAgentAuctionId(freeAgentAuctionId);

        assertEquals(faab, freeAgentAuctionBid);
        verify(freeAgentAuctionBidService, times(1)).upsert(freeAgentAuctionBid);
    }


}
