package com.homer.util.core.data;

/**
 * Created by arigolub on 3/12/16.
 */
public class Matcher {

    private String match;
    private String field;

    public Matcher(String field, String match) {
        this.match = match;
        this.field = field;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
