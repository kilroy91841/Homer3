package com.homer.type;

import com.google.common.base.Objects;
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
    private DateTime proposedDateUTC;
    @Column
    private DateTime respondedDateUTC;
    @Column(updatable = false)
    private int season;
    @Column
    private EventStatus tradeStatus;

    private Team team1;
    private Team team2;
    private List<TradeElement> tradeElements = Lists.newArrayList();

    // region equals/hashCode/toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Trade trade = (Trade) o;
        return team1Id == trade.team1Id &&
                team2Id == trade.team2Id &&
                season == trade.season &&
                Objects.equal(proposedDateUTC, trade.proposedDateUTC) &&
                Objects.equal(respondedDateUTC, trade.respondedDateUTC) &&
                tradeStatus == trade.tradeStatus &&
                Objects.equal(team1, trade.team1) &&
                Objects.equal(team2, trade.team2) &&
                Objects.equal(tradeElements, trade.tradeElements);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), team1Id, team2Id, proposedDateUTC, respondedDateUTC, season, tradeStatus, team1, team2, tradeElements);
    }

    @Override
    public String toString() {
        return "Trade{" +
                "team1Id=" + team1Id +
                ", team2Id=" + team2Id +
                ", proposedDateUTC=" + proposedDateUTC +
                ", respondedDateUTC=" + respondedDateUTC +
                ", season=" + season +
                ", tradeStatus=" + tradeStatus +
                ", team1=" + team1 +
                ", team2=" + team2 +
                ", tradeElements=" + tradeElements +
                "} " + super.toString();
    }

    // endregion

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

    public DateTime getProposedDateUTC() {
        return proposedDateUTC;
    }

    public void setProposedDateUTC(DateTime proposedDateUTC) {
        this.proposedDateUTC = proposedDateUTC;
    }

    public DateTime getRespondedDateUTC() {
        return respondedDateUTC;
    }

    public void setRespondedDateUTC(DateTime respondedDateUTC) {
        this.respondedDateUTC = respondedDateUTC;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public EventStatus getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(EventStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
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
