package com.inz.inz.service;

import com.inz.inz.entity.User;
import org.springframework.stereotype.Service;
@Service("userService")
public class UserServiceImpl implements UserService {


    @Override
    public void saveWorker(User user) {

    }

    @Override
    public void saveManager(User user) {

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public String generatePassword() {
return "" ;}

    public String ecryptPassword(String str){
        return "";
    }
}
