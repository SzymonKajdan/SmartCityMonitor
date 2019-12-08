package com.inz.inz.emailservice;

import com.inz.inz.exceptionhandler.DbException;

public interface EmailService {
    void sendPassword(String adress, String subject, String content) throws DbException;

    void resetPassword(String email) throws DbException;
}
