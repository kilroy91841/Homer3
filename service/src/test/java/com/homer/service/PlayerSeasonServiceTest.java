package com.homer.service;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.homer.data.common.IPlayerSeasonRepository;
import com.homer.type.PlayerElf;
import com.homer.type.PlayerSeason;
import com.homer.type.Position;
import com.homer.type.Status;
import com.homer.util.LeagueUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.*;

/**
 * Created by arigolub on 3/18/16.
 */
public class PlayerSeasonServiceTest {

    private PlayerSeasonService service;
    private IPlayerSeasonRepository repo;
    private PlayerSeason playerSeason;

    private static final long PLAYER_ID = 1;
    private static final long PLAYER_ID_2 = 2;
    private static final Long TEAM1 = 100L;
    private static final Long TEAM2 = 101L;
    private static final Long TEAM3 = 102L;

    @Before
    public void setup() {
        repo = mock(IPlayerSeasonRepository.class);

        playerSeason = new PlayerSeason();
        playerSeason.setSeason(LeagueUtil.SEASON);
        playerSeason.setPlayerId(PLAYER_ID);
        playerSeason.setTeamId(TEAM1);
        playerSeason.setFantasyPosition(Position.CATCHER);
        when(repo.getMany(anyMap())).thenReturn(Lists.newArrayList(playerSeason));
        when(repo.upsert(any())).thenAnswer(x -> x.getArguments()[0]);

        service = new PlayerSeasonService(repo, mock(EventBus.class));
    }

