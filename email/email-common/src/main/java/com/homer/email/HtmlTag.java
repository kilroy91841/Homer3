package com.homer.email;

/**
 * Created by arigolub on 5/3/16.
 */
public enum HtmlTag {

    DIV("div"),
    SPAN("span"),
    P("p"),
    A("a"),
    UL("ul"),
    LI("li"),
    ;

    private String tag;

    HtmlTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return tag;
    }
}
