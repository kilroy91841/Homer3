package com.homer.auth.stormpath;

import com.homer.service.auth.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by arigolub on 5/1/16.
 */
public class StormpathAuthServiceTest {

    private StormpathAuthService service = StormpathAuthService.FACTORY.getInstance();

    private static final String USER_NAME = "arigolub";
    private static final String EMAIL = "arigolub+1@gmail.com";
    private static final long TEAM_ID = 1;
    private static final String FIRST_NAME = "Ari";
    private static final String LAST_NAME = "Golub";

    private static final String PASSWORD = "Passwerd1";
    private static final String PASSWORD_2 = "Passwerd2";

    private User user;

    @Before
    public void setup() {
        user = new User();
        user.setUserName(USER_NAME);
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setTeamId(TEAM_ID);
        user.setAdmin(true);
    }

    @Test
    public void testSuccess() {
        User authenticatedUser = service.authenticate(EMAIL, PASSWORD);
        assertEquals(user, authenticatedUser);
    }

    @Test
    public void testFailure() {
        User failedUser = service.authenticate(EMAIL, PASSWORD + "1");
        assertNull(failedUser);
    }

    @Test
    public void testChangePassword() {
        assertTrue(service.changePassword(EMAIL, PASSWORD, PASSWORD_2));

        assertFalse(service.changePassword(EMAIL, PASSWORD, PASSWORD_2));

        User authenticatedUser = service.authenticate(EMAIL, PASSWORD_2);
        assertEquals(user, authenticatedUser);

        assertTrue(service.changePassword(EMAIL, PASSWORD_2, PASSWORD));

        authenticatedUser = service.authenticate(EMAIL, PASSWORD);
        assertEquals(user, authenticatedUser);
    }
}
