package com.inz.inz.adapter;

import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.resoruce.userResource.*;
import com.inz.inz.entity.User;

import java.util.List;

public interface UserAdapter {
    User createUser(UserResourcePost user) throws DbException;

    UserAuthResoruce mapUserAuthResource(User user);

    List<UserRank> getRank();

    void sendNewPassword(String email) throws DbException;

    void  editUser(EditUserResource user) throws DbException;

    void changePassword(PasswordResourcePost password) throws DbException;

}
