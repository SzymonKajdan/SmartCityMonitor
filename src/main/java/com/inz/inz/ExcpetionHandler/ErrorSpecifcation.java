package com.inz.inz.ExcpetionHandler;

public enum ErrorSpecifcation {
    NOTSUPPORTEDMETHOD("Method not supported","0001"),
    USEREXIST("User alredy exist","0002"),
    CREATINGERROR("Error at Saving ","0003"),
    RESURCENOTEXIST("Resource exist","0004"),
    OPERATIONNOTALLOWED("Operation not allowed","0006");


    private String details;
    private String code;

    ErrorSpecifcation(String details, String code) {
        this.details=details;
        this.code=code;
    }

    public  String getDetails(){
        return  details;
    }
    public String getCode(){
        return code;
    }
}
