package com.inz.inz.seciurity.model;

import com.inz.inz.entity.BanEntity;
import com.inz.inz.entity.ReportEntity;
import com.inz.inz.entity.UserRatingEntity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "UserEntity")
@Data
public class User {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    @Column(name = "USERNAME", length = 50, unique = true)

    @Size(min = 4, max = 50)
    private String username;

    @Column(name = "PASSWORD", length = 100)

    @Size(min = 4, max = 100)
    private String password;

    @Column(name = "FIRSTNAME", length = 50)

    @Size(min = 4, max = 50)
    private String firstname;

    @Column(name = "LASTNAME", length = 50)

    @Size(min = 4, max = 50)
    private String lastname;

    @Column(name = "EMAIL", length = 50, unique = true)
    @Size(min = 4, max = 50)
    private String email;

    @Column(name = "ENABLED")
    private Boolean enabled;

    @Column(name = "LASTPASSWORDRESETDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordResetDate;

    @OneToOne
    BanEntity banEntity;

    @OneToOne
    UserRatingEntity userRatingEntity;

    @OneToMany
    List<ReportEntity> reportsList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    private List<Authority> authorities;
}