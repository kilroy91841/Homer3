package com.homer.service;

import com.homer.data.PlayerDailyRepository;
import com.homer.data.PlayerRepository;
import com.homer.data.TeamDailyRepository;
import com.homer.data.TeamRepository;
import com.homer.email.aws.AWSEmailService;
import com.homer.external.rest.espn.ESPNRestClient;
import com.homer.external.rest.mlb.MLBRestClient;
import com.homer.service.utility.ESPNUtility;
import com.homer.type.TeamDaily;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by arigolub on 7/30/16.
 */
public class TeamDailyServiceTest {

    private TeamDailyService service;

    @Test
    public void test_refreshTeamDailies() {
        TeamService teamService = new TeamService(new TeamRepository());
        PlayerDailyService playerDailyService =
                new PlayerDailyService(new PlayerDailyRepository(), new PlayerService(new PlayerRepository()),
                        new ESPNRestClient(), new MLBRestClient(), teamService, new AWSEmailService());
        service = new TeamDailyService(new TeamDailyRepository(), playerDailyService, teamService);

        TeamDaily teamDaily = service.refreshTeamDaily(1, DateTime.parse("2016-07-27T12:00:00"), 116);
        assertNotNull(teamDaily);
        assertEquals(44, teamDaily.getAtBats());
        assertEquals(17, teamDaily.getHits());
        assertEquals(4, teamDaily.getRuns());
        assertEquals(1, teamDaily.getHomeRuns());
        assertEquals(7, teamDaily.getRbi());
        assertEquals(6, teamDaily.getWalks());
        assertEquals(1, teamDaily.getStolenBases());

        assertEquals(30.3333, teamDaily.getInningsPitched(), 0.001);
        assertEquals(7, teamDaily.getEarnedRuns());
        assertEquals(26, teamDaily.getPitcherStrikeouts());
        assertEquals(3, teamDaily.getWins());
        assertEquals(25, teamDaily.getPitcherHits());
        assertEquals(7, teamDaily.getPitcherWalks());
        assertEquals(0, teamDaily.getSaves());
    }
}
