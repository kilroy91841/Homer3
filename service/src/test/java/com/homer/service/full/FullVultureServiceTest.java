package com.homer.service.full;

import com.google.common.collect.Lists;
import com.homer.email.IEmailService;
import com.homer.exception.ExistingVultureInProgressException;
import com.homer.exception.NotVulturableException;
import com.homer.service.IPlayerSeasonService;
import com.homer.service.IPlayerService;
import com.homer.service.ITeamService;
import com.homer.service.IVultureService;
import com.homer.service.auth.IUserService;
import com.homer.type.*;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ScheduledFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by arigolub on 5/7/16.
 */
public class FullVultureServiceTest {

    private FullVultureService service;
    private IVultureService vultureService;
    private IPlayerSeasonService playerSeasonService;

    private static final Long TEAM = 1L;

    private static final Long PLAYER = 99L;
    private static final Long DROP_PLAYER = 98L;
    private static final Long NOT_VULTURABLE_PLAYER = 100L;
    private static final Long EXISTING_VULTURE_PLAYER = 101L;
    private static final Long DROP_PLAYER_HAS_MOVED = 103L;

    private static final Long VULTURE_NOT_VULTURABLE_PLAYER = 102L;
    private static final Long VULTURE_VULTURABLE_PLAYER = 104L;

    @Before
    public void setup() {
        FullVultureService.getInProgressVultureMap().clear();

        playerSeasonService = mock(IPlayerSeasonService.class);
        when(playerSeasonService.getCurrentPlayerSeason(anyLong())).thenAnswer(x -> {
            Long id = (Long)x.getArguments()[0];
            PlayerSeason ps = new PlayerSeason();
            ps.setPlayerId(id);
            ps.setTeamId(TEAM);
            if (NOT_VULTURABLE_PLAYER.equals(id) || DROP_PLAYER.equals(id)) {
                ps.setVulturable(false);
            } else {
                ps.setVulturable(true);
            }
            return ps;
        });
        when(playerSeasonService.switchTeam(any(PlayerSeason.class), any(Long.class), any(Long.class))).thenAnswer(x -> {
            PlayerSeason ps = (PlayerSeason)x.getArguments()[0];
            Long newTeamId = (Long)x.getArguments()[2];
            ps.setTeamId(newTeamId);
            return ps;
        });
        when(playerSeasonService.upsert(any(PlayerSeason.class))).thenAnswer(x -> x.getArguments()[0]);

        vultureService = mock(IVultureService.class);
        when(vultureService.getForPlayer(anyLong(), eq(true))).thenAnswer(x -> {
            Long id = (Long)x.getArguments()[0];
            Vulture vulture = null;
            if (EXISTING_VULTURE_PLAYER.equals(id)) {
                vulture = new Vulture();
                vulture.setId(VULTURE_VULTURABLE_PLAYER);
            }
            return vulture;
        });
        when(vultureService.getById(anyLong())).thenAnswer(x -> {
            Long id = (Long)x.getArguments()[0];
            Vulture vulture = null;
            if (VULTURE_NOT_VULTURABLE_PLAYER.equals(id)) {
                vulture = new Vulture();
                vulture.setId(VULTURE_NOT_VULTURABLE_PLAYER);
                vulture.setPlayerId(NOT_VULTURABLE_PLAYER);
            } else if (VULTURE_VULTURABLE_PLAYER.equals(id)) {
                vulture = new Vulture();
                vulture.setId(VULTURE_VULTURABLE_PLAYER);
                vulture.setTeamId(TEAM);
                vulture.setPlayerId(PLAYER);
                vulture.setDropPlayerId(DROP_PLAYER);
            }
            return vulture;
        });
        when(vultureService.upsert(any(Vulture.class))).thenAnswer(x -> x.getArguments()[0]);

        IUserService userService = mock(IUserService.class);
        when(userService.getUsersForTeam(anyLong())).thenReturn(Lists.newArrayList());

        IPlayerService playerService = mock(IPlayerService.class);
        when(playerService.getById(anyLong())).thenAnswer(x -> {
            Player p = new Player();
            p.setName("");
            return p;
        });

        ITeamService teamService = mock(ITeamService.class);
        when(teamService.getTeamById(anyLong())).then(x -> {
            Team t = new Team();
            t.setName("");
            return t;
        });

        service = new FullVultureService(vultureService, playerSeasonService, teamService, playerService,
                userService, mock(IEmailService.class));
    }

