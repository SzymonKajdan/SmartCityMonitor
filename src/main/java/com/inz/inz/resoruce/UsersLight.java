package com.inz.inz.resoruce;

import lombok.Data;

@Data
public class UsersLight {
    private Long id ;

    private String username;

    private String email;

    private Boolean isBanned;
}
