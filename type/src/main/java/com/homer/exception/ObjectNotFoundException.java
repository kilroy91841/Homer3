package com.homer.exception;

/**
 * Created by arigolub on 3/18/16.
 */
public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException() {}

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
