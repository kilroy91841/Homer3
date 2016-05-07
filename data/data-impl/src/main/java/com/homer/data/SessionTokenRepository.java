package com.homer.data;

import com.google.common.collect.Maps;
import com.homer.data.common.ISessionTokenRepository;
import com.homer.type.auth.SessionToken;
import com.homer.util.data.BaseRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.annotation.Nullable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

/**
 * Created by arigolub on 5/1/16.
 */
public class SessionTokenRepository extends BaseRepository<SessionToken> implements ISessionTokenRepository {

    private static SecureRandom RANDOM = new SecureRandom();
    private static final int DAYS_UNTIL_EXPIRATION = 30;

    public SessionTokenRepository() {
        super(SessionToken.class);
    }

    @Override
    public String createForUser(String userName, long teamId) {
        SessionToken sessionToken = new SessionToken();
        sessionToken.setUserName(userName);
        sessionToken.setTeamId(teamId);
        sessionToken.setToken(nextSessionToken());
        sessionToken.setExpirationDateUTC(DateTime.now(DateTimeZone.UTC).plusDays(DAYS_UNTIL_EXPIRATION));
        return super.upsert(sessionToken).getToken();
    }

    @Nullable
    @Override
    public SessionToken getByToken(String token) {
        Map<String, Object> filters = Maps.newHashMap();
        filters.put("token", token);
        return super.get(filters);
    }

    private static String nextSessionToken() {
        return new BigInteger(130, RANDOM).toString(32);
    }
}
