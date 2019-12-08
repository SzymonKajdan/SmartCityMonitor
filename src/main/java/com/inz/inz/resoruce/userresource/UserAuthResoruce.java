package com.inz.inz.resoruce.userresource;

import com.inz.inz.resoruce.reportresource.ReportLight;
import lombok.Data;

import java.util.List;

@Data
public class UserAuthResoruce {

    private Long id;

    private String username;

    private String firstname;

    private String surname;

    private String email;

    private Boolean isBanned;

    private Double userRating;

    private List<ReportLight> reports;
}
