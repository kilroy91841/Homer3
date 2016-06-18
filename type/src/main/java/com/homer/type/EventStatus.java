package com.homer.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homer.util.core.IIntEnum;

/**
 * Created by arigolub on 5/4/16.
 */
@SuppressWarnings("SpellCheckingInspection")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EventStatus implements IIntEnum<EventStatus> {

    IN_PROGRESS(1, "In Progress"),
    FIXED(2, "Fixed"),
    SUCCESSFUL(3, "Successful"),
    INVALID(4, "Invalid"),
    ERROR(5, "Error"),
    COMPLETE(6, "Complete"),
    REQUESTED(7, "Requested"),
    DENIED(8, "Denied"),
    CANCELLED(9, "Cancelled"),
    ;

    private final int id;
    private final String name;

    EventStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getId() {
        return id;
    }
}
