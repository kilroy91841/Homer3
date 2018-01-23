package com.homer.external.rest.mlb;

import com.homer.external.common.IMLBClient;
import com.homer.external.common.mlb.*;
import com.homer.external.rest.mlb.parser.JSONPlayerParser;
import com.homer.external.rest.mlb.parser.JSONRosterParser;
import com.homer.external.rest.mlb.parser.JSONStatParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 4/17/16.
 */
public class MLBRestClient implements IMLBClient {

    final static Logger logger = LoggerFactory.getLogger(MLBRestClient.class);

    private static final String URL_PLAYERINFO      = "http://mlb.com/lookup/json/named.player_info.bam";
    private static final String URL_ROSTER          = "http://mlb.mlb.com/lookup/json/named.roster_40.bam";
    private static final String URL_BATTERSTATS     = "http://mlb.mlb.com/lookup/json/named.mlb_bio_hitting_last_10.bam";
    private static final String URL_PITCHERSTATS    = "http://mlb.mlb.com/lookup/json/named.mlb_bio_pitching_last_10.bam";
    private static final String URL_SCHEDULEPART1   = "http://gd2.mlb.com/components/game/mlb/";
    private static final String URL_SCHEDULEPART2   = "/master_scoreboard.json";

    private static final String PARAM_SPORTCODE     = "sport_code";
    private static final String PARAM_PLAYERID      = "player_id";
    private static final String PARAM_GAMECOUNT     = "results";
    private static final String PARAM_GAMETYPE      = "game_type";
    private static final String PARAM_SEASON        = "season";
    private static final String PARAM_TEAMID        = "team_id";

    private static final String VALUE_SPORTCODE     = "'mlb'";
    private static final int    VALUE_GAMECOUNT     = 200;
    private static final String VALUE_GAMETYPE      = "'R'";
    private static final int    VALUE_SEASON        = 2018;

    /**
     * Get MLB Player created from JSON retrieved from MLB using Unirest client
     * Example URL: http://mlb.com/lookup/json/named.player_info.bam?player_id=545361&sport_code=%27mlb%27
     *
     * @param playerId The MLBPlayerID
     * @return
     */
    @Nullable
    @Override
    public MLBPlayer getPlayer(long playerId) {
        MLBPlayer player = null;

        Map<String, Object> parameters = getPlayerParameters(playerId);
        try {

            HttpResponse<JsonNode> response = Unirest.get(URL_PLAYERINFO)
                    .queryString(parameters)
                    .asJson();

            player = JSONPlayerParser.parsePlayer(response.getBody());

        } catch (UnirestException e) {
            System.out.println(String.format("Http Client Exception [playerId:%s]", playerId));
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(String.format("Runtime Exception [playerId:%s]", playerId));
            e.printStackTrace();
        }
        return player;
    }

    private Map<String, Object> getPlayerParameters(long playerId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(PARAM_SPORTCODE, VALUE_SPORTCODE);
        parameters.put(PARAM_PLAYERID, playerId);
        return parameters;
    }

    /**
     * Get MLB Player Stats created from JSON retrieved from MLB using Unirest client
     * Example Batting URL: http://mlb.mlb.com/lookup/json/named.mlb_bio_hitting_last_10.bam?results=200&game_type=%27R%27&season=2014&player_id=545361
     * Example Pitching URL: http://mlb.mlb.com/lookup/json/named.mlb_bio_pitching_last_10.bam?results=200&game_type=%27R%27&season=2014&player_id=433587
     *
     * @param playerId The MLBPlayerId
     * @param isBatter Whether the playerId corresponds to a hitter or a pitcher. True for hitter, false for pitcher.
     * @return {@link List <com.homer.mlb.Stats>}
     */
    @Override
    public Stats getStats(long playerId, boolean isBatter) {
        Stats stats = null;
        Map<String, Object> parameters = getStatsParameters(playerId);
        try {
            String url = isBatter ? URL_BATTERSTATS : URL_PITCHERSTATS;

            HttpRequest request = Unirest.get(url)
                    .queryString(parameters);
            logger.info("Making request to url " + request.getUrl());
            HttpResponse<JsonNode> response = request.asJson();

            logger.info("Parsing stats for mlbPlayerId " + playerId);
            stats = JSONStatParser.parseStats(response.getBody(), isBatter);

        } catch (UnirestException e) {
            System.out.println(String.format("Http Client Exception [playerId:" + playerId + "]", e));
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(String.format("Runtime Exception [playerId:" + playerId + "]", e));
            e.printStackTrace();
        }
        return stats;
    }

    private Map<String, Object> getStatsParameters(long playerId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(PARAM_GAMECOUNT, VALUE_GAMECOUNT);
        parameters.put(PARAM_GAMETYPE, VALUE_GAMETYPE);
        parameters.put(PARAM_SEASON, VALUE_SEASON);
        parameters.put(PARAM_PLAYERID, playerId);
        return parameters;
    }

    /**
     * Get List of MLB Players on a specified team created from JSON retrieved from MLB using Unirest client
     * Example URL: http://mlb.mlb.com/lookup/json/named.roster_40.bam?team_id=147
     * @param teamId The MLB TeamId
     * @return {@link List<MLBPlayer>}
     */
    @Override
    public List<MLBPlayer> get40ManRoster(long teamId) {
        List<MLBPlayer> players = null;

        Map<String, Object> parameters = getTeamParameters(teamId);
        try {

            HttpRequest request = Unirest.get(URL_ROSTER)
                    .queryString(parameters);
            logger.info("Requesting URL " + request.getUrl());
            HttpResponse<JsonNode> response = request.asJson();

            players = JSONRosterParser.parseRoster(response.getBody());

        } catch (UnirestException e) {
            logger.error("Http Client Exception [teamId:" + teamId + "]", e);
        } catch (Exception e) {
            logger.error("Runtime Exception [teamId:" + teamId + "]", e);
        }
        return players;
    }

    private Map<String, Object> getTeamParameters(long teamId) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(PARAM_TEAMID, teamId);
        return parameters;
    }
//
//    /**
//     * Make request to get all the games for the specified date
//     * Example URL: http://gd2.mlb.com/components/game/mlb/year_2014/month_08/day_09/master_scoreboard.json
//     * @param date The date in question
//     * @return {@link List<com.homer.mlb.Game>}
//     */
//    @Override
//    public List<Game> getSchedule(LocalDate date) {
//        List<Game> games = null;
//
//        String urlDatePart = getURLDatePart(date);
//        try {
//            String url = URL_SCHEDULEPART1 + urlDatePart + URL_SCHEDULEPART2;
//            HttpResponse<JsonNode> response = Unirest
//                    .get(url)
//                    .asJson();
//
//            games = JSONScheduleParser.parseSchedule(response.getBody());
//
//        } catch (UnirestException e) {
//            LOG.error("Http Client Exception [date:" + date+ "]", e);
//        } catch (Exception e) {
//            LOG.error("Runtime Exception [date:" + date + "]", e);
//        }
//
//        return games;
//    }

    private String getURLDatePart(LocalDate date) {
        String monthString = String.valueOf(date.getMonthValue());
        if(monthString.length() < 2) {
            monthString = "0" + monthString;
        }
        String dayString = String.valueOf(date.getDayOfMonth());
        if(dayString.length() < 2) {
            dayString = "0" + dayString;
        }
        return "year_" + date.getYear() + "/month_" + monthString + "/day_" + dayString;
    }
}
