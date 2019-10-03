package com.inz.inz.mapper;

import com.inz.inz.repository.AuthorityRepository;
import com.inz.inz.seciurity.Resource.UserAuthResoruce;
import com.inz.inz.seciurity.Resource.UserResourcePost;
import com.inz.inz.seciurity.model.Authority;
import com.inz.inz.seciurity.model.AuthorityName;
import com.inz.inz.seciurity.model.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.Date;

@Mapper(componentModel="spring")
public abstract class  UserMapper {

    @Autowired
    AuthorityRepository authorityRepository;

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Mappings({
            @Mapping(target="username", source="user.username"),
            @Mapping(target="surname", source="user.lastname"),
            @Mapping(target = "firstname",source = "user.firstname"),
            @Mapping(target = "email",source = "user.email"),
    })
   public abstract UserAuthResoruce mapToUserAuthResource(User user);

    @AfterMapping
    protected void fillUserRating(User user, @MappingTarget UserAuthResoruce userAuthResoruce){

        if(user.getBanEntity()!=null){
            userAuthResoruce.setIsBanned(user.getBanEntity().isBanned());
        }
        if (user.getUserRatingEntity() != null) {
            Integer rating = user.getUserRatingEntity().getQuantity() != 0
                    ? user.getUserRatingEntity().getMarks() / user.getUserRatingEntity().getQuantity() : 0;
            userAuthResoruce.setUserRating(rating);
        }
    }

    @Mappings({
            @Mapping(target ="email",source = "userResourcePost.email")    ,
            @Mapping(target = "lastname",source = "userResourcePost.lastname"),
            @Mapping(target = "firstname",source = "userResourcePost.firstname"),
            @Mapping(target = "username",source = "userResourcePost.username"),
            @Mapping(target = "enabled",constant = "true"),

            })
    public abstract User mapUserResourcePostToUser(UserResourcePost userResourcePost);

    @AfterMapping
    protected void fillUser(UserResourcePost post,@MappingTarget User user){

        Authority userRole = authorityRepository.findByName(AuthorityName.ROLE_WORKER);
        user.setAuthorities(Collections.singletonList(userRole));
        user.setPassword(passwordEncoder().encode(post.getPassword()));
        user.setLastPasswordResetDate(new Date());
    }
}
