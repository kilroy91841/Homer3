package com.homer.type.view;

import com.google.common.collect.Lists;
import com.homer.type.Position;
import com.homer.type.Team;
import com.homer.type.Trade;
import com.homer.util.HomerBeanUtil;
import com.homer.util.core.$;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created by arigolub on 2/15/16.
 */
public class TeamView extends Team {

    private List<PlayerView> majorLeaguers = Lists.newArrayList();
    private List<PlayerView> minorLeaguers = Lists.newArrayList();
    private int salary;

    private List<PlayerView> catcher = Lists.newArrayList();
    private List<PlayerView> firstBase = Lists.newArrayList();
    private List<PlayerView> secondBase = Lists.newArrayList();
    private List<PlayerView> thirdBase = Lists.newArrayList();
    private List<PlayerView> shortstop = Lists.newArrayList();
    private List<PlayerView> middleInfield = Lists.newArrayList();
    private List<PlayerView> cornerInfield = Lists.newArrayList();
    private List<PlayerView> outfield = Lists.newArrayList();
    private List<PlayerView> utility = Lists.newArrayList();
    private List<PlayerView> pitcher = Lists.newArrayList();
    private List<PlayerView> disabledList = Lists.newArrayList();
    private List<PlayerView> bench = Lists.newArrayList();

    private List<DraftDollarView> draftDollars = Lists.newArrayList();
    private List<MinorLeaguePickView> minorLeaguePicks = Lists.newArrayList();

    private List<Trade> trades = Lists.newArrayList();

    public TeamView() { }

    public static TeamView from(Team team) {
        TeamView tv = new TeamView();
        HomerBeanUtil.copyProperties(tv, team);
        return tv;
    }

    //region getters + setters

    public List<MinorLeaguePickView> getMinorLeaguePicks() {
        return minorLeaguePicks;
    }

    public void setMinorLeaguePicks(List<MinorLeaguePickView> minorLeaguePicks) {
        this.minorLeaguePicks = minorLeaguePicks;
    }

    public List<DraftDollarView> getDraftDollars() {
        return draftDollars;
    }

    public void setDraftDollars(List<DraftDollarView> draftDollars) {
        this.draftDollars = draftDollars;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }

    //region players

    public List<PlayerView> getMajorLeaguers() {
        return majorLeaguers;
    }

    public void setMajorLeaguers(List<PlayerView> majorLeaguers) {
        this.majorLeaguers = majorLeaguers;
    }

    public List<PlayerView> getMinorLeaguers() {
        return minorLeaguers;
    }

    public void setMinorLeaguers(List<PlayerView> minorLeaguers) {
        this.minorLeaguers = minorLeaguers;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public List<PlayerView> getCatcher() {
        if (catcher.size() == 0) {
            setPosition(TeamView::setCatcher, Position.CATCHER);
        }
        return catcher;
    }

    public void setCatcher(List<PlayerView> catcher) {
        this.catcher = catcher;
    }

    public List<PlayerView> getFirstBase() {
        if (firstBase.size() == 0) {
            setPosition(TeamView::setFirstBase, Position.FIRSTBASE);
        }
        return firstBase;
    }

    public void setFirstBase(List<PlayerView> firstBase) {
        this.firstBase = firstBase;
    }

    public List<PlayerView> getSecondBase() {
        if (secondBase.size() == 0) {
            setPosition(TeamView::setSecondBase, Position.SECONDBASE);
        }
        return secondBase;
    }

    public void setSecondBase(List<PlayerView> secondBase) {
        this.secondBase = secondBase;
    }

    public List<PlayerView> getShortstop() {
        if (shortstop.size() == 0) {
            setPosition(TeamView::setShortstop, Position.SHORTSTOP);
        }
        return shortstop;
    }

    public void setShortstop(List<PlayerView> shortstop) {
        this.shortstop = shortstop;
    }

    public List<PlayerView> getThirdBase() {
        if (thirdBase.size() == 0) {
            setPosition(TeamView::setThirdBase, Position.THIRDBASE);
        }
        return thirdBase;
    }

    public void setThirdBase(List<PlayerView> thirdBase) {
        this.thirdBase = thirdBase;
    }

    public List<PlayerView> getMiddleInfield() {
        if (middleInfield.size() == 0) {
            setPosition(TeamView::setMiddleInfield, Position.MIDDLEINFIELD);
        }
        return middleInfield;
    }

    public void setMiddleInfield(List<PlayerView> middleInfield) {
        this.middleInfield = middleInfield;
    }

    public List<PlayerView> getCornerInfield() {
        if (cornerInfield.size() == 0) {
            setPosition(TeamView::setCornerInfield, Position.CORNERINFIELD);
        }
        return cornerInfield;
    }

    public void setCornerInfield(List<PlayerView> cornerInfield) {
        this.cornerInfield = cornerInfield;
    }

    public List<PlayerView> getOutfield() {
        if (outfield.size() == 0) {
            setPosition(TeamView::setOutfield, Position.OUTFIELD);
        }
        return outfield;
    }

    public void setOutfield(List<PlayerView> outfield) {
        this.outfield = outfield;
    }

    public List<PlayerView> getUtility() {
        if (utility.size() == 0) {
            setPosition(TeamView::setUtility, Position.UTILITY);
        }
        return utility;
    }

    public void setUtility(List<PlayerView> utility) {
        this.utility = utility;
    }

    public List<PlayerView> getPitcher() {
        if (pitcher.size() == 0) {
            setPosition(TeamView::setPitcher, Position.PITCHER);
        }
        return pitcher;
    }

    public void setPitcher(List<PlayerView> pitcher) {
        this.pitcher = pitcher;
    }

    public List<PlayerView> getDisabledList() {
        if (disabledList.size() == 0) {
            setPosition(TeamView::setDisabledList, Position.DISABLEDLIST);
        }
        return disabledList;
    }

    public void setDisabledList(List<PlayerView> disabledList) {
        this.disabledList = disabledList;
    }

    public List<PlayerView> getBench() {
        if (bench.size() == 0) {
            setPosition(TeamView::setBench, Position.BENCH);
        }
        return bench;
    }

    public void setBench(List<PlayerView> bench) {
        this.bench = bench;
    }

    //endregion

    //endregion

    //region helpers

    private void setPosition(BiConsumer<TeamView, List<PlayerView>> setter, Position position) {
        setter.accept(this, $.of(majorLeaguers).filterToList(p -> p.getCurrentSeason().getFantasyPosition() == position));
    }

    //endregion
}
