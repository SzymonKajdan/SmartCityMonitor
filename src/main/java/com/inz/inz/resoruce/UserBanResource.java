package com.inz.inz.resoruce;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserBanResource {
    @NotNull
    private Long id;
}
