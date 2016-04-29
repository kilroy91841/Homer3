package com.homer.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.homer.util.EnumUtil;
import com.homer.util.core.IIntEnum;

/**
 * Created by arigolub on 4/20/16.
 */
@SuppressWarnings("SpellCheckingInspection")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Status implements IIntEnum<Status> {

    ACTIVE(1, "Active", "A"),
    MINORS(2, "Minors", "MIN"),
    DISABLEDLIST(3, "Disabled List", "DL"),
    FREEAGENT(4, "Free Agent", "FA"),
    UNKNOWN(5, "Unknown", "UNK"),
    ;

    private final int id;
    private final String name;
    private final String abbreviation;

    private Status(int id, String name, String abbreviation) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    @JsonCreator
    public static Status forValue(Object value) {
        return EnumUtil.deserialize(Status.class, value);
    }
}
