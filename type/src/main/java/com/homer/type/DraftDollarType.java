package com.homer.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.homer.util.EnumUtil;
import com.homer.util.core.IIntEnum;

/**
 * Created by arigolub on 3/13/16.
 */
@SuppressWarnings("SpellCheckingInspection")
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DraftDollarType implements IIntEnum<DraftDollarType> {
    MLBAUCTION(1, "MLB Draft Dollar"),
    FREEAGENTAUCTION(2, "Free Agent Auction Dollar")
    ;

    private final int id;
    private final String displayText;

    DraftDollarType(int id, String displayText) {
        this.id = id;
        this.displayText = displayText;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public String getDisplayText() {
        return displayText;
    }

    @JsonCreator
    public static DraftDollarType forValue(String value) {
        return EnumUtil.from(DraftDollarType.class, Integer.valueOf(value));
    }

}
