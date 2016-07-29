package com.homer.external.rest.espn.parser;

import com.google.common.collect.Lists;
import com.homer.external.common.espn.ESPNTransaction;
import com.homer.util.core.$;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.joda.time.format.DateTimeFormat;
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

    private ESPNTransaction.Type tranType;
    private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy MMM d h:mm a");

    public TransactionsParser(ESPNTransaction.Type tranType) {
        this.tranType = tranType;
    }

    public List<ESPNTransaction> parse(String html) {
        List<ESPNTransaction> transactions = Lists.newArrayList();
        Document document = Jsoup.parse(html);
        Elements transactionRows = document.select(SELECTOR_TABLEROWS);
        transactionRows.remove(0);
        transactionRows.remove(0);
        LOG.info("Found " + transactionRows.size() + " transactions");
        for(Element e : transactionRows) {
            if(this.tranType.equals(ESPNTransaction.Type.ADD) || this.tranType.equals(ESPNTransaction.Type.DROP)) {
                transactions.addAll(parseAddDrop(tranType, e));
            } else if(this.tranType.equals(ESPNTransaction.Type.TRADE)) {
                transactions.addAll(parseTrade(e));
            } else if(this.tranType.equals(ESPNTransaction.Type.MOVE)) {
                transactions.addAll(parseMove(e));
            } else {
                LOG.warn("Unrecognized tran type " + tranType);
            }
        }
        LOG.info("Done parsing, list: " + transactions);
        return transactions;
    }


    public static List<ESPNTransaction> parseAddDrop(ESPNTransaction.Type tranType, Element e) {
        LOG.info("Parsing AddDrops");

        Node playerNode = e.childNode(2);

        String playerNodeText = ((Element)playerNode).text();
        String playerName = ((Element)playerNode.childNode(1)).text().replace("*", "");
        int teamId = new Integer(e.childNode(3).childNode(0).attr("href").split("teamId=")[1]);

        DateTime dateTime = parseTime(e);

        ESPNTransaction transaction = new ESPNTransaction();
        transaction.setPlayerName(playerName);
        transaction.setTeamId(teamId);
        transaction.setType(tranType);
        transaction.setTransDate(dateTime);
        transaction.setText(playerNodeText);
        LOG.info("Transaction: " + transaction);

        List<ESPNTransaction> transactions = Lists.newArrayList();
        transactions.add(transaction);
        return transactions;
    }


    public static List<ESPNTransaction> parseMove(Element e) {
        LOG.info("Parsing Moves");

        Node playerNode = e.childNode(2);

        String pattern = "(\\w+) from (\\w+) to (\\w+)";
        Pattern pat = Pattern.compile(pattern);

        String playerNodeText = ((Element)playerNode).text();
        String playerName = ((Element)playerNode.childNode(1)).text().replace("*", "");
        int teamId = new Integer(e.childNode(3).childNode(0).attr("href").split("teamId=")[1]);

        Matcher m = pat.matcher(playerNodeText);
        String oldPos = null;
        String newPos = null;
        if (m.find()) {
            oldPos = m.group(2);
            newPos = m.group(3);
        }

        DateTime dateTime = parseTime(e);

        ESPNTransaction transaction = new ESPNTransaction();
        transaction.setPlayerName(playerName);
        transaction.setTeamId(teamId);
        transaction.setType(ESPNTransaction.Type.MOVE);
        transaction.setTransDate(dateTime);
        transaction.setText(playerNodeText);
        transaction.setOldPosition(oldPos);
        transaction.setNewPosition(newPos);
        LOG.info("Transaction: " + transaction);

        List<ESPNTransaction> transactions = Lists.newArrayList();
        transactions.add(transaction);
        return transactions;
    }

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

    private static DateTime parseTime(Element e) {
        Node timeNode = e.childNode(0);
        String time = ((Element)timeNode).text();
        DateTime dateTime = DateTime.parse("2015" + time.split(",")[1], dateFormatter);
        return dateTime;
    }
}

