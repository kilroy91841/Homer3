package com.homer.type.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.homer.type.*;
import com.homer.type.history.HistoryDraftDollar;
import com.homer.util.HomerBeanUtil;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 3/17/16.
 */
public class DraftDollarView extends DraftDollar {

    private Team team;
    @Nullable
    private Trade trade;
    @Nullable
    private Long historyId;
    private List<DraftDollarView> historyDraftDollars;
    @Nullable
    private SeptemberStanding septemberStanding;

    public DraftDollarView() { }

    public static DraftDollarView from(DraftDollar draftDollar) {
        DraftDollarView ddv = new DraftDollarView();
        HomerBeanUtil.copyProperties(ddv, draftDollar);
        return ddv;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Nullable
    public Trade getTrade() {
        return trade;
    }

    public void setTrade(@Nullable Trade trade) {
        this.trade = trade;
    }

    public List<DraftDollarView> getHistoryDraftDollars() {
        return historyDraftDollars;
    }

    public void setHistoryDraftDollars(List<DraftDollarView> historyDraftDollars) {
        this.historyDraftDollars = historyDraftDollars;
    }

    @Nullable
    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(@Nullable Long historyId) {
        this.historyId = historyId;
    }

    @Nullable
    public SeptemberStanding getSeptemberStanding() {
        return septemberStanding;
    }

    public void setSeptemberStanding(@Nullable SeptemberStanding septemberStanding) {
        this.septemberStanding = septemberStanding;
    }

    @JsonProperty
    public String getText() {
        return team.getName() + "/" + getSeason() + "/" + getDraftDollarType().getDisplayText();
    }
}
