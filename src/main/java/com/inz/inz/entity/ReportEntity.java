package com.inz.inz.entity;


import com.inz.inz.entity.enums.ReportType;
import com.inz.inz.security.model.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ReportEntity")
@Data
public class ReportEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_entity_seq")
    @SequenceGenerator(name = "report_entity_seq", sequenceName = "report_entity_seq", allocationSize = 1)
    private Long id;

    private String longitude;

    private String latitude;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String photo;

    private String video;

    private Date dateReport;

    private ReportType reportType;

    @OneToOne
    private ReportRatingEntity reportRating;

    @ManyToOne
    User user;

    @ManyToOne
    CityEntity city;
}
