package com.inz.inz.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ReportRating")
@Data
public class ReportRatingEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_rating_entity_seq")
    @SequenceGenerator(name = "report_rating_entity_seq", sequenceName = "report_rating_entity_seq", allocationSize = 1)
    private Long id;

    private int  marks;

    private int quantity;

    private int notActiveCounter;
}
