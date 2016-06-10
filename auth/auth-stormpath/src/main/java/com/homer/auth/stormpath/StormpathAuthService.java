package com.homer.auth.stormpath;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.homer.service.auth.IAuthService;
import com.homer.service.auth.User;
import com.homer.util.EnvironmentUtility;
import com.homer.util.core.$;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.account.AccountCriteria;
import com.stormpath.sdk.account.AccountList;
import com.stormpath.sdk.account.Accounts;
import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.api.ApiKeys;
import com.stormpath.sdk.authc.*;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.ClientBuilder;
import com.stormpath.sdk.client.Clients;
import com.stormpath.sdk.application.*;
import com.stormpath.sdk.provider.ProviderAccountRequest;
import com.stormpath.sdk.resource.ResourceException;
import com.stormpath.sdk.tenant.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by arigolub on 5/1/16.
 */
public class StormpathAuthService implements IAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StormpathAuthService.class);

    private static Client client;

    private static final String TEAM_ID = "teamId";
    private static final String FILE_NAME = "apiKey.properties";
    private static final String APIKEY_ID = "apiKey.id";
    private static final String APIKEY_SECRET = "apiKey.secret";

    private static List<User> users = null;

    private StormpathAuthService() throws ConfigurationException {
        PropertiesConfiguration config = new PropertiesConfiguration(FILE_NAME);

        Properties properties = new Properties();
        properties.setProperty(APIKEY_ID, config.getString(APIKEY_ID));
        properties.setProperty(APIKEY_SECRET, config.getString(APIKEY_SECRET));

        ApiKey apiKey = ApiKeys.builder().setProperties(properties).build();
        ClientBuilder builder = Clients.builder().setApiKey(apiKey);
        client = builder.build();
    }

    public static class FACTORY {

        private static StormpathAuthService instance;

        @Nullable
        public static StormpathAuthService getInstance() {
            if (instance == null) {
                try {
                    instance = new StormpathAuthService();
                } catch (ConfigurationException e) {
                    return null;
                }
            }
            return instance;
        }
    }

    @Override
    public List<User> getUsers() {
        if (users != null) {
            return users;
        }
        Application application = getApplication();
        AccountList accounts = application.getAccounts();
        users = Lists.newArrayList();
        for(Account account : accounts) {
            users.add(toUser(account));
        }
        return users;
    }

    @Nullable
    @Override
    public User authenticate(String userName, String password) {
        LOGGER.info("Authenticating " + userName);
        try {
            Application application = getApplication();
            BasicAuthenticationOptions options = UsernamePasswordRequests.options().withAccount();
            AuthenticationRequest authenticationRequest = UsernamePasswordRequests.builder()
                    .setUsernameOrEmail(userName)
                    .setPassword(password)
                    .withResponseOptions(options)
                    .build();
            AuthenticationResult result = application.authenticateAccount(authenticationRequest);

            Account account = result.getAccount();
            User user = toUser(account);

            LOGGER.info("Authentication successful for " + userName);
            return user;
        } catch (ResourceException ex) {
            LOGGER.error(
                    String.format("Unable to authenticate user. Code: %s\nMessage: %s\nDeveloperMessage: %s",
                            ex.getCode(), ex.getMessage(), ex.getDeveloperMessage())
            );
        } catch (Exception e) {
            LOGGER.error("Unexpected error while authenticating: " + e.getMessage());
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Override
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        LOGGER.info("Changing password for " + email);
        try {
            //Verify old password is correct.
            User user = authenticate(email, oldPassword);
            if (user == null) {
                LOGGER.info("Authentication failed, cannot change password for " + email);
                return false;
            }

            //Set new password
            Application application = getApplication();
            Map<String, Object> map = Maps.newHashMap();
            map.put("email", email);
            Account account = $.of(application.getAccounts(map)).first();
            if (account == null) {
                throw new IllegalArgumentException("No account for email " + email);
            }
            account.setPassword(newPassword);
            account.save();

            return true;
        } catch (ResourceException ex) {
            LOGGER.error(
                    String.format("Unable to save new password. Code: %s\nMessage: %s\nDeveloperMessage: %s",
                            ex.getCode(), ex.getMessage(), ex.getDeveloperMessage())
            );
        } catch (Exception e) {
            LOGGER.error("Unexpected error while authenticating: " + e.getMessage());
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

    private Application getApplication() {
        Tenant tenant = client.getCurrentTenant();
        ApplicationList applications = tenant.getApplications(Applications.where(Applications.name().eqIgnoreCase(EnvironmentUtility.getInstance().getStormpathApplication())));
        return applications.iterator().next();
    }

    private static User toUser(Account account) {
        User user = new User();
        user.setUserName(account.getUsername());
        user.setEmail(account.getEmail());
        user.setFirstName(account.getGivenName());
        user.setLastName(account.getSurname());

        if (account.getCustomData().containsKey("teamId")) {
            user.setTeamId(getTeamId(account));
        }
        if (account.getCustomData().containsKey("admin")) {
            user.setAdmin(true);
        }
        if (account.getCustomData().containsKey("testUser")) {
            user.setTestUser(true);
        }
        return user;
    }

    private static long getTeamId(Account account) {
        if (account.getCustomData().containsKey(TEAM_ID)) {
            return Long.parseLong(account.getCustomData().get(TEAM_ID).toString());
        }
        return -1;
    }
}
