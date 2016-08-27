package com.homer.service.auth;

import com.google.common.collect.Lists;
import com.homer.exception.LoginFailedException;

import java.util.Collection;
import java.util.List;

/**
 * Created by arigolub on 5/1/16.
 */
public interface IUserService {

    User login(String userName, String password) throws LoginFailedException;
    boolean hasAccess(String token);
    List<User> getAllUsers();
    List<User> getUsersForTeams(Collection<Long> teamIds);
    default List<User> getUsersForTeam(long teamId) {
        return getUsersForTeams(Lists.newArrayList(teamId));
    }
    boolean sendPasswordResetEmail(String email);
}
