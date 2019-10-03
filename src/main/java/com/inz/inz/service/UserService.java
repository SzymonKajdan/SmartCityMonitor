package com.inz.inz.service;


import com.inz.inz.seciurity.model.User;

public interface UserService {

    void saveWorker(User user);
    void saveManager(User user);
    void delete(User user);
    String generatePassword();
    String ecryptPassword(String str);
}
