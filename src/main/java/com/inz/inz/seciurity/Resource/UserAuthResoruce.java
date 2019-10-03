package com.inz.inz.seciurity.Resource;

import lombok.Data;

@Data
public class UserAuthResoruce {
    private String username;

    private String firstname;

    private String surname;

    private String email;

    private Boolean isBanned;

    private Integer userRating;
}
