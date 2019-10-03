package com.inz.inz.controller;

import com.inz.inz.ExcpetionHandler.DbException;
import com.inz.inz.ExcpetionHandler.EnumExcpetion;
import com.inz.inz.adapter.adapterImpl.ReportAdapterImpl;
import com.inz.inz.resoruce.ReportResourcePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("reports")
public class ReportController {
    private  static final String  MEDIA_TYPE = "application/json";

    @Autowired
    ReportAdapterImpl reportAdapter;
    @PostMapping(value = "/addReport", consumes = MEDIA_TYPE, produces = MEDIA_TYPE )
    public ResponseEntity<?> addReport(@RequestBody @Valid ReportResourcePost reportResourcePost, HttpServletRequest request) throws DbException, EnumExcpetion {
        reportAdapter.createReport(request,reportResourcePost);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
