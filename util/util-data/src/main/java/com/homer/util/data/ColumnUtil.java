package com.homer.util.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.util.core.$;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by arigolub on 3/14/16.
 */
public class ColumnUtil {

    private Map<Class, List<String>> classToColumnsMap;
    private Map<Class, List<Field>> classToFieldsMap;

    private static ColumnUtil instance = new ColumnUtil();

    private ColumnUtil() {
        classToColumnsMap = Maps.newHashMap();
        classToFieldsMap = Maps.newHashMap();
    }

    public static List<String> getColumns(Class clazz) {
        List<String> columns = instance.classToColumnsMap.get(clazz);
        if (columns == null) {
            columns = Lists.newArrayList();
            List<Field> fields = getFields(clazz);
            for(Field f : fields) {
                if (f.isAnnotationPresent(Column.class)) {
                    columns.add(f.getName());
                }
            }
            instance.classToColumnsMap.put(clazz, columns);
        }
        return columns;
    }

    public static List<Field> getFields(Class baseClazz)
    {
        List<Field> fields = instance.classToFieldsMap.get(baseClazz);
        if (fields == null) {
            fields = Lists.newArrayList();
            Class clazz = baseClazz;
            while(clazz.getSuperclass() != null) {
                for(Field f : clazz.getDeclaredFields()) {
                    if (f.isAnnotationPresent(Column.class)) {
                        fields.add(f);
                    }
                }
                clazz = clazz.getSuperclass();
            }
            instance.classToFieldsMap.put(baseClazz, fields);
        }
        return fields;
    }

    public static List<Field> getUpdateFields(Class baseClazz)
    {
        List<Field> fields = getFields(baseClazz);
        return $.of(fields).filterToList(f -> f.getAnnotation(Column.class).updatable());
    }
}
