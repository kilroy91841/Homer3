package com.homer.type.view;

import com.homer.type.Position;
import com.homer.type.Team;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by arigolub on 2/15/16.
 */
public class TeamView {

    private Team team;
    private List<PlayerView> majorLeaguers;
    private List<PlayerView> minorLeaguers;
    private int salary;

    private List<PlayerView> catcher;
    private List<PlayerView> firstBase;
    private List<PlayerView> secondBase;
    private List<PlayerView> thirdBase;
    private List<PlayerView> shortstop;
    private List<PlayerView> middleInfield;
    private List<PlayerView> cornerInfield;
    private List<PlayerView> outfield;
    private List<PlayerView> utility;
    private List<PlayerView> pitcher;
    private List<PlayerView> disabledList;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

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

    private void setPosition(BiConsumer<TeamView, List<PlayerView>> setter, Position position) {
        setter.accept(this, majorLeaguers.
                stream().
                filter(p -> p.getCurrentSeason().getFantasyPosition() == position).
                collect(Collectors.toList()));
    }

    public List<PlayerView> getCatcher() {
        if (catcher == null) {
            setPosition(TeamView::setCatcher, Position.CATCHER);
        }
        return catcher;
    }

    public void setCatcher(List<PlayerView> catcher) {
        this.catcher = catcher;
    }

    public List<PlayerView> getFirstBase() {
        if (firstBase == null) {
            setPosition(TeamView::setFirstBase, Position.FIRSTBASE);
        }
        return firstBase;
    }

    public void setFirstBase(List<PlayerView> firstBase) {
        this.firstBase = firstBase;
    }

    public List<PlayerView> getSecondBase() {
        if (secondBase == null) {
            setPosition(TeamView::setSecondBase, Position.SECONDBASE);
        }
        return secondBase;
    }

    public void setSecondBase(List<PlayerView> secondBase) {
        this.secondBase = secondBase;
    }

    public List<PlayerView> getShortstop() {
        if (shortstop == null) {
            setPosition(TeamView::setShortstop, Position.SHORTSTOP);
        }
        return shortstop;
    }

    public void setShortstop(List<PlayerView> shortstop) {
        this.shortstop = shortstop;
    }

    public List<PlayerView> getThirdBase() {
        if (thirdBase == null) {
            setPosition(TeamView::setThirdBase, Position.THIRDBASE);
        }
        return thirdBase;
    }

    public void setThirdBase(List<PlayerView> thirdBase) {
        this.thirdBase = thirdBase;
    }

    public List<PlayerView> getMiddleInfield() {
        if (middleInfield == null) {
            setPosition(TeamView::setMiddleInfield, Position.MIDDLEINFIELD);
        }
        return middleInfield;
    }

    public void setMiddleInfield(List<PlayerView> middleInfield) {
        this.middleInfield = middleInfield;
    }

    public List<PlayerView> getCornerInfield() {
        if (cornerInfield == null) {
            setPosition(TeamView::setCornerInfield, Position.CORNERINFIELD);
        }
        return cornerInfield;
    }

    public void setCornerInfield(List<PlayerView> cornerInfield) {
        this.cornerInfield = cornerInfield;
    }

    public List<PlayerView> getOutfield() {
        if (outfield == null) {
            setPosition(TeamView::setOutfield, Position.OUTFIELD);
        }
        return outfield;
    }

    public void setOutfield(List<PlayerView> outfield) {
        this.outfield = outfield;
    }

    public List<PlayerView> getUtility() {
        if (utility == null) {
            setPosition(TeamView::setUtility, Position.UTILITY);
        }
        return utility;
    }

    public void setUtility(List<PlayerView> utility) {
        this.utility = utility;
    }

    public List<PlayerView> getPitcher() {
        if (pitcher == null) {
            setPosition(TeamView::setPitcher, Position.PITCHER);
        }
        return pitcher;
    }

    public void setPitcher(List<PlayerView> pitcher) {
        this.pitcher = pitcher;
    }

    public List<PlayerView> getDisabledList() {
        if (disabledList == null) {
            setPosition(TeamView::setDisabledList, Position.DISABLIEDLIST);
        }
        return disabledList;
    }

    public void setDisabledList(List<PlayerView> disabledList) {
        this.disabledList = disabledList;
    }
}
