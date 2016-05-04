package com.homer.service.auth;

import com.homer.exception.LoginFailedException;

/**
 * Created by arigolub on 5/1/16.
 */
public interface IUserService {

    User login(String userName, String password) throws LoginFailedException;
    boolean hasAccess(String token);
}
