package com.inz.inz.resoruce.reportResource;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ReportResourcePost {

    @Size(max = 10)
    @NotNull
    private String longitude;

    @Size(max = 10)
    @NotNull
    private String latitude;

    @Size(max=255)
    @NotNull
    private String description;

    @Nullable
    private String photo;

    @Nullable
    private String video;

    @NotNull
    private String reportType;

    @NotNull
    @Size(max = 30,min = 3)
    private String cityName;
}
