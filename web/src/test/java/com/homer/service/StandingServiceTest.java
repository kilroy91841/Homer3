package com.homer.service;

import com.homer.service.utility.ESPNUtility;
import com.homer.type.PlayerDaily;
import com.homer.web.ServiceFactory;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by arigolub on 7/30/16.
 */
public class StandingServiceTest {

    public static void main(String[] args) {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();

        IStandingService standingService = serviceFactory.get(IStandingService.class);

        DateTime date = ESPNUtility.SCORING_PERIOD_1;
//        while (date.isBeforeNow()) {
//            standingService.computeStandingsForDate(date, true);
//            date = date.plusDays(1);
//        }
        //standingService.computeStandingsForDate(DateTime.now(), true);
        List<PlayerDaily> playerDailyList = standingService.getActiveStatsForTeam(1);
        int x = 3;
    }
}
