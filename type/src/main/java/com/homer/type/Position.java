package com.homer.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.homer.util.EnumUtil;
import com.homer.util.core.IIntEnum;

import java.util.LinkedHashMap;

/**
 * Created by arigolub on 3/5/16.
 */
@SuppressWarnings("SpellCheckingInspection")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Position implements IIntEnum<Position> {
    UTILITY(12, "U", null, null, 9),
    MIDDLEINFIELD(10, "2B/SS", UTILITY, null, 6),
    CORNERINFIELD(11, "1B/3B", UTILITY, null, 7),
    PITCHER(1, "P", null, null, 10),
    CATCHER(2, "C", UTILITY, null, 1),
    FIRSTBASE(3, "1B", CORNERINFIELD, UTILITY, 2),
    SECONDBASE(4, "2B", MIDDLEINFIELD, UTILITY, 3),
    THIRDBASE(5, "3B", CORNERINFIELD, UTILITY, 4),
    SHORTSTOP(6, "SS", MIDDLEINFIELD, UTILITY, 5),
    OUTFIELD(7, "OF", UTILITY, null, 8),
    DESIGNATEDHITTER(8, "DH", UTILITY, null, 9),
    RELIEFPITCHER(9, "RP", null, null, 10),
    DISABLEDLIST(13, "DL", null, null, 12),
    MINORLEAGUES(14, "MIN", null, null, 13),
    BENCH(15, "BENCH", null, null, 11),
    ;

    private final int id;
    private final String name;
    private final Position grants1;
    private final Position grants2;
    private final int sort;

    private Position(int id, String name, Position grants1, Position grants2, int sort) {
        this.id = id;
        this.name = name;
        this.grants1 = grants1;
        this.grants2 = grants2;
        this.sort = sort;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Position getGrants1() {
        return grants1;
    }

    public Position getGrants2() {
        return grants2;
    }

    public int getSort() {
        return sort;
    }

    @JsonCreator
    public static Position forValue(Object value) {
        return EnumUtil.deserialize(Position.class, value);
    }
}
