package com.homer.web;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by arigolub on 5/1/16.
 */
@Retention(RUNTIME)
@Target({ METHOD })
public @interface AuthRequired {
}
