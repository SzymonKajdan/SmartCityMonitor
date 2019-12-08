package com.inz.inz.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationException extends Exception {
    private final String details;
    private final String code;

}
