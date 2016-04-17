package com.homer.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.homer.util.EnumUtil;
import com.homer.util.core.IIntEnum;

import java.util.LinkedHashMap;

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
    public static DraftDollarType forValue(Object value) {
        Integer typeId;
        if (value instanceof LinkedHashMap) {
            typeId = (Integer)((LinkedHashMap)value).get("id");
        } else if (value instanceof String) {
            typeId = Integer.valueOf((String)value);
        } else {
            throw new IllegalArgumentException("Unknown object type for DRAFTDOLLARTYPE");
        }
        return EnumUtil.from(DraftDollarType.class, typeId);
    }

}
