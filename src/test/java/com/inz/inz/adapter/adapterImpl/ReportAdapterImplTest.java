package com.inz.inz.adapter.adapterImpl;

import com.inz.inz.entity.BanEntity;
import com.inz.inz.entity.CityEntity;
import com.inz.inz.entity.ReportEntity;
import com.inz.inz.entity.ReportRatingEntity;
import com.inz.inz.entity.enums.ReportType;
import com.inz.inz.exceptionhandler.AuthenticationException;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.exceptionhandler.EnumExcpetion;
import com.inz.inz.mapper.ReportMapper;
import com.inz.inz.repository.*;
import com.inz.inz.resoruce.ReportResource;
import com.inz.inz.resoruce.ReportResourcePost;
import com.inz.inz.seciurity.model.User;
import com.inz.inz.seciurity.service.UserTokenReciver;
import com.inz.inz.service.InitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest()
public class ReportAdapterImplTest {

    @MockBean
    UserTokenReciver userTokenRecvier;

    @MockBean
    ReportMapper reportMapper;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ReportRatingEntityRepository reportRatingEntityRepository;

    @MockBean
    ReportEntityRepository reportEntityRepository;

    @MockBean
    CityEntityRepository cityEntityRepository;

    @MockBean
    UserVotedRepository userVotedRepository;

    @MockBean
    private InitService startupConfiguration;
    @Autowired
    ReportAdapterImpl adapter;

    public  static ReportEntity getReportEntity(){
        ReportEntity reportEntity=new ReportEntity();
        reportEntity.setId(1l);
        reportEntity.setReportType(ReportType.VANDALISM);
        reportEntity.setUser(getUser());
        reportEntity.setCity(cityEntity());
        return reportEntity;
    }

    private static User getUser() {
    User user=new User();
    user.setId(1l);
    user.setBanEntity(getBan());
    user.setReportsList(new ArrayList<>());
    return user;
    }

    private static BanEntity getBan() {
        BanEntity banEntity=new BanEntity();
        return banEntity;
    }

    private static  CityEntity cityEntity(){
        CityEntity cityEntity=new CityEntity();
        cityEntity.setName("lds");
        cityEntity.setId(1l);
        cityEntity.setReportList(new ArrayList<>());
        return cityEntity;
    }
    @Test
    public void createReportTest() throws DbException, EnumExcpetion, AuthenticationException {
        HttpServletRequest servletRequest=new HttpServletRequestWrapper(new MockHttpServletRequest());
        when(userTokenRecvier.getUserId(servletRequest)).thenReturn(1l);
        when(reportMapper.mapToEntity(new ReportResourcePost())).thenReturn(getReportEntity());
        when(userRepository.getOne(1l)).thenReturn(getUser());
        when(cityEntityRepository.findByName(cityEntity().getName())).thenReturn(java.util.Optional.ofNullable(cityEntity()));

        ReportEntity reportEntity=adapter.createReport(servletRequest,new ReportResourcePost());

        assertEquals(getReportEntity().getId(),reportEntity.getId());
    }
    @Test(expected = DbException.class)
    public void createReportSavingExceptionTest() throws DbException, EnumExcpetion, AuthenticationException {
        HttpServletRequest servletRequest=new HttpServletRequestWrapper(new MockHttpServletRequest());
        when(userTokenRecvier.getUserId(servletRequest)).thenReturn(1l);
        when(reportMapper.mapToEntity(new ReportResourcePost())).thenReturn(getReportEntity());
        when(userRepository.getOne(1l)).thenReturn(getUser());
        when(cityEntityRepository.findByName(cityEntity().getName())).thenReturn(java.util.Optional.ofNullable(cityEntity()));
        when(reportRatingEntityRepository.save(new ReportRatingEntity())).thenThrow(DataIntegrityViolationException.class);
        ReportEntity reportEntity=adapter.createReport(servletRequest,new ReportResourcePost());

    }
    @Test(expected = AuthenticationException.class)
    public void createReportByBannedUser() throws DbException, EnumExcpetion, AuthenticationException {
        HttpServletRequest servletRequest=new HttpServletRequestWrapper(new MockHttpServletRequest());
        User user=getUser();
        user.getBanEntity().setBanned(true);
        when(userTokenRecvier.getUserId(servletRequest)).thenReturn(1l);
        when(reportMapper.mapToEntity(new ReportResourcePost())).thenReturn(getReportEntity());
        when(userRepository.getOne(1l)).thenReturn(user);

       adapter.createReport(servletRequest,new ReportResourcePost());
    }

    @Test
    public  void getReportTest() throws DbException {
        when(reportEntityRepository.findById(1l)).thenReturn(Optional.ofNullable(getReportEntity()));
        when(reportMapper.mapToReport(getReportEntity())).thenReturn(new ReportResource());
        ReportResource  reportResource=adapter.getReport(1l);
        assertNotNull(reportResource);
    }

}