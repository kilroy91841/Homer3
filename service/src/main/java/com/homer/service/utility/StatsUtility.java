package com.homer.service.utility;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 8/5/16.
 */
public final class StatsUtility {

    private StatsUtility() {}

    public static double calculateOBP(int atBats, int hits, int walks, int sacFlies, int hitByPitches) {
        if (atBats == 0) {
            return 0;
        }
        return (hits + walks + hitByPitches) / (double)(atBats + walks + hitByPitches + sacFlies);
    }

    @Nullable
    public static Double calculateWhip(double inningsPitched, int hits, int walks) {
        if (inningsPitched == 0) {
            return null;
        }
        return (walks + hits) / inningsPitched;
    }

    @Nullable
    public static Double calculateEra(double inningsPitched, int earnedRuns) {
        if (inningsPitched == 0) {
            return null;
        }
        return (earnedRuns * 9) / inningsPitched;
    }
}
