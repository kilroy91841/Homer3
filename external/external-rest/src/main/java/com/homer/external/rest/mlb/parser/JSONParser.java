package com.homer.external.rest.mlb.parser;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by arigolub on 4/19/16.
 */
public abstract class JSONParser {

    public static <T> void copyPropertiesFromJson(T newObj, JSONObject json) throws InvocationTargetException, IllegalAccessException {
        for(Field f : newObj.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(JsonProperty.class)) {
                JsonProperty ann = f.getAnnotation(JsonProperty.class);
                BeanUtils.setProperty(newObj, f.getName(), json.getString(ann.value()));
            }
        }
    }
}
