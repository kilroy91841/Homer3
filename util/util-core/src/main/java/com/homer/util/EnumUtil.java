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

    public static <T extends IIntEnum> T fromNotParameterized(Class<T> clazz, int id) {
        return (T) from(clazz, id);
    }

    public static <T extends IIntEnum> int to(T obj) {
        return obj.getId();
    }

}
