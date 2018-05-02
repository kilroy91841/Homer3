package com.homer.external.rest.mlb;

import com.homer.external.common.mlb.MLBPlayer;
import com.homer.external.common.mlb.Stats;
import com.homer.external.common.mlb.Team;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by arigolub on 4/18/16.
 */
@Ignore
public class MLBRestClientTest {

    private MLBRestClient client;
    private static final long MIKE_TROUT_PLAYER_ID = 545361;
    private static final long CLAYTON_KERSHAW_PLAYER_ID = 477132;
    private static final long JORGE_MATEO_PLAYER_ID = 622761;

    @Before
    public void setup() {
        client = new MLBRestClient();
    }

    @Test
    public void test_playerInfo() {
        MLBPlayer player = client.getPlayer(MIKE_TROUT_PLAYER_ID);
        assertNotNull(player);
        assertEquals("Mike Trout", player.getName());
    }

    @Test
    public void test_stats_batter() {
        Stats stats = client.getStats(MIKE_TROUT_PLAYER_ID, true);
        assertTrue(stats.getGameLog().size() > 0);
    }

    @Test
    public void test_stats_batterNoMajorLeagueExperience() {
        Stats stats = client.getStats(JORGE_MATEO_PLAYER_ID, true);
        assertEquals(0, stats.getGameLog().size());
    }

    @Test
    public void test_stats_pitcher() {
        Stats stats = client.getStats(CLAYTON_KERSHAW_PLAYER_ID, false);
        assertTrue(stats.getGameLog().size() > 0);
    }

    @Test
    public void test_getTeamRecords()
    {
        List<Team> teams = client.getTeamRecords();
        assertEquals(30, teams.size());
    }
}
