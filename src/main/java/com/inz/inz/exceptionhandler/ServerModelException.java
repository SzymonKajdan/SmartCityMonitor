package com.inz.inz.exceptionhandler;

import lombok.Data;

import java.util.List;

@Data
public class ServerModelException {

    private String details;

    private String code;

    private List<Field> fields;
}
