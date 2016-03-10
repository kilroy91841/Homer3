package com.homer.util;

import com.homer.util.core.IIntEnum;

/**
 * Created by arigolub on 3/6/16.
 */
public class EnumUtil {

    public static <T extends IIntEnum<T>> T from(Class<T> clazz, int id) {
        for (T e : clazz.getEnumConstants())
        {
            if (e.getId() == id)
            {
                return e;
            }
        }
        return null;
    }

}
