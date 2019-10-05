package com.inz.inz.seciurity.Resource;

import com.inz.inz.resoruce.ReportLight;
import lombok.Data;

import java.util.List;

@Data
public class UserAuthResoruce {
    private String username;

    private String firstname;

    private String surname;

    private String email;

    private Boolean isBanned;

    private Integer userRating;

    private List<ReportLight> reports;
}
