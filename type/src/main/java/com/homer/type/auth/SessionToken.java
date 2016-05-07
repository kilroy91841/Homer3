package com.homer.type.auth;

import com.google.common.base.Objects;
import com.homer.type.BaseObject;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by arigolub on 5/1/16.
 */
@Table(name = "session_tokens", schema = "homer")
public class SessionToken extends BaseObject {

    @Column
    private String userName;
    @Column
    private long teamId;
    @Column
    private String token;
    @Column
    private DateTime expirationDateUTC;

    public SessionToken() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SessionToken that = (SessionToken) o;
        return Objects.equal(userName, that.userName) &&
                Objects.equal(token, that.token) &&
                Objects.equal(teamId, that.teamId) &&
                Objects.equal(expirationDateUTC, that.expirationDateUTC);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), userName, token, teamId, expirationDateUTC);
    }

    @Override
    public String toString() {
        return "SessionToken{" +
                "userName='" + userName + '\'' +
                ", teamId="+ teamId +
                ", expirationDateUTC=" + expirationDateUTC +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public DateTime getExpirationDateUTC() {
        return expirationDateUTC;
    }

    public void setExpirationDateUTC(DateTime expirationDateUTC) {
        this.expirationDateUTC = expirationDateUTC;
    }
}
