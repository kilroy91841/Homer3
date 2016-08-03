package com.homer.service;

import com.homer.type.ScoringPeriod;
import com.homer.type.TeamDaily;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by arigolub on 7/30/16.
 */
public interface ITeamDailyService extends IIdService<TeamDaily> {
    
    List<TeamDaily> getByDate(DateTime date);
    List<TeamDaily> getBetweenDates(DateTime start, DateTime end);
    List<TeamDaily> refreshTeamDailies();
    List<TeamDaily> refreshTeamDailies(ScoringPeriod scoringPeriod);
    TeamDaily refreshTeamDaily(int teamId, DateTime date, int scoringPeriodId);
}
