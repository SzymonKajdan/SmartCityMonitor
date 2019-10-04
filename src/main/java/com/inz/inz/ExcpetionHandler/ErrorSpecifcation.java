package com.inz.inz.ExcpetionHandler;

public enum ErrorSpecifcation {
    NOTSUPPORTEDMETHOD("Method not supported","0001"),
    USEREXIST("User alredy exist","0002"),
    CREATINGERROR("Error at Saving ","0003"),
    RESURCENOTEXIST("Resource exist","0004");

    ErrorSpecifcation(String details, String code) {

    }
    public  String getDetails(){
        return  this.getDetails();
    }
    public String getCode(){
        return this.getCode();
    }
}
