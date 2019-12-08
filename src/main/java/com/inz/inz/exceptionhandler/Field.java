package com.inz.inz.exceptionhandler;

import lombok.Data;

import java.io.Serializable;

@Data
public class Field implements Serializable {

    private static final long serialVersionUID = 5132317749858235778L;
    private  String fieldName;

    private String details;
}
