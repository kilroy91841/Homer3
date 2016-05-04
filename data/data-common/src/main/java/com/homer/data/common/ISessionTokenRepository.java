package com.homer.data.common;

import com.homer.type.auth.SessionToken;
import com.homer.util.core.data.IRepository;

import javax.annotation.Nullable;

/**
 * Created by arigolub on 5/1/16.
 */
public interface ISessionTokenRepository extends IRepository<SessionToken> {

    String createForUser(String userName);
    @Nullable
    SessionToken getByToken(String token);
}
