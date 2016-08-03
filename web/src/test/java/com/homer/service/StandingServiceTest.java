package com.homer.service;

import com.homer.data.*;
import com.homer.email.aws.AWSEmailService;
import com.homer.external.rest.espn.ESPNRestClient;
import com.homer.external.rest.mlb.MLBRestClient;
import com.homer.service.utility.ESPNUtility;
import org.joda.time.DateTime;

/**
 * Created by arigolub on 7/30/16.
 */
public class StandingServiceTest {

    public static void main(String[] args) {
        TeamService teamService = new TeamService(new TeamRepository());
        PlayerDailyService playerDailyService = new PlayerDailyService(new PlayerDailyRepository(),
                new PlayerService(new PlayerRepository()),
                new ESPNRestClient(), new MLBRestClient(),
                teamService, new AWSEmailService());
        StandingService standingService =
                new StandingService(new StandingRepository(),
                        new TeamDailyService(new TeamDailyRepository(), playerDailyService, teamService));

        DateTime date = ESPNUtility.SCORING_PERIOD_1;
//        while (date.isBeforeNow()) {
//            standingService.computeStandingsForDate(date, true);
//            date = date.plusDays(1);
//        }
        standingService.computeStandingsForDate(DateTime.now(), true);
    }
}
