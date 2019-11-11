package com.inz.inz.security.service;

import com.inz.inz.security.Resource.UserAuthResoruce;

import java.io.Serializable;
import java.util.Date;

public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;

    private final Date expirationDate;

    private final UserAuthResoruce user ;

    public JwtAuthenticationResponse(String token, Date expirationDate, UserAuthResoruce user) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    public String getToken() {
        return this.token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public UserAuthResoruce getUser() {
        return user;
    }
}
