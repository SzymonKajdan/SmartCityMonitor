package com.inz.inz.security.service;

import com.inz.inz.exceptionhandler.DbException;

public interface EmailService {
    void sendPassword(String adress, String subject, String content) throws DbException;

    void resetPassword(String email) throws DbException;
}
