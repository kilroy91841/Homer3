package com.homer.type;

import com.google.common.collect.Sets;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Created by arigolub on 7/30/16.
 */
public class StandingCategory implements Comparable<StandingCategory> {

    private static final Set<String> REVERSE_CATEGORIES = Sets.newHashSet("whip", "era");

    private final long teamId;
    private final String category;
    @Nullable
    private final Double result;
    private double points;

    public StandingCategory(long teamId, String category, Double result) {
        this.teamId = teamId;
        this.category = category;
        this.result = result;
    }

    public long getTeamId() {
        return teamId;
    }

    public String getCategory() {
        return category;
    }

    @Nullable
    public Double getResult() {
        return result;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    @Override
    public int compareTo(StandingCategory o) {
        if (!this.category.equals(o.category)) {
            throw new IllegalArgumentException("Cannot compare across categories");
        }
        int val;
        if (this.result == null && o.result != null) {
            val = 1;
        } else if (this.result != null && o.result == null) {
            val = -1;
        } else if (this.result == null && o.result == null) {
            val = 0;
        } else if (this.result > o.result) {
            val = -1;
        } else if (this.result < o.result) {
            val = 1;
        } else {
            val = 0;
        }
        return REVERSE_CATEGORIES.contains(this.category) ? val * -1 : val;
    }
}
