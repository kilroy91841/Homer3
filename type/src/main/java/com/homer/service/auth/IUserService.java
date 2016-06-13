package com.homer.service.auth;

import com.homer.exception.LoginFailedException;

import java.util.List;

/**
 * Created by arigolub on 5/1/16.
 */
public interface IUserService {

    User login(String userName, String password) throws LoginFailedException;
    boolean hasAccess(String token);
    List<User> getAllUsers();
    List<User> getUsersForTeam(long teamId);
    boolean sendPasswordResetEmail(String email);
}
