package com.homer.service;

import com.homer.type.PlayerDaily;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by arigolub on 7/29/16.
 */
public interface IPlayerDailyService extends IIdService<PlayerDaily> {

    List<PlayerDaily> refreshPlayerDailies();
    List<PlayerDaily> refreshPlayerDailies(int teamId, DateTime date, int scoringPeriodId);
}
