package com.homer.type.view;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by arigolub on 6/10/16.
 */
public class MinorLeagueDraftView {

    private List<MinorLeaguePickView> picks;
    @Nullable
    private MinorLeaguePickView currentPick;

    public List<MinorLeaguePickView> getPicks() {
        return picks;
    }

    public void setPicks(List<MinorLeaguePickView> picks) {
        this.picks = picks;
    }

    @Nullable
    public MinorLeaguePickView getCurrentPick() {
        return currentPick;
    }

    public void setCurrentPick(@Nullable MinorLeaguePickView currentPick) {
        this.currentPick = currentPick;
    }
}
