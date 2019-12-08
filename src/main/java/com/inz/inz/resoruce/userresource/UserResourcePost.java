package com.inz.inz.resoruce.userresource;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserResourcePost {
    @Size(min = 4, max = 10)
    @NotNull
    private String username;


    @Size(min = 4, max = 20)
    @NotNull
    private String password;


    @Size(min = 4, max = 10)
    private String firstname;


    @Size(min = 4, max = 10)
    private String lastname;


    @Size(min = 4, max = 30)
    @NotNull
    @Email
    private String email;
}
