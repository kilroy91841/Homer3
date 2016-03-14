package com.homer.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.homer.util.core.IIntEnum;

/**
 * Created by arigolub on 3/13/16.
 */
@SuppressWarnings("SpellCheckingInspection")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DraftDollarType implements IIntEnum<DraftDollarType> {
    MLBAUCTION(1),
    FREEAGENTAUCTION(2)
    ;

    private final int id;

    DraftDollarType(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
}
