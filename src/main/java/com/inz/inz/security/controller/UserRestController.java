package com.inz.inz.security.controller;

import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.security.AdapterImpl.UserAdapterImpl;
import com.inz.inz.resoruce.userResource.EditUserResource;
import com.inz.inz.resoruce.userResource.PasswordResourcePost;
import com.inz.inz.resoruce.userResource.UserRank;
import com.inz.inz.resoruce.userResource.UserResourcePost;
import com.inz.inz.security.jwt.JwtUser;
import com.inz.inz.security.service.UserTokenReciver;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Log4j2
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
        log.info("add");
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

    @PostMapping
    @RequestMapping(value = "editUser")
    public ResponseEntity<?> editUser(@Valid @RequestBody EditUserResource userResourcePost) throws DbException {
        userAdapter.editUser(userResourcePost);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping
    @RequestMapping(value = "changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordResourcePost passwordResourcePost) throws DbException {
        userAdapter.changePassword(passwordResourcePost);
        return new ResponseEntity(HttpStatus.OK);
    }

}
