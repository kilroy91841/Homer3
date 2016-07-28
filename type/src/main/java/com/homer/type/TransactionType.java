package com.homer.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homer.util.core.IIntEnum;

/**
 * Created by arigolub on 7/25/16.
 */
@SuppressWarnings("SpellCheckingInspection")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TransactionType implements IIntEnum<EventStatus> {

    ADD(1, "Add"),
    DROP(2, "Drop"),
    MOVE(3, "Move"),
    TRADE(4, "Trade"),
    UNKNOWN(5, "Unknown"),
    ;

    private final int id;
    private final String name;

    TransactionType(int id, String name) {
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
