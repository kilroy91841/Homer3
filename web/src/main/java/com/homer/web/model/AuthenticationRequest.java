package com.homer.web.model;

/**
 * Created by arigolub on 5/1/16.
 */
public class AuthenticationRequest {

    private String userName;
    private String encodedPassword;

    @Override
    public String toString() {
        return "AuthenticationRequest{" +
                "userName='" + userName + '\'' +
                ", encodedPassword='" + encodedPassword + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }
}
