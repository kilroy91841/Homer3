package com.homer.util.core;

import java.util.EnumSet;

/**
 * Created by arigolub on 3/6/16.
 */
public interface IIntEnum<T extends IIntEnum<T>> {

    int getId();
    String getName();
}
