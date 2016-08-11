package com.homer.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.data.common.IKeeperRepository;
import com.homer.exception.KeeperException;
import com.homer.type.*;
import com.homer.util.LeagueUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by arigolub on 8/15/16.
 */
public class KeeperServiceTest {

    private IKeeperService service;

    private IKeeperRepository repo;
    private IDraftDollarService draftDollarService;
    private IPlayerSeasonService playerSeasonService;

    private static final long TEAM_1 = 1;
    private static final int TEAM_1_AMOUNT = 280;

    @Before
    public void setup() {
        repo = mock(IKeeperRepository.class);
        draftDollarService = mock(IDraftDollarService.class);
        playerSeasonService = mock(IPlayerSeasonService.class);

        DraftDollar dd1 = new DraftDollar();
        dd1.setSeason(LeagueUtil.SEASON);
        dd1.setTeamId(TEAM_1);
        dd1.setDraftDollarType(DraftDollarType.MLBAUCTION);
        dd1.setAmount(220);

        DraftDollar dd2 = new DraftDollar();
        dd2.setSeason(LeagueUtil.NEXT_SEASON);
        dd2.setTeamId(TEAM_1);
        dd2.setDraftDollarType(DraftDollarType.MLBAUCTION);
        dd2.setAmount(TEAM_1_AMOUNT);

        DraftDollar dd3 = new DraftDollar();
        dd3.setSeason(LeagueUtil.NEXT_SEASON);
        dd3.setTeamId(TEAM_1);
        dd3.setDraftDollarType(DraftDollarType.FREEAGENTAUCTION);
        dd3.setAmount(100);

        when(draftDollarService.getDraftDollarsByTeam(TEAM_1)).thenReturn(Lists.newArrayList(dd1, dd2, dd3));

        service = new KeeperService(repo, draftDollarService, playerSeasonService);
    }

