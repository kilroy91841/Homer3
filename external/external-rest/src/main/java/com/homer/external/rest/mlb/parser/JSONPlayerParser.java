package com.homer.external.rest.mlb.parser;

import com.homer.external.common.mlb.MLBPlayer;
import com.homer.external.common.mlb.MLBPlayerStatus;
import com.homer.external.rest.mlb.MLBJSONObject;
import com.homer.external.rest.mlb.model.JsonPlayer;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONObject;

/**
 * Created by arigolub on 2/14/15.
 */
public class JSONPlayerParser extends JSONParser {

    private static final String JSON_QUERY_RESULTS  = "queryResults";
    private static final String JSON_PLAYER_INFO    = "player_info";
    private static final String JSON_ROW            = "row";

    public static MLBPlayer parsePlayer(JsonNode json) throws Exception {
        JSONObject obj = json
                .getObject()
                .getJSONObject(JSON_PLAYER_INFO)
                .getJSONObject(JSON_QUERY_RESULTS)
                .getJSONObject(JSON_ROW);
        return create(new MLBJSONObject(obj));
    }
    
    protected static MLBPlayer create(MLBJSONObject jsonObject) throws Exception {

        JsonPlayer jsonPlayer = new JsonPlayer();
        copyPropertiesFromJson(jsonPlayer, jsonObject);

        MLBPlayer player = new MLBPlayer();
        player.setName(jsonPlayer.getNameDisplayFirstLast());
        player.setFirstName(jsonPlayer.getNameFirst());
        player.setLastName(jsonPlayer.getNameLast());
        player.setId(Long.parseLong(jsonPlayer.getPlayerID()));
        player.setPositionId(getPositionId(jsonPlayer.getPrimaryPosition()));
        player.setMlbPlayerStatus(MLBPlayerStatus.fromStatusCode(jsonPlayer.getStatusCode()));
        player.setTeamId(Integer.parseInt(jsonPlayer.getTeamID()));
        return player;
    }

    private static int getPositionId(String primaryPosition) {
        if ("D".equals(primaryPosition)) {
            return 8;
        }
        if ("8".equals(primaryPosition) || "9".equals(primaryPosition)) {
            return 7;
        }
        return Integer.parseInt(primaryPosition);
    }
}
