package com.inz.inz.adapter.adapterImpl;

import com.inz.inz.entity.*;
import com.inz.inz.entity.enums.ReportType;
import com.inz.inz.exceptionhandler.AuthenticationException;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.exceptionhandler.EnumExcpetion;
import com.inz.inz.mapper.ReportMapper;
import com.inz.inz.repository.*;
import com.inz.inz.resoruce.MarkResourcePost;
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
        reportEntity.setReportRating(reportRatingEntity() );
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

    private  static  ReportRatingEntity reportRatingEntity(){
        ReportRatingEntity ratingEntity=new ReportRatingEntity();
        ratingEntity.setUsersVoted(new ArrayList<>());
        ratingEntity.getUsersVoted().add(userVoted());
        ratingEntity.setId(1l);
        ratingEntity.setMarks(5);
        ratingEntity.setQuantity(1);
        return ratingEntity;
    }
    private static UserVoted userVoted(){
        UserVoted userVoted=new UserVoted();
        userVoted.setMarked(false);
        userVoted.setUserId(1l);
        return userVoted;
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
    @Test(expected = DbException.class)
    public  void getReportFailureTest() throws DbException {

        when(reportMapper.mapToReport(getReportEntity())).thenReturn(new ReportResource());
        ReportResource  reportResource=adapter.getReport(1l);

    }

    @Test
    public  void addMarkTest() throws DbException, AuthenticationException {

        User user=getUser();
        user.setId(2l);


        when(userRepository.findById(2l)).thenReturn(Optional.of(user));
        when(reportEntityRepository.findById(1l)).thenReturn(Optional.ofNullable(getReportEntity()));
        MarkResourcePost markResourcePost=new MarkResourcePost();
        markResourcePost.setMark(5);
        markResourcePost.setReportId(1l);
        markResourcePost.setUserId(2l);

        adapter.addMArk(markResourcePost);

        ReportEntity reportEntity=getReportEntity();
        reportEntity.getReportRating().getUsersVoted().get(0).setUserId(2l);
        when(userVotedRepository.findByUserId(2l)).thenReturn(Optional.ofNullable(userVoted()));
        when(reportEntityRepository.findById(1l)).thenReturn(Optional.ofNullable(reportEntity));
        adapter.addMArk(markResourcePost);
    }

    @Test(expected = DbException.class)
    public void addMarkFailTest() throws DbException, AuthenticationException {
        User user=getUser();
        user.setId(1l);
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        when(reportEntityRepository.findById(1l)).thenReturn(Optional.ofNullable(getReportEntity()));
        MarkResourcePost markResourcePost=new MarkResourcePost();
        markResourcePost.setMark(5);
        markResourcePost.setReportId(1l);
        markResourcePost.setUserId(1l);

        adapter.addMArk(markResourcePost);
    }

    @Test(expected = DbException.class)
    public void notReportExist() throws DbException, AuthenticationException {
        User user=getUser();
        user.setId(1l);
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        when(reportEntityRepository.findById(1l)).thenReturn(Optional.ofNullable(null));
        MarkResourcePost markResourcePost=new MarkResourcePost();
        markResourcePost.setMark(5);
        markResourcePost.setReportId(1l);
        markResourcePost.setUserId(2l);

        adapter.addMArk(markResourcePost);
    }

    @Test(expected = DbException.class)
    public void secondMarkAndTest() throws DbException, AuthenticationException {
        User user=getUser();
        user.setId(2l);
        ReportEntity reportEntity=getReportEntity();
        reportEntity.getReportRating().getUsersVoted().get(0).setUserId(2l);
        reportEntity.getReportRating().getUsersVoted().get(0).setMarked(true);
        when(userRepository.findById(2l)).thenReturn(Optional.of(user));
        when(reportEntityRepository.findById(1l)).thenReturn(Optional.ofNullable(reportEntity));
        MarkResourcePost markResourcePost=new MarkResourcePost();
        markResourcePost.setMark(5);
        markResourcePost.setReportId(1l);
        markResourcePost.setUserId(2l);

        adapter.addMArk(markResourcePost);


    }

    @Test(expected = DbException.class)
    public  void  checkReportTest() throws DbException, AuthenticationException {
        User user=getUser();
        user.setId(2l);
        ReportEntity reportEntity=getReportEntity();
        reportEntity.getReportRating().getUsersVoted().get(0).setUserId(2l);
        reportEntity.getReportRating().getUsersVoted().get(0).setMarked(true);
        reportEntity.setUser(user);
        when(userRepository.findById(2l)).thenReturn(Optional.of(user));
        when(reportEntityRepository.findById(1l)).thenReturn(Optional.ofNullable(reportEntity));
        MarkResourcePost markResourcePost=new MarkResourcePost();
        markResourcePost.setMark(5);
        markResourcePost.setReportId(1l);
        markResourcePost.setUserId(2l);

        adapter.addMArk(markResourcePost);
    }

    @Test(expected = DbException.class)
    public  void  checkReportSecondTest() throws DbException, AuthenticationException {
        User user=getUser();
        user.setId(2l);
        when(userRepository.findById(2l)).thenReturn(Optional.of(user));
        when(reportEntityRepository.findById(1l)).thenReturn(Optional.ofNullable(null));
        MarkResourcePost markResourcePost=new MarkResourcePost();
        markResourcePost.setMark(5);
        markResourcePost.setReportId(1l);
        markResourcePost.setUserId(2l);

        adapter.addMArk(markResourcePost);
    }




}