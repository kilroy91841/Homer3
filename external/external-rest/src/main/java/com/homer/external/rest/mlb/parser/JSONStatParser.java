package com.homer.external.rest.mlb.parser;

import com.homer.external.common.mlb.HittingStats;
import com.homer.external.common.mlb.Stats;
import com.homer.external.rest.mlb.model.JsonStat;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arigolub on 4/19/16.
 */
public class JSONStatParser extends JSONParser {

    private static final String JSON_QUERY_RESULTS  = "queryResults";
    private static final String JSON_ROW            = "row";
    private static final String JSON_MLBBIO         = "mlb_bio_";
    private static final String JSON_HITTING        = "hitting";
    private static final String JSON_PITCHING       = "pitching";
    private static final String JSON_LAST10         = "_last_10";
    private static final String JSON_MLBINDIVIDUAL  = "mlb_individual_";
    private static final String JSON_GAMELOG        = "_game_log";

    public static List<Stats> parseStats(long mlbPlayerId, JsonNode json, boolean isBatter) throws Exception {
        List<Stats> stats = null;

        String jsonProperty = isBatter ? JSON_HITTING : JSON_PITCHING;

        JSONArray array = json
                .getObject()
                .getJSONObject(JSON_MLBBIO + jsonProperty + JSON_LAST10)
                .getJSONObject(JSON_MLBINDIVIDUAL + jsonProperty + JSON_GAMELOG)
                .getJSONObject(JSON_QUERY_RESULTS)
                .getJSONArray(JSON_ROW);
        if(array.length() > 0) {
            stats = new ArrayList<Stats>();
            for(int i = 0; i < array.length(); i++) {
                JSONObject obj = (JSONObject)array.get(i);
                JsonStat jsonStat = new JsonStat();
                copyPropertiesFromJson(jsonStat, obj);

            }
        }
        return stats;
    }

    private Stats createStats(JsonStat jsonStat, boolean isBatter) {
        if (isBatter) {
            HittingStats stats = new HittingStats();
        }
        return null;
    }
}
