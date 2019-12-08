package com.inz.inz.controller;

import com.inz.inz.adapter.adapterimpl.ReportAdapterImpl;
import com.inz.inz.controller.reportcontroller.ReportController;
import com.inz.inz.entity.ReportEntity;
import com.inz.inz.exceptionhandler.AuthenticationException;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.exceptionhandler.EnumExcpetion;
import com.inz.inz.exceptionhandler.ExceptionModel;
import com.inz.inz.resoruce.reportresource.MarkResourcePost;
import com.inz.inz.resoruce.reportresource.NotActiveResource;
import com.inz.inz.resoruce.reportresource.ReportResource;
import com.inz.inz.resoruce.reportresource.ReportResourcePost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportControllerTest {
    @MockBean
    ReportAdapterImpl reportAdapter;

    @Autowired
    ReportController reportController;

    public static ReportResourcePost getExamplePostReport(){
        ReportResourcePost post=new ReportResourcePost();
        post.setCityName("lodz");
        return post;
    }

    @Test
    public  void getReportById() throws DbException {
        ReportResource reportResource=new ReportResource();
        reportResource.setId(1l);
        when(reportAdapter.getReport(1l)).thenReturn(reportResource);
        ResponseEntity<ReportResource> report=reportController.addReport(1l);
        assertEquals(reportResource,report.getBody());
    }

    @Test
    public void addReportTest() throws EnumExcpetion, AuthenticationException, DbException {
        HttpServletRequest servletRequest=new HttpServletRequestWrapper(new MockHttpServletRequest());
        when(reportAdapter.createReport(servletRequest,getExamplePostReport())).thenReturn(new ReportEntity());
        ResponseEntity<?> responseEntity=reportController.addReport(getExamplePostReport(),servletRequest);
        assertEquals(201,responseEntity.getStatusCode().value());
    }

    @Test
    public void addMarkTest() throws DbException, AuthenticationException {

        MarkResourcePost notActiveResource=new MarkResourcePost();
        reportAdapter=mock(ReportAdapterImpl.class);
        assertEquals(201, reportController.addMark(notActiveResource).getStatusCodeValue());
    }

    @Test
    public void addFalseTest() throws ExceptionModel, AuthenticationException {

        NotActiveResource notActiveResource=new NotActiveResource();
        reportAdapter=mock(ReportAdapterImpl.class);
        assertEquals(201, reportController.markAsFalse(notActiveResource).getStatusCodeValue());
        assertEquals(201, reportController.markAsNotActive(notActiveResource).getStatusCodeValue());
    }
}
