package com.inz.inz.entity;


import lombok.Data;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "UserVotedEntity")
@Data
public class UserVoted {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_voted_entity_seq")
    @SequenceGenerator(name = "user_voted_entity_seq", sequenceName = "user_voted_entity_seq", allocationSize = 1)
    private Long id;


    @NotNull
    private Long  userId;

    private int typeOfVote;

    private  boolean isMarked;

    private boolean isNotActual;

    private boolean isFalse;
}
