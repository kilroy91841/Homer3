package com.homer.external.rest.espn.parser;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.homer.external.common.espn.ESPNPlayer;
import com.homer.util.core.$;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by arigolub on 7/29/16.
 */
public class TeamRosterParser {

    private static final Logger LOG = LoggerFactory.getLogger(TeamRosterParser.class);

    public static List<ESPNPlayer> parse(String html, int teamId) {
        Document document = Jsoup.parse(html);
        List<Element> playerRows =
                $.of(document.select(".tableBody tr.pncPlayerRow")).filter(elem -> !Strings.isNullOrEmpty(elem.attr("id"))).toList();
        List<Element> playerElements =
                $.of(playerRows).toList(e -> e.select(".playertablePlayerName a[tab=null]").get(0));
        List<String> positions =
                $.of(playerRows).toList(e -> e.select(".playerSlot").text());
        List<ESPNPlayer> espnPlayers = Lists.newArrayList();
        for(int i = 0; i < playerElements.size(); i++) {
            String playerName = playerElements.get(i).text();
            String position = positions.get(i);
            String playerId = playerElements.get(i).attr("playerid");
            espnPlayers.add(new ESPNPlayer(playerName, position, teamId, Integer.parseInt(playerId)));
        }
        return espnPlayers;
    }
}
