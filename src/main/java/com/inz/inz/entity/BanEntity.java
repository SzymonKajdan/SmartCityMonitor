package com.inz.inz.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BanEntity")
@Data
public class BanEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ban_entity_seq")
    @SequenceGenerator(name = "ban_entity_seq", sequenceName = "ban_entity_seq", allocationSize = 1)
    private Long id;

    private int  banCounter;

    private Date date;

    boolean isBanned;

}
