package com.homer.util;

import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Created by arigolub on 3/15/16.
 */
public class HomerBeanUtil {

    public static <T, V> void setIfNotNull(T bean, Function<T, V> func, Consumer<V> c) {
        if (bean != null) {
            c.accept(func.apply(bean));
        }
    }

    public static void copyProperties(Object dest, Object orig) {
        try {
            BeanUtils.copyProperties(dest, orig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T cloneBean(T obj) {
        try {
            return (T) BeanUtils.cloneBean(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
