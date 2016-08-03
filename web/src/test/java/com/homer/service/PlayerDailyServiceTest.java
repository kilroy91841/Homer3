package com.homer.service;

import com.homer.data.PlayerDailyRepository;
import com.homer.data.PlayerRepository;
import com.homer.data.TeamRepository;
import com.homer.email.aws.AWSEmailService;
import com.homer.external.rest.espn.ESPNRestClient;
import com.homer.external.rest.mlb.MLBRestClient;
import com.homer.type.PlayerDaily;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by arigolub on 7/29/16.
 */
public class PlayerDailyServiceTest {

    private PlayerDailyService service;

    @Test
    public void test_refreshPlayerDailies() {
        service = new PlayerDailyService(new PlayerDailyRepository(), new PlayerService(new PlayerRepository()),
                new ESPNRestClient(), new MLBRestClient(), new TeamService(new TeamRepository()), new AWSEmailService());
        List<PlayerDaily> list = service.refreshPlayerDailies(1, DateTime.parse("2016-07-29T12:00:00"), 118);
        assertEquals(14, list.size());
    }
}
