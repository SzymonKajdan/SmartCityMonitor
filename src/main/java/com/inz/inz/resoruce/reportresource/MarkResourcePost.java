package com.inz.inz.resoruce.reportresource;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MarkResourcePost {

    @NotNull
    private Long userId;

    @NotNull
    private Long reportId;

    @NotNull
    private int mark;

}
