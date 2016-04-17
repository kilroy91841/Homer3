package com.homer.type;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by arigolub on 2/14/16.
 */
@Table(name = "trades", schema = "homer")
public class Trade extends BaseObject {

    @Column(updatable = false)
    private long team1Id;
    @Column(updatable = false)
    private long team2Id;
    @Column(updatable = false)
    private DateTime tradeDate;
    @Column(updatable = false)
    private int season;

    private Team team1;
    private Team team2;
    private List<TradeElement> tradeElements = Lists.newArrayList();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Trade trade = (Trade) o;

        if (team1Id != trade.team1Id) return false;
        if (team2Id != trade.team2Id) return false;
        if (season != trade.season) return false;
        return tradeDate.equals(trade.tradeDate);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (team1Id ^ (team1Id >>> 32));
        result = 31 * result + (int) (team2Id ^ (team2Id >>> 32));
        result = 31 * result + tradeDate.hashCode();
        result = 31 * result + season;
        return result;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "team1Id=" + team1Id +
                ", team2Id=" + team2Id +
                ", tradeDate=" + tradeDate +
                ", season=" + season +
                "} " + super.toString();
    }

    public long getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(long team1Id) {
        this.team1Id = team1Id;
    }

    public long getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(long team2Id) {
        this.team2Id = team2Id;
    }

    public DateTime getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(DateTime tradeDate) {
        this.tradeDate = tradeDate;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public List<TradeElement> getTradeElements() {
        return tradeElements;
    }
}
