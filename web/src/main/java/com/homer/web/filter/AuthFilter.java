package com.homer.web.filter;

import com.homer.auth.fake.FakeAuthService;
import com.homer.data.SessionTokenRepository;
import com.homer.service.auth.IUserService;
import com.homer.service.auth.UserService;
import com.homer.web.AuthRequired;
import org.glassfish.jersey.server.ExtendedUriInfo;
import org.glassfish.jersey.server.model.ResourceMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

import java.lang.reflect.Method;

/**
 * Created by arigolub on 5/1/16.
 */
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class);
    private static final String ERROR_MESSAGE = "No Method matched for UriInfo %s";

    private IUserService userService = new UserService(FakeAuthService.FACTORY.getInstance(), new SessionTokenRepository());

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        Method method = getMethod((ExtendedUriInfo)context.getUriInfo());

        String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
        context.setProperty("token", token);
        if (method.isAnnotationPresent(AuthRequired.class)) {
            if (token == null || !userService.hasAccess(token)) {
                throw new NotAuthorizedException("Failed to authorize user");
            }

        }
    }

    private Method getMethod(ExtendedUriInfo uriInfo)
    {
        String message = String.format(ERROR_MESSAGE, uriInfo.getRequestUri());
        ResourceMethod rm = uriInfo.getMatchedResourceMethod();
        if (rm == null)
        {
            LOGGER.error(String.format(message));
            throw new NotFoundException(message);
        }

        Method m = rm.getInvocable().getHandlingMethod();
        if (m == null)
        {
            LOGGER.error(String.format(message));
            throw new NotFoundException(message);
        }
        return m;
    }
}
