package com.homer.service.full;

import com.homer.email.IEmailService;
import com.homer.service.*;
import com.homer.service.auth.IUserService;
import com.homer.service.gather.IGatherer;
import com.homer.type.*;
import com.homer.util.LeagueUtil;
import com.homer.util.core.Tuple;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Created by arigolub on 3/20/16.
 */
public class FullTradeServiceTest {

    private FullTradeService fullTradeService;

    private ITradeElementService tradeElementService;
    private IPlayerSeasonService playerSeasonService;
    private IDraftDollarService draftDollarService;
    private IMinorLeaguePickService minorLeaguePickService;
    private ITradeService tradeService;

    @Before
    public void setup() {
        tradeElementService = mock(ITradeElementService.class);
        playerSeasonService = mock(IPlayerSeasonService.class);
        draftDollarService = mock(IDraftDollarService.class);
        minorLeaguePickService = mock(IMinorLeaguePickService.class);
        tradeService = mock(ITradeService.class);

        setupService(tradeElementService);
        setupService(playerSeasonService);
        setupService(draftDollarService);
        setupService(minorLeaguePickService);
        setupService(tradeService);

        when(draftDollarService.transferMoney(anyLong(), anyLong(), anyInt(), any(DraftDollarType.class), anyInt()))
                .thenReturn(new Tuple<>(new DraftDollar(), new DraftDollar()));
        when(minorLeaguePickService.transferPick(anyLong(), anyLong(), anyLong(), anyInt(), anyInt())).thenReturn(new MinorLeaguePick());
        when(minorLeaguePickService.transferSwapRights(anyLong(), anyLong(), anyLong(), anyInt(), anyInt())).thenReturn(new MinorLeaguePick());

        fullTradeService = new FullTradeService(tradeService, minorLeaguePickService, draftDollarService, playerSeasonService, tradeElementService,
                mock(IGatherer.class), mock(IEmailService.class), mock(IUserService.class));
    }

    private void setupService(IIdService service) {
        when(service.upsert(any())).thenAnswer( x-> x.getArguments()[0]);
    }

    @Test
    public void test_FullTrade() {
        Trade tv = new Trade();
        Team team1 = new Team();
        team1.setId(1);
        Team team2 = new Team();
        team2.setId(2);
        tv.setTeam1(team1);
        tv.setTeam2(team2);

        TradeElement tev = new TradeElement();
        tev.setTeamFrom(team1);
        tev.setTeamTo(team2);
        tev.setPlayerId(1L);
        tv.getTradeElements().add(tev);

        long minorLeaguePickId = 100;
        TradeElement tev1 = new TradeElement();
        tev1.setTeamFrom(team2);
        tev1.setTeamTo(team1);
        tev1.setMinorLeaguePickId(minorLeaguePickId);
        tev1.setSwapTrade(false);
        tv.getTradeElements().add(tev1);

        long draftDollarId = 101;
        TradeElement tev2 = new TradeElement();
        tev2.setTeamFrom(team2);
        tev2.setTeamTo(team1);
        tev2.setDraftDollarAmount(5);
        tev2.setDraftDollarId(draftDollarId);
        tv.getTradeElements().add(tev2);

//        fullTradeService.acceptTrade(tv);
//
//        verify(playerSeasonService, times(1)).switchTeam(anyLong(), anyInt(), anyLong(), anyLong());
//        verify(minorLeaguePickService, times(1)).transferPick(anyLong(), anyLong(), anyLong());
//        verify(draftDollarService, times(1)).transferMoney(anyLong(), anyLong(), anyLong(), anyInt());
//
//        verify(tradeElementService, times(3)).upsert(any(TradeElement.class));
//        verify(tradeService, times(1)).upsert(any(Trade.class));
//        verify(playerSeasonService, times(1)).upsert(any(PlayerSeason.class));
//        verify(minorLeaguePickService, times(1)).upsert(any(MinorLeaguePick.class));
//        verify(draftDollarService, times(2)).upsert(any(DraftDollar.class));
    }
}
