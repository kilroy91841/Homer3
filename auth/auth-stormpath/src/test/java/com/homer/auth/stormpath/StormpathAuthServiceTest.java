package com.homer.auth.stormpath;

import com.homer.service.auth.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by arigolub on 5/1/16.
 */
public class StormpathAuthServiceTest {

    private StormpathAuthService service = StormpathAuthService.FACTORY.getInstance();

    private static final String USER_NAME = "Test User";
    private static final String EMAIL = "testuser@gmail.com";
    private static final long TEAM_ID = 15;
    private static final String FIRST_NAME = "Test";
    private static final String LAST_NAME = "User";

    private static final String PASSWORD = "passwerd";

    private User user;

    @Before
    public void setup() {
        user = new User();
        user.setUserName(USER_NAME);
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setTeamId(TEAM_ID);
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
}
