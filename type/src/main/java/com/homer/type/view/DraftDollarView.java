package com.homer.type.view;

import com.homer.type.DraftDollar;
import com.homer.type.Player;
import com.homer.type.Team;
import com.homer.util.HomerBeanUtil;

/**
 * Created by arigolub on 3/17/16.
 */
public class DraftDollarView extends DraftDollar {

    private Team team;

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
}
