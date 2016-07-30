package com.homer.external.rest.mlb.parser;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.homer.external.common.mlb.BaseStats;
import com.homer.external.common.mlb.HittingStats;
import com.homer.external.common.mlb.PitchingStats;
import com.homer.external.common.mlb.Stats;
import com.homer.external.rest.mlb.model.JsonStat;
import com.mashape.unirest.http.JsonNode;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;
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
    private static final String JSON_LASTX          = "_last_x_total";

    public static Stats parseStats(JsonNode json, boolean isBatter) throws Exception {
        String jsonProperty = isBatter ? JSON_HITTING : JSON_PITCHING;

        JSONObject queryResults = json
                .getObject()
                .getJSONObject(JSON_MLBBIO + jsonProperty + JSON_LAST10)
                .getJSONObject(JSON_MLBINDIVIDUAL + jsonProperty + JSON_GAMELOG)
                .getJSONObject(JSON_QUERY_RESULTS);

        if (!queryResults.has(JSON_ROW)) {
            return new Stats();
        }
        JSONArray array;
        try {
            array = queryResults.getJSONArray(JSON_ROW);
        } catch (JSONException e) {
            JSONObject obj = queryResults.getJSONObject(JSON_ROW);
            array = new JSONArray();
            array.put(0, obj);
        }

        List<BaseStats> gameLog = Lists.newArrayList();
        if(array.length() > 0) {
            gameLog = new ArrayList<BaseStats>();
            for(int i = 0; i < array.length(); i++) {
                JSONObject obj = (JSONObject)array.get(i);
                JsonStat jsonStat = new JsonStat();
                copyPropertiesFromJson(jsonStat, obj);
                gameLog.add(create(jsonStat, isBatter));
            }
        }

        JSONObject lastXTotal = json
                .getObject()
                .getJSONObject(JSON_MLBBIO + jsonProperty + JSON_LAST10)
                .getJSONObject(JSON_MLBINDIVIDUAL + jsonProperty + JSON_LASTX)
                .getJSONObject(JSON_QUERY_RESULTS)
                .getJSONObject(JSON_ROW);
        JsonStat jsonSeasonStats = new JsonStat();
        copyPropertiesFromJson(jsonSeasonStats, lastXTotal);
        BaseStats seasonStats = create(jsonSeasonStats, isBatter);
        return new Stats(seasonStats, gameLog);
    }

    protected static BaseStats create(JsonStat jsonStat, boolean isBatter) {
        BaseStats stat;
        if (isBatter) {
            HittingStats hittingStats = new HittingStats();
            hittingStats.setAtBats(safeIntParse(jsonStat.getAb()));
            hittingStats.setRuns(safeIntParse(jsonStat.getR()));
            hittingStats.setRbi(safeIntParse(jsonStat.getRbi()));
            hittingStats.setHomeRuns(safeIntParse(jsonStat.getHr()));
            hittingStats.setStolenBases(safeIntParse(jsonStat.getSb()));
            hittingStats.setBattingAverage(safeDoubleParse(jsonStat.getAvg()));
            hittingStats.setOnBasePercentage(safeDoubleParse(jsonStat.getObp()));
            hittingStats.setSluggingPercentage(safeDoubleParse(jsonStat.getSlg()));
            hittingStats.setOnBasePlusSlugging(safeDoubleParse(jsonStat.getOps()));
            hittingStats.setWalks(safeIntParse(jsonStat.getBb()));
            hittingStats.setHitByPitches(safeIntParse(jsonStat.getHbp()));
            hittingStats.setSacFlies(safeIntParse(jsonStat.getSf()));
            hittingStats.setTotalBases(safeIntParse(jsonStat.getTb()));
            stat = hittingStats;
        } else {
            PitchingStats pitchingStats = new PitchingStats();
            Double inningsPitched = safeDoubleParse(jsonStat.getIp());
            if (inningsPitched != null) {
                int wholePart = (int) (double) inningsPitched;
                double fractionPart = (inningsPitched - wholePart) * 3.333333;
                double modifiedInningsPitched = wholePart + fractionPart;
                pitchingStats.setInningsPitched(modifiedInningsPitched);
            }
            pitchingStats.setEra(safeDoubleParse(jsonStat.getEra()));
            pitchingStats.setWhip(safeDoubleParse(jsonStat.getWhip()));
            pitchingStats.setWins(safeIntParse(jsonStat.getW()));
            pitchingStats.setSaves(safeIntParse(jsonStat.getSv()));
            pitchingStats.setStrikeouts(safeIntParse(jsonStat.getSo()));
            pitchingStats.setHits(safeIntParse(jsonStat.getH()));
            pitchingStats.setWalks(safeIntParse(jsonStat.getBb()));
            pitchingStats.setEarnedRuns(safeIntParse(jsonStat.getEr()));
            stat = pitchingStats;
        }
        if (!Strings.isNullOrEmpty(jsonStat.getGameDate())) {
            stat.setGameDate(DateTime.parse(jsonStat.getGameDate()));
        }
        return stat;
    }
    
    @Nullable
    private static Integer safeIntParse(@Nullable String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return Integer.parseInt(str);
    }
    
    @Nullable
    private static Double safeDoubleParse(@Nullable String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return Double.parseDouble(str);
    }
}
