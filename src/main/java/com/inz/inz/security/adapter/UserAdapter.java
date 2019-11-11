package com.inz.inz.security.adapter;

import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.security.Resource.UserAuthResoruce;
import com.inz.inz.security.Resource.UserRank;
import com.inz.inz.security.Resource.UserResourcePost;
import com.inz.inz.security.model.User;

import java.util.List;

public interface UserAdapter {
    User createUser(UserResourcePost user) throws DbException;

    UserAuthResoruce mapUserAuthResource(User user);

    List<UserRank> getRank();

    void sendNewPassword(String email) throws DbException;
}
