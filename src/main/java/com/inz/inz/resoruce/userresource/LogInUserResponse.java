package com.inz.inz.resoruce.userresource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class LogInUserResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;

    private final Date expirationDate;

    private final UserAuthResoruce user ;


}
