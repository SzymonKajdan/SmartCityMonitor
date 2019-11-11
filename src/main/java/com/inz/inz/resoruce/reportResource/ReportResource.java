package com.inz.inz.resoruce.reportResource;

import com.inz.inz.entity.enums.ReportType;
import lombok.Data;

@Data
public class ReportResource extends ReportLight {

    private ReportType reportType;

    private String reportDate;

    private ReportRatingResource reportRating;

    private Double mark;


    private Long userId;

}
