package com.t3h.land2110e.model.response;

import com.t3h.land2110e.entity.UserProfileEntity;

public class LoginResponse {
    private String token;
    private UserProfileEntity user;

    public UserProfileEntity getUser() {
        return user;
    }

    public void setUser(UserProfileEntity user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
