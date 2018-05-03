package com.homer.external.rest.fangraphs;

import com.homer.external.common.IFangraphsClient;
import com.homer.external.common.mlb.HittingStats;
import com.homer.external.common.mlb.PitchingStats;
import com.homer.external.common.mlb.Stats;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;

/**
 * @author ari@mark43.com
 * @since 5/2/18
 */
public class FangraphsClient implements IFangraphsClient
{
    private static final Map<BiConsumer<HittingStats, Integer>, Integer> BATTING_STAT_TO_INDEX = new HashMap<>();
    private static final Map<BiConsumer<PitchingStats, Integer>, Integer> PITCHING_STAT_TO_INDEX = new HashMap<>();
    private static final Map<BiConsumer<PitchingStats, Double>, Integer> PITCHING_STAT_DOUBLE_TO_INDEX = new HashMap<>();

    private static final String PLAYER_ID_PARAM = "{playerId}";
    private static final String URL = "https://www.fangraphs.com/statss.aspx?playerid=" + PLAYER_ID_PARAM;

    public static void main(String[] args)
    {
        FangraphsClient fangraphsClient = new FangraphsClient();
        Stats mikeTroutStats = fangraphsClient.getProjections(10155, true);
        System.out.println(mikeTroutStats);
        Stats jakeArrietaStats = fangraphsClient.getProjections(4153, false);
        System.out.println(jakeArrietaStats);
    }

    @Override
    public Stats getProjections(long playerId, boolean isBatter)
    {
        String content = null;
        try
        {
            URLConnection connection =  new URL(URL.replace(PLAYER_ID_PARAM, String.valueOf(playerId))).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Document document = Jsoup.parse(content);
        Elements elements = document.getElementsByClass("grid_projectionsin_show");

        Element projection = elements.get(4);
        Stats stats;
        if (isBatter)
        {
            HittingStats hittingStats = new HittingStats();
            for (BiConsumer<HittingStats, Integer> consumer : BATTING_STAT_TO_INDEX.keySet())
            {
                int index = BATTING_STAT_TO_INDEX.get(consumer);
                TextNode textNode = (TextNode)projection.childNode(index).childNode(0);
                int statValue;
                try
                {
                    statValue = Integer.valueOf(textNode.getWholeText());
                }
                catch (NumberFormatException e)
                {
                    statValue = 0;
                }
                consumer.accept(hittingStats, statValue);
            }
            stats = new Stats(hittingStats, new ArrayList());
        }
        else
        {
            PitchingStats pitchingStats = new PitchingStats();
            for (BiConsumer<PitchingStats, Integer> consumer : PITCHING_STAT_TO_INDEX.keySet())
            {
                int index = PITCHING_STAT_TO_INDEX.get(consumer);
                TextNode textNode = (TextNode)projection.childNode(index).childNode(0);
                int statValue;
                try
                {
                    statValue = Integer.valueOf(textNode.getWholeText());
                }
                catch (NumberFormatException e)
                {
                    statValue = 0;
                }
                consumer.accept(pitchingStats, statValue);
            }
            for (BiConsumer<PitchingStats, Double> consumer : PITCHING_STAT_DOUBLE_TO_INDEX.keySet())
            {
                int index = PITCHING_STAT_DOUBLE_TO_INDEX.get(consumer);
                TextNode textNode = (TextNode)projection.childNode(index).childNode(0);
                double statValue;
                try
                {
                    statValue = Double.valueOf(textNode.getWholeText());
                }
                catch (NumberFormatException e)
                {
                    statValue = 0.0;
                }
                consumer.accept(pitchingStats, statValue);
            }
            stats = new Stats(pitchingStats, new ArrayList());
        }
        return stats;
    }

    static
    {
//        BATTING_STAT_TO_INDEX.put("season", 1);
        BATTING_STAT_TO_INDEX.put(HittingStats::setGames, 3);
        BATTING_STAT_TO_INDEX.put(HittingStats::setAtBats, 4);
        BATTING_STAT_TO_INDEX.put(HittingStats::setPlateAppearances, 5);
        BATTING_STAT_TO_INDEX.put(HittingStats::setHits, 6);
//        BATTING_STAT_TO_INDEX.put("1B", 7);
//        BATTING_STAT_TO_INDEX.put("2B", 8);
//        BATTING_STAT_TO_INDEX.put("3B", 9);
        BATTING_STAT_TO_INDEX.put(HittingStats::setHomeRuns, 10);
        BATTING_STAT_TO_INDEX.put(HittingStats::setRuns, 11);
        BATTING_STAT_TO_INDEX.put(HittingStats::setRbi, 12);
        BATTING_STAT_TO_INDEX.put(HittingStats::setWalks, 13);
//        BATTING_STAT_TO_INDEX.put("IBB", 14);
//        BATTING_STAT_TO_INDEX.put("SO", 15);
        BATTING_STAT_TO_INDEX.put(HittingStats::setHitByPitches, 16);
        BATTING_STAT_TO_INDEX.put(HittingStats::setSacFlies, 17);
        BATTING_STAT_TO_INDEX.put(HittingStats::setSacBunts, 18);
//        BATTING_STAT_TO_INDEX.put("GDP", 19);
        BATTING_STAT_TO_INDEX.put(HittingStats::setStolenBases, 20);
//        BATTING_STAT_TO_INDEX.put("CS", 21);

//        PITCHING_STAT_TO_INDEX.put("season", 1);
        PITCHING_STAT_TO_INDEX.put(PitchingStats::setWins, 3);
//        PITCHING_STAT_TO_INDEX.put(PitchingStats::"L", 4);
//        PITCHING_STAT_TO_INDEX.put(PitchingStats::"ERA", 5);
//        PITCHING_STAT_TO_INDEX.put(PitchingStats::"G", 6);
//        PITCHING_STAT_TO_INDEX.put(PitchingStats::"GS", 7);
//        PITCHING_STAT_TO_INDEX.put(PitchingStats::"CG", 8);
//        PITCHING_STAT_TO_INDEX.put(PitchingStats::"ShO", 9);
        PITCHING_STAT_TO_INDEX.put(PitchingStats::setSaves, 10);
//        PITCHING_STAT_TO_INDEX.put(PitchingStats::"HLD", 11);
//        PITCHING_STAT_TO_INDEX.put(PitchingStats::"BS", 12);
        PITCHING_STAT_DOUBLE_TO_INDEX.put(PitchingStats::setInningsPitched, 13);
//        PITCHING_STAT_TO_INDEX.put(PitchingStats::"TBF", 14);
        PITCHING_STAT_TO_INDEX.put(PitchingStats::setHits, 15);
//        PITCHING_STAT_TO_INDEX.put(PitchingStats::"R", 16);
        PITCHING_STAT_TO_INDEX.put(PitchingStats::setEarnedRuns, 17);
//        PITCHING_STAT_TO_INDEX.put(PitchingStats::"HR", 18);
        PITCHING_STAT_TO_INDEX.put(PitchingStats::setWalks, 19);
//        PITCHING_STAT_TO_INDEX.put(PitchingStats::"IBB", 20);
        PITCHING_STAT_TO_INDEX.put(PitchingStats::setHitByPithces, 21);
//        PITCHING_STAT_TO_INDEX.put(PitchingStats::"WP", 22);
//        PITCHING_STAT_TO_INDEX.put(PitchingStats::"BK", 23);
        PITCHING_STAT_TO_INDEX.put(PitchingStats::setStrikeouts, 24);
    }
}
