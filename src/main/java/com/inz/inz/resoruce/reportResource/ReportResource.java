package com.inz.inz.resoruce.reportResource;

import lombok.Data;

@Data
public class ReportResource extends ReportLight {


    private String reportDate;

    private ReportRatingResource reportRating;

    private Double mark;

    private Long userId;

    private String video;

}
