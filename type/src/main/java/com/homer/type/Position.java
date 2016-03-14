package com.homer.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homer.util.core.IIntEnum;

/**
 * Created by arigolub on 3/5/16.
 */
@SuppressWarnings("SpellCheckingInspection")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Position implements IIntEnum<Position> {
    UTILITY(12, "U", null, null),
    MIDDLEINFIELD(10, "2B/SS", UTILITY, null),
    CORNERINFIELD(11, "1B/3B", UTILITY, null),
    PITCHER(1, "P", null, null),
    CATCHER(2, "C", UTILITY, null),
    FIRSTBASE(3, "1B", CORNERINFIELD, UTILITY),
    SECONDBASE(4, "2B", MIDDLEINFIELD, UTILITY),
    THIRDBASE(5, "3B", CORNERINFIELD, UTILITY),
    SHORTSTOP(6, "SS", MIDDLEINFIELD, UTILITY),
    OUTFIELD(7, "OF", UTILITY, null),
    DESIGNATEDHITTER(8, "DH", UTILITY, null),
    RELIEFPITCHER(9, "RP", null, null),
    DISABLIEDLIST(13, "DL", null, null),
    MINORLEAGUES(14, "MIN", null, null);

    private final int id;
    private final String name;
    private final Position grants1;
    private final Position grants2;

    private Position(int id, String name, Position grants1, Position grants2) {
        this.id = id;
        this.name = name;
        this.grants1 = grants1;
        this.grants2 = grants2;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Position getGrants1() {
        return grants1;
    }

    public Position getGrants2() {
        return grants2;
    }
}
