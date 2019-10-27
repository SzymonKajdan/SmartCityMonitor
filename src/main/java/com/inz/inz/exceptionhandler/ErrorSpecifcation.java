package com.inz.inz.exceptionhandler;

public enum ErrorSpecifcation {
    NOTSUPPORTEDMETHOD("Method not supported","0001"),
    USEREXIST("User alredy exist","0002"),
    CREATINGERROR("Error at Saving ","0003"),
    RESURCENOTEXIST("Resource exist","0004"),
    OPERATIONNOTALLOWED("Operation not allowed","0006"),
    SENDINGEMAIL("Error at sending emial try again later","0007"),
    USERNOTEXIST("User with this email not exist","0008"),
    NOTFOUND("Resorcue not found ","0009");

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
