package com.homer.service.auth;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import com.homer.data.common.ISessionTokenRepository;
import com.homer.exception.LoginFailedException;
import com.homer.type.auth.SessionToken;
import com.homer.util.core.$;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by arigolub on 5/1/16.
 */
public class UserService implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private IAuthService authService;
    private ISessionTokenRepository sessionTokenRepo;

    public UserService(IAuthService authService, ISessionTokenRepository sessionTokenRepo) {
        this.authService = authService;
        this.sessionTokenRepo = sessionTokenRepo;
    }

    @Override
    public User login(String userName, String password) throws LoginFailedException{
        User user = authService.authenticate(userName, password);
        if (user == null) {
            throw new LoginFailedException(String.format("Login failed for %s", userName));
        }
        String token = sessionTokenRepo.createForUser(user.getUserName(), user.getTeamId());
        user.setToken(BaseEncoding.base64().encode(token.getBytes(Charsets.US_ASCII)));
        return user;
    }

    @Override
    public boolean hasAccess(String token) {
        String decodedToken = new String(BaseEncoding.base64().decode(token), Charsets.US_ASCII);
        SessionToken sessionToken = sessionTokenRepo.getByToken(decodedToken);
        if (sessionToken == null || DateTime.now().isAfter(sessionToken.getExpirationDateUTC())) {
            return false;
        }
        logger.info(String.format("Successful session auth for %s", sessionToken.getUserName()));
        return true;
    }

    @Override
    public List<User> getAllUsers() {
        return authService.getUsers();
    }

    @Override
    public List<User> getUsersForTeam(long teamId) {
        List<User> allUsers = getAllUsers();
        return $.of(allUsers).filterToList(user -> user.getTeamId() == teamId);
    }

    @Override
    public boolean sendPasswordResetEmail(String email) {
        return authService.sendPasswordResetEmail(email);
    }
}
