package com.homer.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.homer.util.EnumUtil;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 2/14/16.
 */
@Table(name = "draft_dollar")
public class DraftDollar extends BaseObject {

    @Column
    @JsonIgnore
    private long teamId;
    @Column
    private String season;
    @Column
    @JsonIgnore
    private int draftDollarTypeId;
    @Column
    private int amount;

    private Team team;
    private DraftDollarType draftDollarType;

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public int getDraftDollarTypeId() {
        return draftDollarTypeId;
    }

    public void setDraftDollarTypeId(int draftDollarTypeId) {
        this.draftDollarTypeId = draftDollarTypeId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public DraftDollarType getDraftDollarType() {
        if (draftDollarType == null) {
            draftDollarType = EnumUtil.from(DraftDollarType.class, draftDollarTypeId);
        }
        return draftDollarType;
    }

    public void setDraftDollarType(DraftDollarType draftDollarType) {
        this.draftDollarType = draftDollarType;
        this.draftDollarTypeId = draftDollarType.getId();
    }
}
