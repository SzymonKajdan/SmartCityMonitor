package com.inz.inz.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationException extends  Throwable {
    private String details;
    private String code;

}