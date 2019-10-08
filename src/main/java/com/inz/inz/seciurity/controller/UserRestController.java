package com.inz.inz.seciurity.controller;

import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.seciurity.AdapterImpl.UserAdapterImpl;
import com.inz.inz.seciurity.Resource.UserResourcePost;
import com.inz.inz.seciurity.jwt.JwtUser;
import com.inz.inz.seciurity.service.UserTokenReciver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class UserRestController {



    @Autowired
    UserAdapterImpl userAdapter;

    @Autowired
    UserTokenReciver userTokenRecvier;

    @GetMapping
    @RequestMapping(value = "user")
    public  JwtUser getAuthenticatedUser(HttpServletRequest request) {
        JwtUser user = userTokenRecvier.getAuthenticatedUser(request);
        return user;
    }

    @PostMapping
    @RequestMapping(value = "addUser")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserResourcePost userResourcePost) throws DbException {
        userAdapter.createUser(userResourcePost);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
