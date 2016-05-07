package com.homer.exception;

/**
 * Created by arigolub on 5/1/16.
 */
public class LoginFailedException extends Exception {

    public LoginFailedException() {
    }

    public LoginFailedException(String message) {
        super(message);
    }
}
