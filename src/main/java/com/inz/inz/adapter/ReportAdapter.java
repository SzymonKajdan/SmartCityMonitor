package com.inz.inz.adapter;

import com.inz.inz.exceptionhandler.AuthenticationException;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.exceptionhandler.EnumExcpetion;
import com.inz.inz.exceptionhandler.ExceptionModel;
import com.inz.inz.entity.ReportEntity;
import com.inz.inz.resoruce.reportresource.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface ReportAdapter {

    ReportEntity createReport(HttpServletRequest request, ReportResourcePost reportResourcePost) throws DbException, EnumExcpetion, AuthenticationException;

    ReportResource getReport(Long id) throws DbException;

    Optional<ReportEntity> addMArk(MarkResourcePost markResourcePost) throws DbException, AuthenticationException;

    Optional<ReportEntity> markAsNotActive(NotActiveResource notActiveResource) throws ExceptionModel, AuthenticationException;

    Optional<ReportEntity> markAsFalse(NotActiveResource notActiveResource) throws AuthenticationException, DbException;

    List<ReportLight> getReports(Long id) throws DbException;

    void blockReport(Long id) throws DbException;
}
