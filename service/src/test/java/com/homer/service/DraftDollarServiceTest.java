package com.homer.service;

import com.homer.data.common.IDraftDollarRepository;
import com.homer.type.DraftDollar;
import com.homer.type.DraftDollarType;
import com.homer.util.LeagueUtil;
import com.homer.util.core.Tuple;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by arigolub on 3/20/16.
 */
public class DraftDollarServiceTest {

    private IDraftDollarRepository repo;
    private DraftDollarService service;

    private static long INSUFFICIENT_FUNDS_TEAM = 1;
    private static long FROM_TEAM = 2;
    private static long TO_TEAM = 3;
    private static int FROM_STARTING_AMOUNT = 260;
    private static int TO_STARTING_AMOUNT = 245;
    private static int LOW_FUNDS = 5;

    @Before
    public void setup() {
        repo = mock(IDraftDollarRepository.class);

        when(repo.get(anyMap())).thenAnswer(x -> {
            Map<String, Object> filters = (Map<String, Object>) x.getArguments()[0];
            long teamId = (long) filters.get("teamId");
            DraftDollar dd = new DraftDollar();
            dd.setTeamId(teamId);
            if (teamId == FROM_TEAM) {
                dd.setAmount(FROM_STARTING_AMOUNT);
            } else if (teamId == TO_TEAM) {
                dd.setAmount(TO_STARTING_AMOUNT);
            } else if (teamId == INSUFFICIENT_FUNDS_TEAM) {
                dd.setAmount(LOW_FUNDS);
            }
            return dd;
        });

        service = new DraftDollarService(repo);
    }

    @Test
    public void test_SuccessfulTransfer() {
        Tuple<DraftDollar> pair = service.transferMoney(FROM_TEAM, TO_TEAM, LeagueUtil.SEASON, DraftDollarType.MLBAUCTION, 5);
        DraftDollar fromDollar = pair.getLeft();
        DraftDollar toDollar = pair.getRight();
        assertEquals(fromDollar.getAmount(), FROM_STARTING_AMOUNT - 5);
        assertEquals(toDollar.getAmount(), TO_STARTING_AMOUNT + 5);
    }

    //TODO Add test for transferring money that violates dollar limit

    @Test(expected = IllegalArgumentException.class)
    public void test_InsufficientFunds() {
        service.transferMoney(INSUFFICIENT_FUNDS_TEAM, TO_TEAM, LeagueUtil.SEASON, DraftDollarType.MLBAUCTION, 100);
    }
}