    @Test(expected = NotVulturableException.class)
    public void test_playerNotVulturable() {
        service.createVulture(1, NOT_VULTURABLE_PLAYER, null, false);
    }

    @Test(expected = ExistingVultureInProgressException.class)
    public void test_ExistingVultureInProgress() {
        service.createVulture(1, EXISTING_VULTURE_PLAYER, null, false);
    }

    @Test
    public void test_SuccessfulVulture() {
        Long dropPlayerId = 5L;
        Long teamId = 1L;
        Vulture vulture = service.createVulture(teamId, PLAYER, dropPlayerId, true);
        assertEquals(PLAYER, (Long)vulture.getPlayerId());
        assertEquals(dropPlayerId, vulture.getDropPlayerId());
        assertEquals(teamId, (Long)vulture.getTeamId());
        assertTrue(vulture.getIsCommisionerVulture());
        assertEquals(EventStatus.IN_PROGRESS, vulture.getVultureStatus());
        assertNotNull(vulture.getExpirationDateUTC());

        assertTrue(FullVultureService.getInProgressVultureMap().size() == 1);
    }

    @Test
    public void test_ResolveForPlayerNotVulturable() {
        FullVultureService.getInProgressVultureMap().put(VULTURE_NOT_VULTURABLE_PLAYER, null);

        Vulture resolvedVulture = service.resolveVulture(VULTURE_NOT_VULTURABLE_PLAYER);
        assertEquals(VULTURE_NOT_VULTURABLE_PLAYER, (Long)resolvedVulture.getId());
        assertEquals(EventStatus.FIXED, resolvedVulture.getVultureStatus());

        verify(playerSeasonService, never()).switchTeam(anyLong(), anyInt(), any(), any());

        assertTrue(FullVultureService.getInProgressVultureMap().size() == 0);
    }

    @Test
    public void test_ResolvePlayerVulturable() {
        FullVultureService.getInProgressVultureMap().put(VULTURE_VULTURABLE_PLAYER, null);

        Vulture resolvedVulture = service.resolveVulture(VULTURE_VULTURABLE_PLAYER);
        assertEquals(VULTURE_VULTURABLE_PLAYER, (Long)resolvedVulture.getId());
        assertEquals(EventStatus.SUCCESSFUL, resolvedVulture.getVultureStatus());

        PlayerSeason playerSeason = new PlayerSeason();
        playerSeason.setPlayerId(PLAYER);
        playerSeason.setTeamId(TEAM);
        playerSeason.setVulturable(false);

        PlayerSeason dropPlayerSeason = new PlayerSeason();
        dropPlayerSeason.setPlayerId(DROP_PLAYER);
        dropPlayerSeason.setTeamId(null);

        verify(playerSeasonService, times(1)).upsert(playerSeason);
        verify(playerSeasonService, times(1)).upsert(dropPlayerSeason);

        assertTrue(FullVultureService.getInProgressVultureMap().size() == 0);
    }

    @Test
    public void test_MarkInProgressVultureAsFixed_NoVulture() {
        assertTrue(service.markInProgressVultureForPlayerAsFixed(12345L));
    }

    @Test
    public void test_MarkInProgressVultureAsFixed_VultureExists() {
        ScheduledFuture scheduledFuture = mock(ScheduledFuture.class);
        when(scheduledFuture.isDone()).thenReturn(false);
        when(scheduledFuture.isCancelled()).thenReturn(false);
        when(scheduledFuture.cancel(false)).thenReturn(true);
        FullVultureService.getInProgressVultureMap().put(VULTURE_VULTURABLE_PLAYER, scheduledFuture);

        assertTrue(service.markInProgressVultureForPlayerAsFixed(EXISTING_VULTURE_PLAYER));

        assertTrue(FullVultureService.getInProgressVultureMap().size() == 0);
    }
}
