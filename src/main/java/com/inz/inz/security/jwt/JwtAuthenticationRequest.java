package com.inz.inz.security.jwt;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter@Setter
public class  JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    private String username;
    private String password;

    public JwtAuthenticationRequest() {
        super();
    }

    public JwtAuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }
}
