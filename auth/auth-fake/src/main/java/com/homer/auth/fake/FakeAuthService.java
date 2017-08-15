package com.homer.auth.fake;

import com.google.common.collect.Lists;
import com.homer.service.auth.IAuthService;
import com.homer.service.auth.User;
import com.homer.util.core.$;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Nullable;
import java.io.*;
import java.util.List;

/**
 * Created by arigolub on 8/15/17.
 */
public class FakeAuthService implements IAuthService {

    private static final String FILE_NAME = "/users.json";

    private List<User> users = Lists.newArrayList();

    private FakeAuthService() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        InputStream stream = this.getClass().getResourceAsStream(FILE_NAME);
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        Object obj = parser.parse(bufferedReader);
        JSONArray users = (JSONArray) obj;
        users.forEach(o -> {
            JSONObject jo = (JSONObject) o;
            String email = (String) jo.get("email");
            String firstName = (String) jo.get("firstName");
            String lastName = (String) jo.get("lastName");
            long teamId = (long) jo.get("teamId");
            boolean admin = (boolean) jo.get("admin");
            User user = new User();
            user.setEmail(email);
            user.setUserName(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setTeamId(teamId);
            user.setAdmin(admin);
            this.users.add(user);
        });
    }

    public static class FACTORY {

        private static FakeAuthService instance;

        @Nullable
        public static FakeAuthService getInstance() {
            if (instance == null) {
                try {
                    instance = new FakeAuthService();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return instance;
        }
    }

    @Nullable
    @Override
    public User authenticate(String userName, String password) {
        return $.of(FACTORY.getInstance().users).first(u -> userName.equals(u.getEmail()));
    }

    @Override
    public boolean changePassword(String userName, String oldPassword, String newPassword) {
        return true;
    }

    @Override
    public boolean sendPasswordResetEmail(String email) {
        return false;
    }

    @Override
    public List<User> getUsers() {
        return FACTORY.getInstance().users;
    }
}
