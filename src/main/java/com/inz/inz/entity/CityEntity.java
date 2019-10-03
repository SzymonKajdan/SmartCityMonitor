package com.inz.inz.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CityEntity")
@Data
public class CityEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_entity_seq")
    @SequenceGenerator(name = "city_entity_seq", sequenceName = "city_entity_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String longitude;

    private String latitude;

    @OneToMany
    List<ReportEntity> reportList;

}
