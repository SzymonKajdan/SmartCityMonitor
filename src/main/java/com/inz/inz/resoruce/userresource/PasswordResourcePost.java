package com.inz.inz.resoruce.userresource;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PasswordResourcePost {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 4, max = 30)
    private String oldPassword;

    @NotNull
    @Size(min = 4,max = 30)
    private  String newPassword;

}
