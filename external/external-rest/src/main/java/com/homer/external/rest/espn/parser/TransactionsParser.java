package com.homer.external.rest.espn.parser;

import com.google.common.collect.Lists;
import com.homer.external.common.espn.ESPNTransaction;
import com.homer.util.LeagueUtil;
import com.homer.util.core.$;
import com.homer.util.core.Pair;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.joda.time.format.DateTimeFormat;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by arigolub on 7/25/16.
 */
public class TransactionsParser {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionsParser.class);

    private static final String SELECTOR_TABLEROWS  = ".tableBody tr";
    private static final String SELECTOR_BR         = "br";
    private static final String POSITION_PATTERN    = "from ([\\/\\w]+) to ([\\/\\w]+)";

    private ESPNTransaction.Type tranType;
    private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy MMM d h:mm a");

    public static List<ESPNTransaction> parse(ESPNTransaction.Type tranType, String html) {
        List<ESPNTransaction> transactions = Lists.newArrayList();
        Document document = Jsoup.parse(html);
        Elements transactionRows = document.select(SELECTOR_TABLEROWS);
        transactionRows.remove(0);
        transactionRows.remove(0);
        LOG.info("Found " + transactionRows.size() + " transactions");
        for(Element e : transactionRows) {
            transactions.addAll(parseTransactionRow(tranType, e));
        }
        LOG.info("Done parsing, list: " + transactions);
        return transactions;
    }

    public static List<ESPNTransaction> parseTransactionRow(ESPNTransaction.Type tranType, Element e) {
        LOG.info("Parsing Transactions");

        List<ESPNTransaction> transactions = Lists.newArrayList();

        DateTime dateTime = parseTime(e);
        int teamId = parseTeamId(e);
        Node playerNode = e.childNode(2);

        for(int i = 0; i + 3 <=  playerNode.childNodes().size(); i += 4) {
            String playerNameString = ((Element)playerNode.childNode(i+1)).text().replace("*", "");
            String fullText = ((TextNode)playerNode.childNode(i)).text() +
                    playerNameString +
                    ((TextNode)playerNode.childNode(i+2)).text();

            ESPNTransaction espnTransaction = createTransaction(playerNameString, teamId, tranType, dateTime, fullText);
            if(tranType.equals(ESPNTransaction.Type.ADD) || tranType.equals(ESPNTransaction.Type.DROP)) {
                transactions.add(espnTransaction);
            } else if(tranType.equals(ESPNTransaction.Type.MOVE)) {
                Pair<String, String> positions = getPositionsForMove(fullText);
                espnTransaction.setOldPosition(positions.getFirst());
                espnTransaction.setNewPosition(positions.getSecond());
                transactions.add(espnTransaction);
            } else {
                LOG.info("Unsupported transaction type: " + tranType);
                continue;
            }
            LOG.info("Transaction: " + espnTransaction);
        }
        return transactions;
    }

    private static Pair<String, String> getPositionsForMove(String fullText) {
        Pattern pat = Pattern.compile(POSITION_PATTERN);
        Matcher m = pat.matcher(fullText);
        String oldPos = null;
        String newPos = null;
        if (m.find()) {
            oldPos = m.group(1);
            newPos = m.group(2);
        }
        return new Pair<>(oldPos, newPos);
    }

    private static ESPNTransaction createTransaction(String playerName, int teamId, ESPNTransaction.Type transactionType,
                                                     DateTime transDate, String fullText) {
        ESPNTransaction transaction = new ESPNTransaction();
        transaction.setPlayerName(playerName);
        transaction.setTeamId(teamId);
        transaction.setType(transactionType);
        transaction.setTransDate(transDate);
        transaction.setText(fullText);
        return transaction;
    }

    private static DateTime parseTime(Element e) {
        Node timeNode = e.childNode(0);
        String time = ((Element)timeNode).text();
        return DateTime.parse(LeagueUtil.SEASON + time.split(",")[1], dateFormatter);
    }

    private static int parseTeamId(Element e) {
        return new Integer(e.childNode(3).childNode(0).attr("href").split("teamId=")[1]);
    }

    // region trade

    public static List<ESPNTransaction> parseTrade(Element e) {
        LOG.info("Parsing trades");
        if(!e.childNode(1).toString().contains("Trade Upheld")) {
            LOG.info("Trade is not 'Trade Upheld', delaying parsing until its upheld");
            return Lists.newArrayList();
        }
        DateTime dateTime = parseTime(e);

        //The trade text only contains team code, not team id, so map the team code in the trade text
        //to the team ids found in the link. Then use these codes to map which team ids received which player
        String teamCode1 = ((Element)e.childNode(3).childNode(0)).text().split(" ")[0];
        int teamId1 = new Integer(e.childNode(3).childNode(0).attr("href").split("teamId=")[1]);
        String teamCode2 = ((Element)e.childNode(3).childNode(2)).text().split(" ")[0];
        int teamId2 = new Integer(e.childNode(3).childNode(2).attr("href").split("teamId=")[1]);

        List<ESPNTransaction> transactions = Lists.newArrayList();
        List<List<Node>> individualTradeMoves = Lists.newArrayList();

        Node tradeTextNode = e.childNode(2);

        List<Node> individualMove = new ArrayList<Node>();
        for(int i = 0; i < tradeTextNode.childNodes().size(); i++) {
            if(tradeTextNode.childNode(i).getClass().equals(TextNode.class)) {
                individualMove.add(tradeTextNode.childNode(i));
            } else if(tradeTextNode.childNode(i).getClass().equals(Element.class)) {
                Element e1 = (Element)tradeTextNode.childNode(i);
                if(e1.tag().getName().equals(SELECTOR_BR)) {
                    individualTradeMoves.add(individualMove);
                    individualMove = new ArrayList<Node>();
                } else {
                    individualMove.add(e1.childNode(0));
                }
            }
        }
        if(individualMove.size() > 0) {
            individualTradeMoves.add(individualMove);
        }
        for(List<Node> tradeNodes : individualTradeMoves) {
            Node tradingTeamNode = tradeNodes.get(0);
            Node playerNode = tradeNodes.get(1);
            Node tradedToTeamNode = tradeNodes.get(2);
            String tradingTeamName = tradingTeamNode.toString().split(" ")[0];
            String playerName = playerNode.toString();
            String tradedToTeamName = tradedToTeamNode.toString().split("to ")[1];
            Integer acquiringTeamId = null;
            if(tradedToTeamName.equals(teamCode1)) {
                acquiringTeamId = teamId1;
            } else if(tradedToTeamName.equals(teamCode2)) {
                acquiringTeamId = teamId2;
            } else {
                LOG.warn("Could not find acquiring team for this trade");
            }
            ESPNTransaction transaction = new ESPNTransaction();
            transaction.setTransDate(dateTime);
            transaction.setPlayerName(playerName);
            transaction.setType(ESPNTransaction.Type.TRADE);
            transaction.setTeamId(acquiringTeamId);
            transaction.setText($.of(tradeNodes).toList(Node::toString).stream().collect(Collectors.joining("")));
            transactions.add(transaction);
        }
        return transactions;
    }

    // endregion
}

