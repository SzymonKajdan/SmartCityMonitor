package com.inz.inz.controller;

import com.inz.inz.ExcpetionHandler.DbException;
import com.inz.inz.ExcpetionHandler.EnumExcpetion;
import com.inz.inz.adapter.adapterImpl.ReportAdapterImpl;
import com.inz.inz.resoruce.MarkResourcePost;
import com.inz.inz.resoruce.ReportResource;
import com.inz.inz.resoruce.ReportResourcePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("reports")
public class ReportController {
    private  static final String  MEDIA_TYPE = "application/json";

    @Autowired
    ReportAdapterImpl reportAdapter;

    @GetMapping(value = "/getReport/{id}", produces = MEDIA_TYPE )
    public ResponseEntity<ReportResource> addReport(@PathVariable Long id ) throws DbException {
       ReportResource reportResource= reportAdapter.getReport(id);
        return new ResponseEntity<>(reportResource,HttpStatus.CREATED);
    }

    @PostMapping(value = "/addReport", consumes = MEDIA_TYPE, produces = MEDIA_TYPE )
    public ResponseEntity<?> addReport(@RequestBody @Valid ReportResourcePost reportResourcePost, HttpServletRequest request) throws DbException, EnumExcpetion {
        reportAdapter.createReport(request,reportResourcePost);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "addMark",consumes = MEDIA_TYPE,produces = MEDIA_TYPE)
    public  ResponseEntity<?> addMark(@RequestBody @Valid MarkResourcePost markResourcePost) throws DbException {
        reportAdapter.addMArk(markResourcePost);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
