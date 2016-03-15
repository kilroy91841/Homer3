package com.homer.service.gather;

import com.google.common.collect.Lists;
import com.homer.service.*;
import com.homer.type.Player;
import com.homer.type.PlayerSeason;
import com.homer.type.Team;
import com.homer.type.view.PlayerView;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by arigolub on 3/17/16.
 */
public class PlayerGathererTest {

    private Gatherer gatherer;
    private Team team1;
    private Team team2;
    private Player player1;
    private Player player2;
    private Player player3;
    private PlayerSeason playerSeason1_2015;
    private PlayerSeason playerSeason1_2016;
    private PlayerSeason playerSeason2_2016;
    private PlayerSeason playerSeason3_2015;

    @Before
    public void setup() {
        IPlayerService playerService = mock(IPlayerService.class);
        IPlayerSeasonService playerSeasonService = mock(IPlayerSeasonService.class);
        ITeamService teamService = mock(ITeamService.class);
        IDraftDollarService draftDollarService = mock(IDraftDollarService.class);
        IMinorLeaguePickService minorLeaguePickService = mock(IMinorLeaguePickService.class);
        ITradeService tradeService = mock(ITradeService.class);
        ITradeElementService tradeElementService = mock(ITradeElementService.class);

        team1 = new Team();
        team1.setId(1);
        team2 = new Team();
        team2.setId(2);

        player1 = new Player();
        player1.setId(1);
        player1.setMlbTeamId(108);
        player2 = new Player();
        player2.setId(2);
        player2.setMlbTeamId(108);
        player3 = new Player();
        player3.setId(3);
        player3.setMlbTeamId(109);
        playerSeason1_2016 = new PlayerSeason();
        playerSeason1_2016.setPlayerId(player1.getId());
        playerSeason1_2016.setSeason(2016);
        playerSeason1_2016.setTeamId(1L);
        playerSeason1_2015 = new PlayerSeason();
        playerSeason1_2015.setPlayerId(player1.getId());
        playerSeason1_2015.setSeason(2015);
        playerSeason1_2015.setTeamId(2L);
        playerSeason2_2016 = new PlayerSeason();
        playerSeason2_2016.setPlayerId(player2.getId());
        playerSeason2_2016.setSeason(2016);
        playerSeason2_2016.setTeamId(2L);
        playerSeason3_2015 = new PlayerSeason();
        playerSeason3_2015.setPlayerId(player3.getId());
        playerSeason3_2015.setSeason(2015);
        playerSeason3_2015.setTeamId(1L);

        when(teamService.getTeams()).thenReturn(Lists.newArrayList(team1, team2));
        when(playerSeasonService.getPlayerSeasons(anyCollectionOf(Long.class))).thenAnswer(x -> {
            Collection<Long> ids = (Collection)x.getArguments()[0];
            List<PlayerSeason> playerSeasons = Lists.newArrayList();
            if (ids.contains(1L)) {
                playerSeasons.add(playerSeason1_2016);
                playerSeasons.add(playerSeason1_2015);
            }
            if (ids.contains(2L)) {
                playerSeasons.add(playerSeason2_2016);
            }
            if (ids.contains(3L)) {
                playerSeasons.add(playerSeason3_2015);
            }
            return playerSeasons;
        });

        gatherer = new Gatherer(playerService, teamService, playerSeasonService, draftDollarService, minorLeaguePickService,
                tradeService, tradeElementService);
    }

    @Test
    public void testGatherPlayers() {
        List<PlayerView> playerViews = gatherer.gatherPlayers(Lists.newArrayList(player1, player2, player3));
        playerViews.forEach(pv -> {
            assertNotNull(pv.getMlbTeam());
            assertNotNull(pv.getPlayerSeasons());
            if (pv.getId() == 3) {
                assertNull(pv.getCurrentSeason());
            } else {
                assertNotNull(pv.getCurrentSeason());
            }
        });
        assertEquals(3, playerViews.size());
    }

    @Test
    public void testGatherTeam() {

    }
}
