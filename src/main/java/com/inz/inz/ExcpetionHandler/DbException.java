package com.inz.inz.ExcpetionHandler;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class DbException extends ExceptionModel {

    private HttpStatus httpStatus;
    public DbException(String details, String code, Field field) {
    super(details,code,field);
    }
    public DbException(String details, String code, Field field,HttpStatus httpStatus) {
        super(details,code,field);
        this.httpStatus=httpStatus;
    }
    public DbException(){
    }
}
