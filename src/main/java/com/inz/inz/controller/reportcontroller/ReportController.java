package com.inz.inz.controller.reportcontroller;

import com.inz.inz.adapter.adapterimpl.ReportAdapterImpl;
import com.inz.inz.exceptionhandler.AuthenticationException;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.exceptionhandler.EnumExcpetion;
import com.inz.inz.exceptionhandler.ExceptionModel;
import com.inz.inz.resoruce.reportresource.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity addReport(@RequestBody @Valid ReportResourcePost reportResourcePost, HttpServletRequest request) throws DbException, EnumExcpetion, AuthenticationException {
        reportAdapter.createReport(request,reportResourcePost);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "addMark",consumes = MEDIA_TYPE,produces = MEDIA_TYPE)
    public  ResponseEntity addMark(@RequestBody @Valid MarkResourcePost markResourcePost) throws DbException, AuthenticationException {
        reportAdapter.addMArk(markResourcePost);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "markAsNotActive",consumes = MEDIA_TYPE,produces = MEDIA_TYPE)
    public ResponseEntity markAsNotActive(@RequestBody @Valid NotActiveResource notActiveResource) throws AuthenticationException, ExceptionModel {
        reportAdapter.markAsNotActive(notActiveResource);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "markAsFalse",consumes = MEDIA_TYPE,produces = MEDIA_TYPE)
    public ResponseEntity markAsFalse(@RequestBody @Valid NotActiveResource notActiveResource) throws AuthenticationException, ExceptionModel {
        reportAdapter.markAsFalse(notActiveResource);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "getUserReports",produces = MEDIA_TYPE)
    public ResponseEntity<List<ReportLight>> getReports(@Valid @RequestParam(value = "id")Long id) throws DbException {

        return new ResponseEntity<>(reportAdapter.getReports(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "blockReport/{id}",produces = MEDIA_TYPE)
    public ResponseEntity getProtectedGreeting(@PathVariable Long id) throws DbException {
        reportAdapter.blockReport(id);
        return new  ResponseEntity<>(HttpStatus.OK);
    }
}