    @Test
    public void test_CreatePlayerSeason() {
        when(repo.getMany(anyMap())).thenReturn(Lists.newArrayList());
        PlayerSeason createdPlayerSeason = service.createPlayerSeason(PLAYER_ID_2, LeagueUtil.SEASON);

        PlayerSeason playerSeason = new PlayerSeason();
        playerSeason.setPlayerId(PLAYER_ID_2);
        playerSeason.setSeason(LeagueUtil.SEASON);
        playerSeason.setKeeperSeason(0);
        playerSeason.setSalary(0);
        playerSeason.setIsMinorLeaguer(false);
        playerSeason.setMlbStatus(Status.UNKNOWN);
        playerSeason.setVulturable(false);

        assertEquals(playerSeason, createdPlayerSeason);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_PlayerSeasonAlreadyExists() {
        service.createPlayerSeason(PLAYER_ID, LeagueUtil.SEASON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_NoPlayerId() {
        service.createPlayerSeason(0, LeagueUtil.SEASON);
    }

    @Test
    public void test_SwitchTeam() {
        PlayerSeason playerSeason = new PlayerSeason();
        playerSeason.setPlayerId(PLAYER_ID);
        playerSeason.setSeason(LeagueUtil.SEASON);
        PlayerElf.switchTeam(playerSeason, TEAM2);
        assertEquals(TEAM2, playerSeason.getTeamId());
        assertNotNull(playerSeason.getFantasyPosition());

        PlayerElf.switchTeam(playerSeason, null);
        assertEquals(null, playerSeason.getTeamId());
        assertNull(playerSeason.getFantasyPosition());
    }

    @Test
    public void test_SwitchFantasyPosition() {
        playerSeason.setFantasyPosition(Position.FIRSTBASE);
        PlayerElf.switchFantasyPosition(playerSeason, Position.FIRSTBASE, Position.CATCHER);
        assertEquals(Position.CATCHER, playerSeason.getFantasyPosition());
        assertFalse(playerSeason.getIsMinorLeaguer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_CannotSwitchToExistingPosition() {
        PlayerElf.switchFantasyPosition(playerSeason, Position.CATCHER, Position.CATCHER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_PlayerSwitchedToOtherPosition() {
        playerSeason.setFantasyPosition(Position.THIRDBASE);
        PlayerElf.switchFantasyPosition(playerSeason, Position.CATCHER, Position.SECONDBASE);
    }

    @Test
    public void test_PlayerAlreadySwitchedPosition() {
        playerSeason.setFantasyPosition(Position.THIRDBASE);
        PlayerElf.switchFantasyPosition(playerSeason, Position.CATCHER, Position.THIRDBASE);
        assertEquals(Position.THIRDBASE, playerSeason.getFantasyPosition());
    }

    @Test
    public void test_PlayerMovedFromMinorsToActive() {
        playerSeason.setMlbStatus(Status.ACTIVE);
        playerSeason.setFantasyPosition(Position.MINORLEAGUES);
        playerSeason.setIsMinorLeaguer(true);
        playerSeason.setVulturable(true);
        PlayerElf.switchFantasyPosition(playerSeason, Position.MINORLEAGUES, Position.THIRDBASE);
        assertEquals(Position.THIRDBASE, playerSeason.getFantasyPosition());
        assertFalse(playerSeason.getIsMinorLeaguer());
        assertFalse(playerSeason.getVulturable());
    }

    @Test
    public void test_UpdateVulturable_ActivePlayerNotVulturable() {
        PlayerSeason playerSeason = new PlayerSeason();
        playerSeason.setFantasyPosition(Position.CATCHER);
        playerSeason.setMlbStatus(Status.ACTIVE);
        playerSeason.setVulturable(true);
        PlayerElf.updateVulturable(playerSeason);
        assertFalse(playerSeason.getVulturable());
    }

    @Test
    public void test_UpdateVulturable_DLPlayerNotVulturable() {
        PlayerSeason playerSeason = new PlayerSeason();
        playerSeason.setFantasyPosition(Position.DISABLEDLIST);
        playerSeason.setMlbStatus(Status.DISABLEDLIST);
        playerSeason.setVulturable(true);
        PlayerElf.updateVulturable(playerSeason);
        assertFalse(playerSeason.getVulturable());
    }

    @Test
    public void test_UpdateVulturable_MinorsPlayerNotVulturable() {
        PlayerSeason playerSeason = new PlayerSeason();
        playerSeason.setFantasyPosition(Position.MINORLEAGUES);
        playerSeason.setMlbStatus(Status.MINORS);
        playerSeason.setVulturable(false);
        playerSeason.setVulturable(true);
        PlayerElf.updateVulturable(playerSeason);
        assertFalse(playerSeason.getVulturable());

        playerSeason.setFantasyPosition(Position.MINORLEAGUES);
        playerSeason.setMlbStatus(Status.ACTIVE);
        playerSeason.setIsMinorLeaguer(true);
        playerSeason.setVulturable(true);
        PlayerElf.updateVulturable(playerSeason);
        assertFalse(playerSeason.getVulturable());
    }

    @Test
    public void test_UpdateVulturable_ActivePlayerVulturable() {
        PlayerSeason playerSeason = new PlayerSeason();
        playerSeason.setFantasyPosition(Position.CATCHER);
        playerSeason.setMlbStatus(Status.DISABLEDLIST);
        playerSeason.setVulturable(false);
        PlayerElf.updateVulturable(playerSeason);
        assertTrue(playerSeason.getVulturable());

        playerSeason.setFantasyPosition(Position.CATCHER);
        playerSeason.setMlbStatus(Status.MINORS);
        playerSeason.setVulturable(false);
        PlayerElf.updateVulturable(playerSeason);
        assertTrue(playerSeason.getVulturable());
    }

    @Test
    public void test_UpdateVulturable_DLPlayerVulturable() {
        PlayerSeason playerSeason = new PlayerSeason();
        playerSeason.setFantasyPosition(Position.DISABLEDLIST);
        playerSeason.setMlbStatus(Status.ACTIVE);
        playerSeason.setVulturable(false);
        PlayerElf.updateVulturable(playerSeason);
        assertTrue(playerSeason.getVulturable());
    }

    @Test
    public void test_UpdateVulturable_MinorsPlayerVulturable() {
        PlayerSeason playerSeason = new PlayerSeason();
        playerSeason.setFantasyPosition(Position.MINORLEAGUES);
        playerSeason.setMlbStatus(Status.ACTIVE);
        playerSeason.setIsMinorLeaguer(false);
        playerSeason.setVulturable(false);
        PlayerElf.updateVulturable(playerSeason);
        assertTrue(playerSeason.getVulturable());
    }
}
