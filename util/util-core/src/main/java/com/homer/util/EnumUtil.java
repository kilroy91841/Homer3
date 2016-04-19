package com.homer.util;

import com.homer.util.core.IIntEnum;

import java.util.LinkedHashMap;

/**
 * Created by arigolub on 3/6/16.
 */
public class EnumUtil {

    public static <T extends IIntEnum<T>> T deserialize(Class<T> clazz, Object value) {
        Integer id;
        if (value instanceof LinkedHashMap) {
            id = (Integer)((LinkedHashMap)value).get("id");
        } else if (value instanceof String) {
            id = Integer.valueOf((String) value);
        } else if (value instanceof Integer) {
            id = (Integer) value;
        } else {
            throw new IllegalArgumentException("Unknown object type for " + clazz);
        }
        return EnumUtil.from(clazz, id);
    }

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
