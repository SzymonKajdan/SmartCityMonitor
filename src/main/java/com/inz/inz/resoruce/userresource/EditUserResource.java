package com.inz.inz.resoruce.userresource;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class EditUserResource {
    @NotNull
    private Long id;
    @Size(min = 4,max = 10)
    private String username;
    @Size(min = 4,max = 10)
    private String firstname;

    @Size(min = 4,max = 10)
    private String surname;

    @Email
    @Size(min = 4,max = 30)
    private String email;

}
