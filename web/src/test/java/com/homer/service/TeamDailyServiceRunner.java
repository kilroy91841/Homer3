package com.homer.service;

import com.homer.data.PlayerDailyRepository;
import com.homer.data.PlayerRepository;
import com.homer.data.TeamDailyRepository;
import com.homer.data.TeamRepository;
import com.homer.email.aws.AWSEmailService;
import com.homer.external.rest.espn.ESPNRestClient;
import com.homer.external.rest.mlb.MLBRestClient;
import com.homer.service.utility.ESPNUtility;
import org.joda.time.DateTime;

/**
 * Created by arigolub on 7/30/16.
 */
public class TeamDailyServiceRunner {

    public static void main(String[] args) {
        TeamService teamService = new TeamService(new TeamRepository());
        PlayerDailyService playerDailyService =
                new PlayerDailyService(new PlayerDailyRepository(), new PlayerService(new PlayerRepository()),
                        new ESPNRestClient(), new MLBRestClient(), teamService, new AWSEmailService());
        TeamDailyService service = new TeamDailyService(new TeamDailyRepository(), playerDailyService, teamService);

        //DateTime date = ESPNUtility.SCORING_PERIOD_1;
        DateTime date = DateTime.parse("2016-06-15T12:00:00.000-04:00");
        while (date.isBeforeNow()) {
            System.out.println("Date: " + date.toString());
            service.refreshTeamDailies(ESPNUtility.getScoringPeriod(date));
            date = date.plusDays(1);
        }
    }
}
