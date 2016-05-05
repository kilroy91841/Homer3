package com.homer.auth.stormpath;

import com.homer.service.auth.IAuthService;
import com.homer.service.auth.User;
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
import com.stormpath.sdk.resource.ResourceException;
import com.stormpath.sdk.tenant.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 5/1/16.
 */
public class StormpathAuthService implements IAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StormpathAuthService.class);

    private static Client client;

    private static final String APPLICATION = "HomerAtTheBat";
    private static final String TEAM_ID = "teamId";
    private static final String FILE_NAME = "apiKey.properties";

    private StormpathAuthService() throws ConfigurationException {
        PropertiesConfiguration config = new PropertiesConfiguration(FILE_NAME);
        String path = config.getPath();

        ApiKey apiKey = ApiKeys.builder().setFileLocation(path).build();
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

//    public User getUsers() {
//        Application application = getApplication();)
//        AccountList accounts = application.getAccounts();
//    }

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

    private Application getApplication() {
        Tenant tenant = client.getCurrentTenant();
        ApplicationList applications = tenant.getApplications(Applications.where(Applications.name().eqIgnoreCase(APPLICATION)));
        return applications.iterator().next();
    }

    private static long getTeamId(Account account) {
        if (account.getCustomData().containsKey(TEAM_ID)) {
            return Long.parseLong(account.getCustomData().get(TEAM_ID).toString());
        }
        return -1;
    }
}
