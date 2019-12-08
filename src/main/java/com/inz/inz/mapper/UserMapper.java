package com.inz.inz.mapper;

import com.inz.inz.repository.AuthorityRepository;
import com.inz.inz.resoruce.UserBanResource;
import com.inz.inz.resoruce.UsersLight;
import com.inz.inz.resoruce.userresource.EditUserResource;
import com.inz.inz.resoruce.userresource.UserAuthResoruce;
import com.inz.inz.resoruce.userresource.UserRank;
import com.inz.inz.resoruce.userresource.UserResourcePost;
import com.inz.inz.entity.Authority;
import com.inz.inz.entity.AuthorityName;
import com.inz.inz.entity.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    ReportMapper reportMapper;


    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    @Mappings({
            @Mapping(target = "username", source = "user.username"),
            @Mapping(target = "surname", source = "user.lastname"),
            @Mapping(target = "firstname", source = "user.firstname"),
            @Mapping(target = "email", source = "user.email"),
            @Mapping(target = "reports", ignore = true),
            @Mapping(target = "id", source = "user.id")
    })
    public abstract UserAuthResoruce mapToUserAuthResource(User user);


    @Mappings({
            @Mapping(target = "username", source = "user.username"),
            @Mapping(target = "quantity", source = "user.userRatingEntity.quantity")
    })
    public abstract UserRank mapToUserRank(User user);

    @Mappings({
            @Mapping(target = "email", source = "userResourcePost.email"),
            @Mapping(target = "lastname", source = "userResourcePost.lastname"),
            @Mapping(target = "firstname", source = "userResourcePost.firstname"),
            @Mapping(target = "username", source = "userResourcePost.username"),


    })
    public abstract User mapUserResourcePostToUser(UserResourcePost userResourcePost);


    @Mappings({
            @Mapping(target = "email", source = "editUserResource.email"),
            @Mapping(target = "lastname", source = "editUserResource.surname"),
            @Mapping(target = "firstname", source = "editUserResource.firstname"),
            @Mapping(target = "username", source = "editUserResource.username"),
            @Mapping(target = "id", source = "editUserResource.id")

    })
    public abstract User userToUpdateMapToUser(EditUserResource editUserResource);

    @Mappings({
            @Mapping(target = "email", source = "user.email"),
            @Mapping(target = "id", source = "user.id"),
            @Mapping(target = "username", source = "user.username"),
            @Mapping(target = "isBanned", expression = "java(user.getBanEntity().isBanned())")
    })
    public abstract UsersLight mapToUserLigth(User user);

    @Mapping(target = "id",source = "userBanResource.id")
    public  abstract  User mapToUser(UserBanResource userBanResource);
    @AfterMapping
    protected void fillMark(User user, @MappingTarget UserRank userRank) {
        userRank.setAvgMark(user.getUserRatingEntity().getQuantity() != 0
                ? Math.round((double) user.getUserRatingEntity().getMarks() / (double) user.getUserRatingEntity().getQuantity()) : 0.0);

    }

    @AfterMapping
    protected void fillUserRating(User user, @MappingTarget UserAuthResoruce userAuthResoruce) {

        if (user.getBanEntity() != null) {
            userAuthResoruce.setIsBanned(user.getBanEntity().isBanned());
        }
        if (user.getUserRatingEntity() != null) {
            double rating = user.getUserRatingEntity().getQuantity() != 0
                    ? (double) user.getUserRatingEntity().getMarks() / (double) user.getUserRatingEntity().getQuantity() : 0.0;
            userAuthResoruce.setUserRating(rating);
        }
    }

    @AfterMapping
    protected void fillUserReports(User user, @MappingTarget UserAuthResoruce userAuthResoruce) {
        if (user.getReportsList() != null) {
            userAuthResoruce.setReports(user.getReportsList().stream().map(reportMapper::mapToReportLigth).collect(Collectors.toList()));
        } else {
            userAuthResoruce.setReports(new ArrayList<>());
        }
    }


    @AfterMapping
    protected void fillUser(UserResourcePost post, @MappingTarget User user) {

        Authority userRole = authorityRepository.findByName(AuthorityName.ROLE_USER);
        user.setEnabled(true);
        user.setAuthorities(Collections.singletonList(userRole));
        user.setPassword(passwordEncoder.encode(post.getPassword()));
        user.setLastPasswordResetDate(new Date());
    }
}
