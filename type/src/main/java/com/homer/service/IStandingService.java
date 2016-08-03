package com.homer.service;

import com.homer.type.Standing;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by arigolub on 7/30/16.
 */
public interface IStandingService extends IIdService<Standing> {

    List<Standing> getByDate(DateTime date);
    List<Standing> sortStandings(List<Standing> standings);
    List<Standing> computeStandingsForDate(DateTime date, boolean refreshTeamDailies);
    List<Standing> getLatestStandings();
    List<Standing> computeStandingsBetweenDates(DateTime start, DateTime end);
}
