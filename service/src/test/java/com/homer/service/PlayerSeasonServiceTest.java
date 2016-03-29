package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.data.common.IPlayerSeasonRepository;
import com.homer.type.PlayerSeason;
import com.homer.type.Position;
import com.homer.util.HomerBeanUtil;
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
        when(repo.getMany(anyMap())).thenReturn(Lists.newArrayList(playerSeason));

        service = new PlayerSeasonService(repo);
    }

    @Test
    public void test_SwitchTeam() {
        PlayerSeason playerSeason = service.switchTeam(PLAYER_ID, LeagueUtil.SEASON, TEAM1, TEAM2);
        assertEquals(TEAM2, playerSeason.getTeamId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_CannotMoveToExistingTeam() {
        service.switchTeam(PLAYER_ID, LeagueUtil.SEASON, TEAM1, TEAM1);
    }

    @Test
    public void test_PlayerAlreadyMoved() {
        PlayerSeason movedPlayerSeason = new PlayerSeason();
        HomerBeanUtil.copyProperties(movedPlayerSeason, playerSeason);
        movedPlayerSeason.setTeamId(TEAM2);
        when(repo.getMany(anyMap())).thenReturn(Lists.newArrayList(movedPlayerSeason));
        PlayerSeason updatedPlayer = service.switchTeam(PLAYER_ID, LeagueUtil.SEASON, TEAM1, TEAM2);
        assertEquals(TEAM2, updatedPlayer.getTeamId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_PlayerMovedToOtherTeam() {
        PlayerSeason movedPlayerSeason = new PlayerSeason();
        HomerBeanUtil.copyProperties(movedPlayerSeason, playerSeason);
        movedPlayerSeason.setTeamId(TEAM3);
        when(repo.getMany(anyMap())).thenReturn(Lists.newArrayList(movedPlayerSeason));
        service.switchTeam(PLAYER_ID, LeagueUtil.SEASON, TEAM1, TEAM2);
    }

    @Test
    public void test_SwitchFantasyPosition() {
        playerSeason.setFantasyPosition(Position.FIRSTBASE);
        PlayerSeason playerSeason = service.switchFantasyPosition(PLAYER_ID, LeagueUtil.SEASON, Position.FIRSTBASE, Position.CATCHER);
        assertEquals(Position.CATCHER, playerSeason.getFantasyPosition());
        assertFalse(playerSeason.getIsMinorLeaguer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_CannotSwitchToExistingPosition() {
        service.switchFantasyPosition(PLAYER_ID, LeagueUtil.SEASON, Position.CATCHER, Position.CATCHER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_PlayerSwitchedToOtherPosition() {
        playerSeason.setFantasyPosition(Position.THIRDBASE);
        service.switchFantasyPosition(PLAYER_ID, LeagueUtil.SEASON, Position.CATCHER, Position.SECONDBASE);
    }

    @Test
    public void test_PlayerAlreadySwitchedPosition() {
        playerSeason.setFantasyPosition(Position.THIRDBASE);
        PlayerSeason updatedPlayerSeason = service.switchFantasyPosition(PLAYER_ID, LeagueUtil.SEASON, Position.CATCHER, Position.THIRDBASE);
        assertEquals(Position.THIRDBASE, updatedPlayerSeason.getFantasyPosition());
    }

    @Test
    public void test_PlayerMovedFromMinorsToActive() {
        playerSeason.setFantasyPosition(Position.MINORLEAGUES);
        playerSeason.setIsMinorLeaguer(true);
        PlayerSeason updatedPlayerSeason = service.switchFantasyPosition(PLAYER_ID, LeagueUtil.SEASON, Position.MINORLEAGUES, Position.THIRDBASE);
        assertEquals(Position.THIRDBASE, updatedPlayerSeason.getFantasyPosition());
        assertFalse(updatedPlayerSeason.getIsMinorLeaguer());
    }
}
