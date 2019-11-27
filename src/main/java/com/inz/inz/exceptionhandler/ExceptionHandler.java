package com.inz.inz.exceptionhandler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.ArrayList;
import java.util.Collections;

@ControllerAdvice
@Log4j2
public class ExceptionHandler {



    @org.springframework.web.bind.annotation.ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ServerModelException> getUnsapportedMethodException(HttpRequestMethodNotSupportedException ex){
        ServerModelException serverModelException=new ServerModelException();
        serverModelException.setCode(ErrorSpecifcation.NOTSUPPORTEDMETHOD.getCode());
        serverModelException.setDetails(ErrorSpecifcation.NOTSUPPORTEDMETHOD.getDetails());

        log.error("Unsupported method call return code "+serverModelException.getCode()+" with Http status"+HttpStatus.METHOD_NOT_ALLOWED);

        return  new ResponseEntity<>(serverModelException, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({DbException.class})
    public  ResponseEntity<ServerModelException> getPSQLException(DbException ex){
        ServerModelException serverModelException= genrateException(ex.getCode(), ex.getCaused(), ex.getField());
        log.error("Failrue  "+serverModelException.getCode()+" with Http status"+HttpStatus.UNPROCESSABLE_ENTITY);
        HttpStatus httpStatus=ex.getHttpStatus()!=null?ex.getHttpStatus():HttpStatus.UNPROCESSABLE_ENTITY;
        return  new ResponseEntity<>(serverModelException,httpStatus);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({MethodArgumentNotValidException.class})
    public  ResponseEntity<ServerModelException> vaildException(MethodArgumentNotValidException ex){
        ServerModelException serverModelException=new ServerModelException();
        serverModelException.setCode("VE");
        serverModelException.setDetails("Valdiation Exception ");
        serverModelException.setFields(new ArrayList<>());
        ex.getBindingResult().getFieldErrors().stream().forEach(x->{
            Field f=new Field();
            f.setDetails(x.getDefaultMessage());
            f.setField(x.getField());
            serverModelException.getFields().add(f);
        });
        log.error("ValidationFailrue "+serverModelException.getCode()+" with Http status"+HttpStatus.UNPROCESSABLE_ENTITY);

        return  new ResponseEntity<>(serverModelException,HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({AuthenticationException.class})
    public  ResponseEntity<ServerModelException> authException(AuthenticationException ex){
        ServerModelException serverModelException=new ServerModelException();
        serverModelException.setCode(ex.getCode());
        serverModelException.setDetails(ex.getDetails());

        log.error("ValidationFailRue "+serverModelException.getCode()+" with Http status"+HttpStatus.UNAUTHORIZED);
        return  new ResponseEntity<>(serverModelException,HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({EnumExcpetion.class})
    public  ResponseEntity<ServerModelException> authException(EnumExcpetion ex){

        ServerModelException serverModelException= genrateException(ex.getCode(), ex.getCaused(), ex.getField());
        log.error("EnumExcetpion "+serverModelException.getCode()+" with Http status"+HttpStatus.UNPROCESSABLE_ENTITY);
        return  new ResponseEntity<>(serverModelException,HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({NumberFormatException.class})
    public  ResponseEntity<ServerModelException>convertExcepiton(NumberFormatException ex){

        ServerModelException serverModelException=new ServerModelException();
        serverModelException.setDetails("Cast fail for "+ex.getMessage());
        serverModelException.setCode("C2");
        return  new ResponseEntity<>(serverModelException,HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({MissingPathVariableException.class})
    public ResponseEntity<ServerModelException> pathParamsExceiption(MissingPathVariableException ex){

        ServerModelException serverModelException=new ServerModelException();
        serverModelException.setCode("VE");
        serverModelException.setDetails("Valdiation Exception Missing "+ex.getVariableName()+" in path.");
        serverModelException.setFields(new ArrayList<>());

        return new ResponseEntity<>(serverModelException,HttpStatus.UNPROCESSABLE_ENTITY);
    }
    private ServerModelException  genrateException(String code, String caused, Field field) {
        ServerModelException serverModelException=new ServerModelException();
        serverModelException.setCode(code);
        serverModelException.setDetails(caused);
        if (field != null) {
            serverModelException.setFields(Collections.singletonList(field));
        } else {
            serverModelException.setFields(new ArrayList<>());
        }
        return serverModelException;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ServerModelException> requestParamException(){
        ServerModelException serverModelException=new ServerModelException();
        serverModelException.setDetails("Required Long parameter 'id' is not present");
        serverModelException.setCode("VE");
        return new ResponseEntity<>(serverModelException,HttpStatus.BAD_REQUEST);
    }
}
