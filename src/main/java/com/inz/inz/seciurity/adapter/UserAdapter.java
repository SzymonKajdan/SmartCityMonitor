package com.inz.inz.seciurity.adapter;

import com.inz.inz.ExcpetionHandler.DbException;
import com.inz.inz.seciurity.Resource.UserAuthResoruce;
import com.inz.inz.seciurity.Resource.UserResourcePost;
import com.inz.inz.seciurity.model.User;

public interface UserAdapter {
     User  createUser(UserResourcePost user) throws DbException;

     UserAuthResoruce mapUserAuthResource(User user);
}
