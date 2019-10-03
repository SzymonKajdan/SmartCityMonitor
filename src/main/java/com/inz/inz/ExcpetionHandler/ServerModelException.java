package com.inz.inz.ExcpetionHandler;

import lombok.Data;

import java.util.List;

@Data
public class ServerModelException {

    private String details;

    private String code;

    private List<Field> fields;
}