    @Test
    public void test_AllValid() {
        when(repo.getForTeam(TEAM_1, LeagueUtil.NEXT_SEASON)).thenReturn(Lists.newArrayList());

        List<Keeper> keepers = Lists.newArrayList();
        List<Long> playerIds = Lists.newArrayList();
        Map<Long, PlayerSeason> playerSeasons = Maps.newHashMap();
        keepers.add(setupKeeper(1, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(2, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(3, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(4, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(5, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(6, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(7, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(8, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(9, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(10, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(11, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(12, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(13, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(14, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(15, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(16, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(17, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(18, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(19, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(20, 0, true, 0, playerIds, playerSeasons));

        when(playerSeasonService.getCurrentPlayerSeasons(playerIds)).thenReturn(playerSeasons);

        List<Keeper> savedKeepers = service.replaceKeepers(keepers, TEAM_1);

        assertEquals(20, savedKeepers.size());
        for(Keeper keeper : keepers) {
            if (!keeper.getIsMinorLeaguer()) {
                assertEquals(4, keeper.getSalary());
                assertEquals(1, keeper.getKeeperSeason());
            } else {
                assertEquals(0, keeper.getSalary());
                assertEquals(0, keeper.getKeeperSeason());
            }
        }
    }

    @Test
    public void test_DeletePreexisting() {
        Map<Long, PlayerSeason> playerSeasons = Maps.newHashMap();
        List<Long> playerIds = Lists.newArrayList();

        Keeper existingKeeper1 = setupKeeper(1, 1, false, 0, playerIds, playerSeasons);
        existingKeeper1.setId(100);
        Keeper existingKeeper2 = setupKeeper(2, 1, false, 0, Lists.newArrayList(), playerSeasons);
        existingKeeper2.setId(101);
        when(repo.getForTeam(TEAM_1, LeagueUtil.NEXT_SEASON)).thenReturn(Lists.newArrayList(existingKeeper1, existingKeeper2));


        Keeper newKeeper1 = setupKeeper(3, 1, false, 0, playerIds, playerSeasons);
        Keeper newKeeper2 = setupKeeper(4, 1, false, 0, playerIds, playerSeasons);

        when(playerSeasonService.getCurrentPlayerSeasons(playerIds)).thenReturn(playerSeasons);

        List<Keeper> keepers = Lists.newArrayList();
        keepers.add(existingKeeper1);
        keepers.add(newKeeper1);
        keepers.add(newKeeper2);

        List<Keeper> savedKeepers = service.replaceKeepers(keepers, TEAM_1);

        assertEquals(3, savedKeepers.size());
        verify(repo, times(1)).delete(100);
        verify(repo, times(1)).delete(101);
        verify(repo, times(1)).upsert(existingKeeper1);
        verify(repo, times(1)).upsert(newKeeper1);
        verify(repo, times(1)).upsert(newKeeper2);
    }

    @Test(expected = KeeperException.MajorLeagueCountExceeded.class)
    public void test_tooManyMajorLeaguers() {
        when(repo.getForTeam(TEAM_1, LeagueUtil.NEXT_SEASON)).thenReturn(Lists.newArrayList());

        List<Keeper> keepers = Lists.newArrayList();
        List<Long> playerIds = Lists.newArrayList();
        Map<Long, PlayerSeason> playerSeasons = Maps.newHashMap();
        keepers.add(setupKeeper(1, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(2, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(3, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(4, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(5, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(6, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(7, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(8, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(9, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(10, 1, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(11, 1, false, 0, playerIds, playerSeasons));

        when(playerSeasonService.getCurrentPlayerSeasons(playerIds)).thenReturn(playerSeasons);

        service.replaceKeepers(keepers, TEAM_1);
    }

    @Test(expected = KeeperException.MinorLeagueCountExceeded.class)
    public void test_tooManyMinorLeaguers() {
        when(repo.getForTeam(TEAM_1, LeagueUtil.NEXT_SEASON)).thenReturn(Lists.newArrayList());

        List<Long> playerIds = Lists.newArrayList();
        Map<Long, PlayerSeason> playerSeasons = Maps.newHashMap();
        List<Keeper> keepers = Lists.newArrayList();
        keepers.add(setupKeeper(11, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(12, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(13, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(14, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(15, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(16, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(17, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(18, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(19, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(20, 0, true, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(21, 0, true, 0, playerIds, playerSeasons));

        when(playerSeasonService.getCurrentPlayerSeasons(playerIds)).thenReturn(playerSeasons);

        service.replaceKeepers(keepers, TEAM_1);
    }

    @Test(expected = KeeperException.InsufficientFunds.class)
    public void test_notEnoughSalary() {
        when(repo.getForTeam(TEAM_1, LeagueUtil.NEXT_SEASON)).thenReturn(Lists.newArrayList());

        List<Long> playerIds = Lists.newArrayList();
        Map<Long, PlayerSeason> playerSeasons = Maps.newHashMap();
        List<Keeper> keepers = Lists.newArrayList();
        keepers.add(setupKeeper(1, TEAM_1_AMOUNT - 15, false, 0, playerIds, playerSeasons));

        when(playerSeasonService.getCurrentPlayerSeasons(playerIds)).thenReturn(playerSeasons);

        service.replaceKeepers(keepers, TEAM_1);
    }

    @Test(expected = RuntimeException.class)
    public void test_playerWithoutKeeperYearsRemaining() {
        when(repo.getForTeam(TEAM_1, LeagueUtil.NEXT_SEASON)).thenReturn(Lists.newArrayList());

        List<Long> playerIds = Lists.newArrayList();
        Map<Long, PlayerSeason> playerSeasons = Maps.newHashMap();
        List<Keeper> keepers = Lists.newArrayList();
        keepers.add(setupKeeper(1, 100, false, 0, playerIds, playerSeasons));
        keepers.add(setupKeeper(2, 100, false, 3, playerIds, playerSeasons));

        when(playerSeasonService.getCurrentPlayerSeasons(playerIds)).thenReturn(playerSeasons);

        service.replaceKeepers(keepers, TEAM_1);
    }

    // region helpers

    private Keeper setupKeeper(long playerId, int salary, boolean minorLeaguer, int keeperSeason,
                               List<Long> playerIds, Map<Long, PlayerSeason> playerSeasons) {
        Keeper keeper = getKeeper(playerId, minorLeaguer);
        playerSeasons.put(playerId, getPlayerSeason(playerId, keeperSeason, salary, minorLeaguer));
        playerIds.add(playerId);
        return keeper;
    }

    private static Keeper getKeeper(long playerId, boolean minorLeaguer) {
        Keeper keeper = new Keeper();
        keeper.setPlayerId(playerId);
        keeper.setTeamId(TEAM_1);
        keeper.setSeason(LeagueUtil.SEASON);
        keeper.setIsMinorLeaguer(minorLeaguer);
        return keeper;
    }

    private static PlayerSeason getPlayerSeason(long playerId, int keeperSeason, int salary, boolean minorLeaguer) {
        PlayerSeason playerSeason = new PlayerSeason();
        playerSeason.setPlayerId(playerId);
        playerSeason.setKeeperSeason(keeperSeason);
        playerSeason.setSalary(salary);
        playerSeason.setIsMinorLeaguer(minorLeaguer);
        return playerSeason;
    }

    // endregion
}
