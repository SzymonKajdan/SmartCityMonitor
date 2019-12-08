package com.inz.inz.resoruce.reportresource;

import lombok.Data;

@Data
public class ReportLight {
    private Long id;

    private String longitude;

    private String latitude;

    private String description;

    private String photo;

    private String reportType;

    private Boolean isActive;

    private String reportDate;
}
