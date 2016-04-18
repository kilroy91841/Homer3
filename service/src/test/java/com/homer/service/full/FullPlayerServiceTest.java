package com.homer.service.full;

import com.homer.service.IPlayerSeasonService;
import com.homer.service.IPlayerService;
import com.homer.type.Player;
import com.homer.type.PlayerSeason;
import com.homer.type.view.PlayerView;
import com.homer.util.LeagueUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by arigolub on 4/17/16.
 */
public class FullPlayerServiceTest {

    private FullPlayerService service;
    private IPlayerService playerService;
    private IPlayerSeasonService playerSeasonService;

    @Before
    public void setup() {
        playerService = mock(IPlayerService.class);
        playerSeasonService = mock(IPlayerSeasonService.class);

        when(playerService.createPlayer(any())).thenAnswer(x -> x.getArguments()[0]);
        when(playerSeasonService.createPlayerSeason(1, LeagueUtil.SEASON)).thenReturn(new PlayerSeason());

        service = new FullPlayerService(playerService, playerSeasonService);
    }

    @Test
    public void testCreatePlayer() {
        Player player = new Player();
        player.setId(1);
        PlayerView playerView = service.createPlayer(player);

        assertNotNull(playerView);
        assertEquals(1, playerView.getPlayerSeasons().size());
    }
}
