package com.inz.inz.security.adapter;

import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.resoruce.userResource.*;
import com.inz.inz.security.model.User;

import java.util.List;

public interface UserAdapter {
    User createUser(UserResourcePost user) throws DbException;

    UserAuthResoruce mapUserAuthResource(User user);

    List<UserRank> getRank();

    void sendNewPassword(String email) throws DbException;

    void  editUser(EditUserResource user) throws DbException;

    void changePassword(PasswordResourcePost password) throws DbException;

}
