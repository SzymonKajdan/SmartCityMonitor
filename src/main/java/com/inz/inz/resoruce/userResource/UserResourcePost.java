package com.inz.inz.resoruce.userResource;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserResourcePost {
    @Size(min = 4, max = 50)
    @NotNull
    private String username;


    @Size(min = 4, max = 100)
    @NotNull
    private String password;


    @Size(min = 4, max = 50)
    private String firstname;


    @Size(min = 4, max = 50)
    private String lastname;


    @Size(min = 4, max = 50)
    @NotNull
    @Email
    private String email;
}
