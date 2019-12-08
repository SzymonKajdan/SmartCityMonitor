package com.inz.inz.controller.usercontroller;

import com.inz.inz.adapter.adapterimpl.UserAdapterImpl;
import com.inz.inz.exceptionhandler.AuthenticationException;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.resoruce.UserBanResource;
import com.inz.inz.resoruce.UsersLight;
import com.inz.inz.resoruce.userresource.*;
import com.inz.inz.security.service.UserTokenReciver;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping(value = {"/", "/api/"})

public class UserRestController {


    @Autowired
    UserAdapterImpl userAdapter;

    @Autowired
    UserTokenReciver userTokenRecvier;

    private static final String MEDIA_TYPE = "application/json";

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "users",consumes = MEDIA_TYPE, produces = MEDIA_TYPE)
    public ResponseEntity<List<UsersLight>> getAuthenticatedUser() {

        return new ResponseEntity<>(userAdapter.getUsers(), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "userBan", consumes = MEDIA_TYPE,produces = MEDIA_TYPE)
    public ResponseEntity banUser(@Valid @RequestBody UserBanResource userBanResource) throws DbException {
        userAdapter.banUser(userBanResource);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "userUnBan", produces = MEDIA_TYPE)
    public ResponseEntity unBanUser(@Valid @RequestBody UserBanResource userBanResource) throws DbException {
        userAdapter.unBanUser(userBanResource);
        return  new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping(value = "signUp", produces = MEDIA_TYPE)
    public ResponseEntity addUser(@Valid @RequestBody UserResourcePost userResourcePost) throws DbException {
        userAdapter.createUser(userResourcePost);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "getRank", produces = MEDIA_TYPE)
    public ResponseEntity<List<UserRank>> getRank() {
        return new ResponseEntity<>(userAdapter.getRank(), HttpStatus.OK);
    }

    @PostMapping(value = "resetPassword/{emailAdddres}", produces = MEDIA_TYPE)

    public ResponseEntity resetPassword(@PathVariable(required = true) String emailAdddres) throws DbException {
        userAdapter.sendNewPassword(emailAdddres);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping(value = "editUser", produces = MEDIA_TYPE)
    public ResponseEntity editUser(@Valid @RequestBody EditUserResource userResourcePost) throws DbException {
        userAdapter.editUser(userResourcePost);
        return new ResponseEntity(HttpStatus.CREATED);
    }


    @PostMapping(value = "changePassword", produces = MEDIA_TYPE)
    public ResponseEntity changePassword(@Valid @RequestBody PasswordResourcePost passwordResourcePost) throws DbException {
        userAdapter.changePassword(passwordResourcePost);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(value = "logIn", produces = MEDIA_TYPE)
    public ResponseEntity createAuthenticationToken(@RequestBody @Valid LogInResourcePost authenticationRequest) throws AuthenticationException {

        LogInUserResponse response = userAdapter.authUser(authenticationRequest);

        return ResponseEntity.ok(response);
    }
}
