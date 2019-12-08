package com.inz.inz.resoruce.reportresource;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NotActiveResource {
    @NotNull
    private Long reportId;
    @NotNull
    private Long userId;
}
