package com.inz.inz.resoruce.reportresource;

import lombok.Data;

@Data
public class ReportResource extends ReportLight {



    private ReportRatingResource reportRating;

    private Double mark;

    private Long userId;

    private String video;

}
