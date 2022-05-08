package com.t3h.land2110e.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatingUserRequest {
    @JsonProperty(required = true)
    private String username;
    private String email;
    @JsonProperty(required = false)
    private String password;
    @JsonProperty(required = true)
    private String avatar;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
