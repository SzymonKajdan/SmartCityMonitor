package com.inz.inz.adapter;

import com.inz.inz.ExcpetionHandler.DbException;
import com.inz.inz.ExcpetionHandler.EnumExcpetion;
import com.inz.inz.entity.ReportEntity;
import com.inz.inz.resoruce.ReportResource;
import com.inz.inz.resoruce.ReportResourcePost;

import javax.servlet.http.HttpServletRequest;

public interface ReportAdapter {

    ReportEntity createReport(HttpServletRequest request, ReportResourcePost reportResourcePost) throws DbException, EnumExcpetion;

    ReportResource getReport(Long id) throws DbException;
}
