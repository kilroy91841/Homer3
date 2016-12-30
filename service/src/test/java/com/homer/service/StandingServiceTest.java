package com.homer.service;

import com.google.common.collect.Lists;
import com.homer.data.common.ISeptemberStandingRepository;
import com.homer.data.common.IStandingRepository;
import com.homer.type.Standing;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by arigolub on 7/30/16.
 */
public class StandingServiceTest {

    private StandingService service;

    @Before
    public void setup() {
        service = new StandingService(mock(IStandingRepository.class), mock(ITeamDailyService.class), mock(IPlayerDailyService.class),
                mock(ISeptemberStandingRepository.class), mock(IDraftDollarService.class));
    }

    @Test
    public void test_sortStandings() {
        Standing standing1 = createStanding(1, 768, 211, 709, 101, .3678, 1078, 73, 94, 3.738, 1.287);
        Standing standing2 = createStanding(10, 749, 210, 692, 103, .3480, 938, 57, 70, 3.268, 1.127);
        Standing standing3 = createStanding(11, 706, 201, 704, 85, .3456, 960, 70, 61, 3.106, 1.105);
        Standing standing4 = createStanding(5, 779, 208, 737, 70, .3530, 910, 66, 27, 3.315, 1.206);
        Standing standing5 = createStanding(8, 648, 173, 672, 91, .3287, 886, 58, 63, 3.248, 1.154);
        Standing standing6 = createStanding(9, 640, 209, 620, 70, .3222, 918, 63, 65, 3.895, 1.214);
        Standing standing7 = createStanding(2, 687, 187, 643, 97, .3239, 825, 52, 67, 3.720, 1.254);
        Standing standing8 = createStanding(4, 651, 191, 626, 60, .3240, 867, 52, 41, 3.860, 1.257);
        Standing standing9 = createStanding(12, 596, 128, 557, 96, .3262, 860, 57, 27, 3.725, 1.263);
        Standing standing10 = createStanding(3, 623, 196, 697, 62, .3418, 773, 41, 41, 4.739, 1.413);
        Standing standing11 = createStanding(6, 624, 169, 609, 75, .3245, 820, 56, 43, 4.531, 1.352);
        Standing standing12 = createStanding(7, 610, 168, 662, 47, .3217, 787, 54, 52, 4.276, 1.289);
        List<Standing> standings = Lists.newArrayList(
                standing3,
                standing5,
                standing6,
                standing10,
                standing2,
                standing7,
                standing8,
                standing9,
                standing11,
                standing4,
                standing12,
                standing1
        );
        List<Standing> sortedStandings = service.sortStandings(standings);
        assertEquals(12, sortedStandings.size());
        assertEquals(103, sortedStandings.get(0).getTotalPoints(), 0);
        assertEquals(1, sortedStandings.get(0).getTeamId());
        assertEquals(99.5, sortedStandings.get(1).getTotalPoints(), 0);
        assertEquals(96, sortedStandings.get(2).getTotalPoints(), 0);
        assertEquals(86, sortedStandings.get(3).getTotalPoints(), 0);
        assertEquals(76, sortedStandings.get(4).getTotalPoints(), 0);
        assertEquals(63.5, sortedStandings.get(5).getTotalPoints(), 0);
        assertEquals(62.5, sortedStandings.get(6).getTotalPoints(), 0);
        assertEquals(46, sortedStandings.get(7).getTotalPoints(), 0);
        assertEquals(43, sortedStandings.get(8).getTotalPoints(), 0);
        assertEquals(37.5, sortedStandings.get(9).getTotalPoints(), 0);
        assertEquals(37, sortedStandings.get(10).getTotalPoints(), 0);
        assertEquals(30, sortedStandings.get(11).getTotalPoints(), 0);
    }

    private static Standing createStanding(long teamId, int runTotal, int hrTotal, int rbiTotal, int sbTotal,
                                           double obpTotal, int kTotal, int winTotal, int saveTotal, double eraTotal, double whipTotal) {
        Standing standing = new Standing();
        standing.setTeamId(teamId);
        standing.setRunTotal(runTotal);
        standing.setHrTotal(hrTotal);
        standing.setRbiTotal(rbiTotal);
        standing.setSbTotal(sbTotal);
        standing.setObpTotal(obpTotal);
        standing.setkTotal(kTotal);
        standing.setWinTotal(winTotal);
        standing.setSaveTotal(saveTotal);
        standing.setWhipTotal(whipTotal);
        standing.setEraTotal(eraTotal);
        return standing;
    }
}
