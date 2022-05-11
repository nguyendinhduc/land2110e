package com.t3h.land2110e.security;

import com.t3h.land2110e.model.response.UserContext;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private UserContext userContext;

    public JwtAuthenticationToken(UserContext userContext) {
        super(null);
        this.userContext = userContext;
    }

    @Override
    public Object getCredentials() {
        return userContext;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
