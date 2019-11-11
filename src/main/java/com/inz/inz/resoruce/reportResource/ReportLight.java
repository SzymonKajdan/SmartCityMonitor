package com.inz.inz.resoruce.reportResource;

import lombok.Data;

@Data
public class ReportLight {
    private Long id;

    private String longitude;

    private String latitude;

    private String description;

    private String photo;

    private String video;

    private Boolean isActive;

}
