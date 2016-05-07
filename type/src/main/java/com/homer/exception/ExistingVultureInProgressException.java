package com.homer.exception;

/**
 * Created by arigolub on 5/7/16.
 */
public class ExistingVultureInProgressException extends RuntimeException {
    public ExistingVultureInProgressException(String message) {
        super(message);
    }
}
