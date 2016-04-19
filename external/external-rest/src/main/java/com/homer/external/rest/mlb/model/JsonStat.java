package com.homer.external.rest.mlb.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by arigolub on 4/19/16.
 */
public class JsonStat {

    @JsonProperty(value = "hr")
    private String hr;
    @JsonProperty(value = "game_type")
    private String gameType;
    @JsonProperty(value = "opp")
    private String opp;
    @JsonProperty(value = "sac")
    private String sac;
    @JsonProperty(value = "opp_team_display_full")
    private String oppTeamDisplayFull;
    @JsonProperty(value = "date")
    private String date;
    @JsonProperty(value = "rbi")
    private String rbi;
    @JsonProperty(value = "lob")
    private String lob;
    @JsonProperty(value = "tb")
    private String tb;
    @JsonProperty(value = "h2b")
    private String h2b;
    @JsonProperty(value = "game_id")
    private String gameID;
    @JsonProperty(value = "avg")
    private String avg;
    @JsonProperty(value = "slg")
    private String slg;
    @JsonProperty(value = "bb")
    private String bb;
    @JsonProperty(value = "opp_score")
    private String oppScore;
    @JsonProperty(value = "ops")
    private String ops;
    @JsonProperty(value = "hbp")
    private String hbp;
    @JsonProperty(value = "d")
    private String d;
    @JsonProperty(value = "so")
    private String so;
    @JsonProperty(value = "game_date")
    private String gameDate;
    @JsonProperty(value = "sf")
    private String sf;
    @JsonProperty(value = "game_pk")
    private String gamePk;
    @JsonProperty(value = "h")
    private String h;
    @JsonProperty(value = "cs")
    private String cs;
    @JsonProperty(value = "obp")
    private String obp;
    @JsonProperty(value = "opp_team_display_short")
    private String oppTeamDisplayShort;
    @JsonProperty(value = "t")
    private String t;
    @JsonProperty(value = "ao")
    private String ao;
    @JsonProperty(value = "r")
    private String r;
    @JsonProperty(value = "go_ao")
    private String goAo;
    @JsonProperty(value = "sb")
    private String sb;
    @JsonProperty(value = "h3b")
    private String h3b;
    @JsonProperty(value = "ibb")
    private String ibb;
    @JsonProperty(value = "team_result")
    private String teamResult;
    @JsonProperty(value = "ab")
    private String ab;
    @JsonProperty(value = "opp_team_id")
    private String oppTeamID;
    @JsonProperty(value = "team_score")
    private String teamScore;
    @JsonProperty(value = "home_away")
    private String homeAway;
    @JsonProperty(value = "go")
    private String go;

    // region getters + setters

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getOpp() {
        return opp;
    }

    public void setOpp(String opp) {
        this.opp = opp;
    }

    public String getSac() {
        return sac;
    }

    public void setSac(String sac) {
        this.sac = sac;
    }

    public String getOppTeamDisplayFull() {
        return oppTeamDisplayFull;
    }

    public void setOppTeamDisplayFull(String oppTeamDisplayFull) {
        this.oppTeamDisplayFull = oppTeamDisplayFull;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRbi() {
        return rbi;
    }

    public void setRbi(String rbi) {
        this.rbi = rbi;
    }

    public String getLob() {
        return lob;
    }

    public void setLob(String lob) {
        this.lob = lob;
    }

    public String getTb() {
        return tb;
    }

    public void setTb(String tb) {
        this.tb = tb;
    }

    public String getH2b() {
        return h2b;
    }

    public void setH2b(String h2b) {
        this.h2b = h2b;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getSlg() {
        return slg;
    }

    public void setSlg(String slg) {
        this.slg = slg;
    }

    public String getBb() {
        return bb;
    }

    public void setBb(String bb) {
        this.bb = bb;
    }

    public String getOppScore() {
        return oppScore;
    }

    public void setOppScore(String oppScore) {
        this.oppScore = oppScore;
    }

    public String getOps() {
        return ops;
    }

    public void setOps(String ops) {
        this.ops = ops;
    }

    public String getHbp() {
        return hbp;
    }

    public void setHbp(String hbp) {
        this.hbp = hbp;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getSo() {
        return so;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public String getSf() {
        return sf;
    }

    public void setSf(String sf) {
        this.sf = sf;
    }

    public String getGamePk() {
        return gamePk;
    }

    public void setGamePk(String gamePk) {
        this.gamePk = gamePk;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getCs() {
        return cs;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }

    public String getObp() {
        return obp;
    }

    public void setObp(String obp) {
        this.obp = obp;
    }

    public String getOppTeamDisplayShort() {
        return oppTeamDisplayShort;
    }

    public void setOppTeamDisplayShort(String oppTeamDisplayShort) {
        this.oppTeamDisplayShort = oppTeamDisplayShort;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getAo() {
        return ao;
    }

    public void setAo(String ao) {
        this.ao = ao;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getGoAo() {
        return goAo;
    }

    public void setGoAo(String goAo) {
        this.goAo = goAo;
    }

    public String getSb() {
        return sb;
    }

    public void setSb(String sb) {
        this.sb = sb;
    }

    public String getH3b() {
        return h3b;
    }

    public void setH3b(String h3b) {
        this.h3b = h3b;
    }

    public String getIbb() {
        return ibb;
    }

    public void setIbb(String ibb) {
        this.ibb = ibb;
    }

    public String getTeamResult() {
        return teamResult;
    }

    public void setTeamResult(String teamResult) {
        this.teamResult = teamResult;
    }

    public String getAb() {
        return ab;
    }

    public void setAb(String ab) {
        this.ab = ab;
    }

    public String getOppTeamID() {
        return oppTeamID;
    }

    public void setOppTeamID(String oppTeamID) {
        this.oppTeamID = oppTeamID;
    }

    public String getTeamScore() {
        return teamScore;
    }

    public void setTeamScore(String teamScore) {
        this.teamScore = teamScore;
    }

    public String getHomeAway() {
        return homeAway;
    }

    public void setHomeAway(String homeAway) {
        this.homeAway = homeAway;
    }

    public String getGo() {
        return go;
    }

    public void setGo(String go) {
        this.go = go;
    }

    // endregion
}
