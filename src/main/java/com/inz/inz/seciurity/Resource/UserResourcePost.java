package com.inz.inz.seciurity.Resource;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserResourcePost {
    @Size(min = 4, max = 50)
    @NotNull
    private String username;

    @Column(name = "PASSWORD", length = 100)
    @Size(min = 4, max = 100)
    @NotNull
    private String password;

    @Column(name = "FIRSTNAME", length = 50)
    @Size(min = 4, max = 50)
    private String firstname;

    @Column(name = "LASTNAME", length = 50)
    @Size(min = 4, max = 50)
    private String lastname;

    @Column(name = "EMAIL", length = 50, unique = true)
    @Size(min = 4, max = 50)
    @NotNull
    private String email;
}
