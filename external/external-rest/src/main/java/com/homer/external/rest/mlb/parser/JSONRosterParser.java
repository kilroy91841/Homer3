package com.homer.external.rest.mlb.parser;

import com.google.common.collect.Lists;
import com.homer.external.common.mlb.MLBPlayer;
import com.homer.external.rest.mlb.MLBJSONObject;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arigolub on 4/20/16.
 */
public class JSONRosterParser extends JSONParser {

    private static final String JSON_QUERY_RESULTS  = "queryResults";
    private static final String JSON_ROW            = "row";
    private static final String JSON_ROSTER40       = "roster_40";

    public static List<MLBPlayer> parseRoster(JsonNode json) throws Exception {
        List<MLBPlayer> players = null;
        JSONArray array = json
                .getObject()
                .getJSONObject(JSON_ROSTER40)
                .getJSONObject(JSON_QUERY_RESULTS)
                .getJSONArray(JSON_ROW);
        if(array.length() > 0) {
            players = Lists.newArrayList();
            for(int i = 0; i < array.length(); i++) {
                JSONObject obj = (JSONObject)array.get(i);
                MLBPlayer player = JSONPlayerParser.create(new MLBJSONObject(obj));
                players.add(player);
            }
        }
        return players;
    }
}

