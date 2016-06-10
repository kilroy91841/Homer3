package com.homer.service.auth;

import com.google.common.base.Objects;

/**
 * Created by arigolub on 5/1/16.
 */
public class User {

    private String token;
    private String email;
    private String userName;
    private String firstName;
    private String lastName;
    private long teamId;
    private boolean admin;
    private boolean testUser;

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return teamId == user.teamId &&
                Objects.equal(token, user.token) &&
                Objects.equal(email, user.email) &&
                Objects.equal(userName, user.userName) &&
                Objects.equal(firstName, user.firstName) &&
                Objects.equal(lastName, user.lastName) &&
                Objects.equal(admin, user.admin) &&
                Objects.equal(testUser, user.testUser);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(token, email, userName, firstName, lastName, teamId, admin, testUser);
    }

    @Override
    public String toString() {
        return "User{" +
                "token='" + token + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", admin='" + admin + '\'' +
                ", teamId=" + teamId +
                ", testUser=" + testUser +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isTestUser() {
        return testUser;
    }

    public void setTestUser(boolean testUser) {
        this.testUser = testUser;
    }
}
