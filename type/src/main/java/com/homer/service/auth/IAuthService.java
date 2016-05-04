package com.homer.service.auth;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 5/1/16.
 */
public interface IAuthService {

    @Nullable
    User authenticate(String userName, String password);
}
