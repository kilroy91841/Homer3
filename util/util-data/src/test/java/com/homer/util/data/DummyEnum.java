package com.homer.util.data;

import com.homer.util.core.IIntEnum;

/**
 * Created by arigolub on 3/15/16.
 */
public enum DummyEnum implements IIntEnum<DummyEnum> {
    VALUE1(1),
    VALUE2(2),
    ;

    private int id;

    private DummyEnum(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
