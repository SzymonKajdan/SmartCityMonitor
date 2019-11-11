package com.inz.inz.security.controller;

import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.security.AdapterImpl.UserAdapterImpl;
import com.inz.inz.security.Resource.UserRank;
import com.inz.inz.security.Resource.UserResourcePost;
import com.inz.inz.security.jwt.JwtUser;
import com.inz.inz.security.service.UserTokenReciver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class UserRestController {


    @Autowired
    UserAdapterImpl userAdapter;

    @Autowired
    UserTokenReciver userTokenRecvier;

    @GetMapping
    @RequestMapping(value = "user")
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        JwtUser user = userTokenRecvier.getAuthenticatedUser(request);
        return user;
    }

    @PostMapping
    @RequestMapping(value = "addUser")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserResourcePost userResourcePost) throws DbException {
        userAdapter.createUser(userResourcePost);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    @RequestMapping("getRank")
    public ResponseEntity<List<UserRank>> getRank() {
        return new ResponseEntity<>(userAdapter.getRank(), HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "resetPassword/{emailAdddres}")
    public ResponseEntity<?> resetPassword(@PathVariable(required = true) String emailAdddres) throws DbException {
        userAdapter.sendNewPassword(emailAdddres);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }


}
