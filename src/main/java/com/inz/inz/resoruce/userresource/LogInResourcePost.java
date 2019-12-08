package com.inz.inz.resoruce.userresource;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@Data
public class LogInResourcePost implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    @NotNull
    private String username;
    @NotNull
    private String password;

    public LogInResourcePost() {
        super();
    }


}
