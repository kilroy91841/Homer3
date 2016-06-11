package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.data.common.IMinorLeaguePickRepository;
import com.homer.type.MinorLeaguePick;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by arigolub on 3/20/16.
 */
public class MinorLeaguePickServiceTest {

    private IMinorLeaguePickRepository repo;
    private MinorLeaguePickService service;

    private static final int SWAPPED_PICK = 1;
    private static final int ONCE_TRADED_PICK = 2;
    private static final long SWAPPED_PICK_OWNER = 3;
    private static final long TRADED_PICK_OWNER = 4;
    private static final long TO_TEAM = 5;

    @Before
    public void setup() {
        repo = mock(IMinorLeaguePickRepository.class);

        when(repo.get(anyMap())).thenAnswer(x -> {
            Map<String, Object> map = (Map<String, Object>) x.getArguments()[0];
            int round = (int) map.get("round");
            MinorLeaguePick mlp = new MinorLeaguePick();
            if (round == SWAPPED_PICK) {
                mlp.setSwapTeamId(SWAPPED_PICK_OWNER);
            } else if (round == ONCE_TRADED_PICK) {
                mlp.setOwningTeamId(TRADED_PICK_OWNER);
            }
            return mlp;
        });

        service = new MinorLeaguePickService(repo);
    }

    @Test
    public void test_TransferPick() {
        MinorLeaguePick mlp = service.transferPick(TRADED_PICK_OWNER, TO_TEAM, 0, ONCE_TRADED_PICK, LeagueUtil.SEASON);
        assertEquals(TO_TEAM, mlp.getOwningTeamId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_IllegalOwnershipTransfer() {
        service.transferPick(100, TO_TEAM, 0, ONCE_TRADED_PICK, LeagueUtil.SEASON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_IllegalSwapTransfer() {
        service.transferSwapRights(100, TO_TEAM, 0, SWAPPED_PICK, LeagueUtil.SEASON);
    }

    @Test
    public void test_orderPicksForDraft() {
        List<MinorLeaguePick> picks = Lists.newArrayList();
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                picks.add(createPick(i, j));
            }
        }
        when(repo.getMany(anyMap())).thenReturn(picks);

        List<MinorLeaguePick> orderedPicks = service.orderMinorLeaguePicksForDraft(Lists.newArrayList(2L, 1L, 3L), 2016);
        assertEquals(9, orderedPicks.size());
        assertEquals(2, orderedPicks.get(0).getOriginalTeamId());
        assertEquals(1, orderedPicks.get(1).getOriginalTeamId());
        assertEquals(3, orderedPicks.get(2).getOriginalTeamId());
        assertEquals(3, orderedPicks.get(3).getOriginalTeamId());
        assertEquals(1, orderedPicks.get(4).getOriginalTeamId());
        assertEquals(2, orderedPicks.get(5).getOriginalTeamId());
        assertEquals(2, orderedPicks.get(6).getOriginalTeamId());
        assertEquals(1, orderedPicks.get(7).getOriginalTeamId());
        assertEquals(3, orderedPicks.get(8).getOriginalTeamId());
    }

    private static MinorLeaguePick createPick(long teamId, int round) {
        MinorLeaguePick minorLeaguePick = new MinorLeaguePick();
        minorLeaguePick.setOriginalTeamId(teamId);
        minorLeaguePick.setRound(round);
        return minorLeaguePick;
    }
}
