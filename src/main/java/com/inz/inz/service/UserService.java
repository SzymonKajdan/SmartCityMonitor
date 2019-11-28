package com.inz.inz.service;


import com.inz.inz.entity.User;

public interface UserService {

    void saveWorker(User user);
    void saveManager(User user);
    void delete(User user);
    String generatePassword();
    String ecryptPassword(String str);
}
