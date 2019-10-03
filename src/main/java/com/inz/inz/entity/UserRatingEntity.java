package com.inz.inz.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "UserRatingEntity")
@Data
public class UserRatingEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_rating_entity_seq")
    @SequenceGenerator(name = "user_rating_entity_seq", sequenceName = "user_rating_entity_seq", allocationSize = 1)
    private Long id;

    private int  marks;

    private int quantity;

}
