package com.homer.service;

import com.homer.type.PlayerDaily;
import com.homer.type.ScoringPeriod;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by arigolub on 7/29/16.
 */
public interface IPlayerDailyService extends IIdService<PlayerDaily> {

    List<PlayerDaily> getByDate(int teamId, DateTime date);
    List<PlayerDaily> refreshPlayerDailies();
    List<PlayerDaily> refreshPlayerDailies(ScoringPeriod scoringPeriod);
    List<PlayerDaily> refreshPlayerDailies(int teamId, DateTime date, int scoringPeriodId);
}
