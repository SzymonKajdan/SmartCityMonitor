package com.inz.inz.adapter;

import com.inz.inz.entity.User;
import com.inz.inz.exceptionhandler.AuthenticationException;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.resoruce.UserBanResource;
import com.inz.inz.resoruce.UsersLight;
import com.inz.inz.resoruce.userresource.*;
import com.inz.inz.resoruce.userresource.LogInResourcePost;
import com.inz.inz.resoruce.userresource.LogInUserResponse;

import java.util.List;

public interface UserAdapter {
    User createUser(UserResourcePost user) throws DbException;

    UserAuthResoruce mapUserAuthResource(User user);

    List<UserRank> getRank();

    void sendNewPassword(String email) throws DbException;

    void  editUser(EditUserResource user) throws DbException;

    void changePassword(PasswordResourcePost password) throws DbException;

    LogInUserResponse authUser(LogInResourcePost authenticationRequest) throws AuthenticationException;

    List<UsersLight> getUsers();

    void banUser(UserBanResource userBanResource) throws DbException;

    void unBanUser(UserBanResource userBanResource) throws DbException;
}
