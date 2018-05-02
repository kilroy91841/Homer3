package com.homer.external.rest.mlb.parser;

import com.homer.external.common.mlb.Team;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ari@mark43.com
 * @since 5/2/18
 */
public class JSONScheduleParser extends JSONParser
{
    public static List<Team> parseSchedule(JsonNode json)
    {
        JSONArray teamArray = json.getObject()
                                  .getJSONObject("standings_schedule_date")
                                  .getJSONObject("schedule_game_date_team_chain")
                                  .getJSONArray("schedule_game_date_rptr");
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < teamArray.length(); i++)
        {
            JSONArray nextTwoGames = teamArray.getJSONObject(i).getJSONObject("schedule_game_date").getJSONObject("queryResults").getJSONArray("row");
            String teamId = teamArray.getJSONObject(i).getString("team_id");
            JSONObject game = nextTwoGames.getJSONObject(0);
            String homeTeamId = game.getString("home_team_id");
            String winLoss = homeTeamId.equals(teamId) ? game.getString("home_team_wl") : game.getString("away_team_wl");
            Team team = new Team();
            team.setTeamId(Integer.valueOf(teamId));
            String[] winLossArray = winLoss.split("-");
            team.setWins(Integer.valueOf(winLossArray[0]));
            team.setLosses(Integer.valueOf(winLossArray[1]));
            teams.add(team);
        }
        return teams;
    }
}
