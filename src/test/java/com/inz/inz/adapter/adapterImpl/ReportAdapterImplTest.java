package com.inz.inz.adapter.adapterImpl;

import com.inz.inz.entity.*;
import com.inz.inz.entity.enums.ReportType;
import com.inz.inz.exceptionhandler.AuthenticationException;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.exceptionhandler.EnumExcpetion;
import com.inz.inz.exceptionhandler.ExceptionModel;
import com.inz.inz.mapper.ReportMapper;
import com.inz.inz.repository.*;
import com.inz.inz.resoruce.reportResource.MarkResourcePost;
import com.inz.inz.resoruce.reportResource.NotActiveResource;
import com.inz.inz.resoruce.reportResource.ReportResource;
import com.inz.inz.resoruce.reportResource.ReportResourcePost;
import com.inz.inz.security.model.User;
import com.inz.inz.security.service.UserTokenReciver;
import com.inz.inz.service.InitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest()
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class ReportAdapterImplTest {


    @Autowired
    BanEntityRepository banEntityRepository;
    @MockBean
    UserTokenReciver userTokenRecvier;


    @Autowired
    ReportMapper reportMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReportRatingEntityRepository reportRatingEntityRepository;

    @Autowired
    ReportEntityRepository reportEntityRepository;

    @Autowired
    CityEntityRepository cityEntityRepository;

    @Autowired
    UserVotedRepository userVotedRepository;

    @MockBean
    private InitService startupConfiguration;

    @Autowired
    ReportAdapterImpl adapter;

    @Autowired
    UserRatingRepository userRatingRepository;

    private void init(boolean good) {

        if (good) {
            UserRatingEntity ratingEntity=new UserRatingEntity();
            userRatingRepository.save(ratingEntity);

            BanEntity banEntity = new BanEntity();
            banEntityRepository.save(banEntity);

            User user = new User();
            user.setUserRatingEntity(ratingEntity);
            user.setBanEntity(banEntity);
            userRepository.save(user);

            CityEntity cityEntity = new CityEntity();
            cityEntity.setReportList(new ArrayList<>());
            cityEntity.setName("lodz");
            cityEntityRepository.save(cityEntity);
        } else {
            BanEntity banEntity = new BanEntity();
            banEntityRepository.save(banEntity);
            User user = new User();
            user.setBanEntity(banEntity);
            userRepository.save(user);
            CityEntity cityEntity = new CityEntity();
            cityEntity.setReportList(null);
            cityEntity.setName("lodz");
            cityEntityRepository.save(cityEntity);
        }

    }


    private void initreports() {
        ReportRatingEntity reportRatingEntity = new ReportRatingEntity();
        reportRatingEntity.setUsersVoted(new ArrayList<>());
        reportRatingEntityRepository.save(reportRatingEntity);

        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setUser(userRepository.findById(1l).get());
        reportEntity.setCity(cityEntityRepository.findByName("lodz").get());
        reportEntity.setDateReport(new Date());
        reportEntity.setReportRating(reportRatingEntity);
        reportEntity.setReportType(ReportType.HOLE_IN_THE_ROAD);
        reportEntityRepository.save(reportEntity);
    }

    @Test
    public void createReportTest() throws DbException, EnumExcpetion, AuthenticationException {
        init(true);
        HttpServletRequest servletRequest = new HttpServletRequestWrapper(new MockHttpServletRequest());
        when(userTokenRecvier.getUserId(servletRequest)).thenReturn(1l);


        ReportResourcePost post = new ReportResourcePost();
        post.setCityName("lodz");
        post.setDescription("ada");
        post.setReportType("VANDALISM");
        ReportEntity reportEntity = adapter.createReport(servletRequest, post);

        assertEquals(Long.valueOf(1l), reportEntity.getId());
    }


    @Test(expected = AuthenticationException.class)
    public void createReportByBannedUser() throws AuthenticationException, DbException, EnumExcpetion {
        HttpServletRequest servletRequest = new HttpServletRequestWrapper(new MockHttpServletRequest());

        init(true);
        ReportResourcePost post = new ReportResourcePost();

        post.setCityName("lodz");
        post.setDescription("ada");
        post.setReportType("VANDALISM");
        User user = userRepository.findById(1l).get();

        user.getBanEntity().setBanned(true);
        banEntityRepository.save(user.getBanEntity());
        when(userTokenRecvier.getUserId(servletRequest)).thenReturn(1l);


        adapter.createReport(servletRequest, post);
    }


    @Test
    public void getReportTest() throws DbException {
        init(true);
        initreports();
        ReportResource reportResource = adapter.getReport(1l);
        assertNotNull(reportResource);
    }

    @Test(expected = DbException.class)
    public void getReportFailureTest() throws DbException {
        adapter.getReport(1l);
    }

    @Test
    public void addMarkTest() throws DbException, AuthenticationException {

        init(true);
        initreports();

        BanEntity banEntity = new BanEntity();
        banEntityRepository.save(banEntity);
        User user = new User();
        user.setBanEntity(banEntity);
        userRepository.save(user);

        MarkResourcePost markResourcePost = new MarkResourcePost();
        markResourcePost.setUserId(2l);
        markResourcePost.setReportId(1l);
        markResourcePost.setMark(5);
        Optional<ReportEntity> reportEntity = adapter.addMArk(markResourcePost);

        assertEquals(1, reportEntity.get().getReportRating().getUsersVoted().size());

        assertEquals(5, reportEntity.get().getReportRating().getMarks());

        assertThrows(DbException.class, () -> {
            adapter.addMArk(markResourcePost);
        });


    }

    @Test(expected = DbException.class)
    public void addMarkFailTest() throws DbException, AuthenticationException {
        init(true);


        MarkResourcePost markResourcePost = new MarkResourcePost();
        markResourcePost.setUserId(1l);
        markResourcePost.setReportId(1l);
        markResourcePost.setMark(5);
        adapter.addMArk(markResourcePost);

    }

    @Test(expected = DbException.class)
    public void notReportExist() throws DbException, AuthenticationException {
        init(true);
        MarkResourcePost markResourcePost = new MarkResourcePost();
        markResourcePost.setUserId(1l);
        markResourcePost.setReportId(1l);
        markResourcePost.setMark(5);
        adapter.addMArk(markResourcePost);
    }


    @Test(expected = DbException.class)
    public void userNotExistTest() throws DbException, AuthenticationException {
        init(true);
        initreports();
        MarkResourcePost markResourcePost = new MarkResourcePost();
        markResourcePost.setMark(4);
        markResourcePost.setReportId(1l);
        markResourcePost.setUserId(3l);

        adapter.addMArk(markResourcePost);
    }


    @Test
    public void addMarkAndSetAsNotActive() throws AuthenticationException, ExceptionModel {
        init(true);
        initreports();

        BanEntity banEntity = new BanEntity();
        banEntityRepository.save(banEntity);
        User user = new User();
        user.setBanEntity(banEntity);
        userRepository.save(user);

        NotActiveResource notActiveResource = new NotActiveResource();
        notActiveResource.setUserId(2l);
        notActiveResource.setReportId(1l);

        adapter.markAsNotActive(notActiveResource);

        MarkResourcePost markResourcePost = new MarkResourcePost();
        markResourcePost.setUserId(2l);
        markResourcePost.setReportId(1l);
        Optional<ReportEntity> r = adapter.addMArk(markResourcePost);

        assertEquals(1, r.get().getReportRating().getNotActiveCounter());
        assertEquals(1, r.get().getReportRating().getUsersVoted().size());
        assertEquals(true, r.get().getReportRating().getUsersVoted().get(0).isMarked());
        assertEquals(false, r.get().getReportRating().getUsersVoted().get(0).isFalse());
        assertEquals(true, r.get().getReportRating().getUsersVoted().get(0).isNotActual());
    }


    @Test
    public void addAsnotActiveAndAddMarkTest() throws AuthenticationException, ExceptionModel {
        init(true);
        initreports();

        BanEntity banEntity = new BanEntity();
        banEntityRepository.save(banEntity);
        User user = new User();
        user.setBanEntity(banEntity);
        userRepository.save(user);


        MarkResourcePost markResourcePost = new MarkResourcePost();
        markResourcePost.setUserId(2l);
        markResourcePost.setReportId(1l);
        Optional<ReportEntity> r = adapter.addMArk(markResourcePost);

        NotActiveResource notActiveResource = new NotActiveResource();
        notActiveResource.setUserId(2l);
        notActiveResource.setReportId(1l);

        r = adapter.markAsNotActive(notActiveResource);

        assertEquals(1, r.get().getReportRating().getNotActiveCounter());
        assertEquals(1, r.get().getReportRating().getUsersVoted().size());
        assertTrue(r.get().getReportRating().getUsersVoted().get(0).isMarked());
        assertFalse(r.get().getReportRating().getUsersVoted().get(0).isFalse());
        assertTrue(r.get().getReportRating().getUsersVoted().get(0).isNotActual());
    }

    @Test
    public void markAsNotActiveTest() throws AuthenticationException, ExceptionModel {
        init(true);
        initreports();
        BanEntity banEntity = new BanEntity();
        banEntityRepository.save(banEntity);
        User user = new User();
        user.setBanEntity(banEntity);
        userRepository.save(user);


        NotActiveResource notActiveResource = new NotActiveResource();
        notActiveResource.setUserId(2l);
        notActiveResource.setReportId(1l);

        Optional<ReportEntity> r = adapter.markAsNotActive(notActiveResource);


        assertEquals(1, r.get().getReportRating().getUsersVoted().size());
        assertTrue(r.get().getReportRating().getUsersVoted().get(0).isNotActual());
        assertFalse(r.get().getReportRating().getUsersVoted().get(0).isMarked());

        assertThrows(DbException.class, () -> {
            adapter.markAsNotActive(notActiveResource);
        });
    }


    @Test
    public void markAsFalse() throws AuthenticationException, DbException {
        init(true);
        initreports();
        BanEntity banEntity = new BanEntity();
        banEntityRepository.save(banEntity);
        User user = new User();
        user.setBanEntity(banEntity);
        userRepository.save(user);
        NotActiveResource notActiveResource = new NotActiveResource();
        notActiveResource.setUserId(2l);
        notActiveResource.setReportId(1l);

        Optional<ReportEntity> r = adapter.markAsFalse(notActiveResource);

        assertEquals(1, r.get().getReportRating().getUsersVoted().size());
        assertTrue(r.get().getReportRating().getUsersVoted().get(0).isFalse());
        assertFalse(r.get().getReportRating().getUsersVoted().get(0).isMarked());


        assertThrows(DbException.class, () -> adapter.markAsFalse(notActiveResource));
    }


    @Test
    public void addAsFalseAndAddMarkTest() throws AuthenticationException, ExceptionModel {
        init(true);
        initreports();

        BanEntity banEntity = new BanEntity();
        banEntityRepository.save(banEntity);
        User user = new User();
        user.setBanEntity(banEntity);
        userRepository.save(user);


        MarkResourcePost markResourcePost = new MarkResourcePost();
        markResourcePost.setUserId(2l);
        markResourcePost.setReportId(1l);
        adapter.addMArk(markResourcePost);

        NotActiveResource notActiveResource = new NotActiveResource();
        notActiveResource.setUserId(2l);
        notActiveResource.setReportId(1l);

        Optional<ReportEntity> r = adapter.markAsFalse(notActiveResource);

        assertEquals(1, r.get().getReportRating().getFalseReportQuantity());
        assertEquals(1, r.get().getReportRating().getUsersVoted().size());
        assertTrue(r.get().getReportRating().getUsersVoted().get(0).isMarked());
        assertFalse(r.get().getReportRating().getUsersVoted().get(0).isNotActual());
        assertTrue(r.get().getReportRating().getUsersVoted().get(0).isFalse());
    }


    @Test
    public void addAllTest() throws ExceptionModel, AuthenticationException {
        init(true);
        initreports();

        BanEntity banEntity = new BanEntity();
        banEntityRepository.save(banEntity);
        User user = new User();
        user.setBanEntity(banEntity);
        userRepository.save(user);


        MarkResourcePost markResourcePost = new MarkResourcePost();
        markResourcePost.setUserId(2l);
        markResourcePost.setReportId(1l);
        adapter.addMArk(markResourcePost);

        NotActiveResource notActiveResource = new NotActiveResource();
        notActiveResource.setUserId(2l);
        notActiveResource.setReportId(1l);

        adapter.markAsFalse(notActiveResource);

        Optional<ReportEntity> r = adapter.markAsNotActive(notActiveResource);

        assertEquals(1, r.get().getReportRating().getFalseReportQuantity());
        assertEquals(1, r.get().getReportRating().getUsersVoted().size());
        assertTrue(r.get().getReportRating().getUsersVoted().get(0).isMarked());
        assertTrue(r.get().getReportRating().getUsersVoted().get(0).isNotActual());
        assertTrue(r.get().getReportRating().getUsersVoted().get(0).isFalse());

    }


    @Test(expected = DbException.class)
    public void checkReportSecondTest() throws DbException, AuthenticationException {
        init(true);
        initreports();

        NotActiveResource notActiveResource=new NotActiveResource();
        notActiveResource.setReportId(1l);
        notActiveResource.setUserId(3l);
        adapter.markAsFalse(notActiveResource);

    }

    @Test
    public  void setFalseReportTest() throws AuthenticationException, DbException {

        init(true);
        initreports();
        ReportEntity reportEntity=reportEntityRepository.findById(1l).get();
        reportEntity.getReportRating().setFalseReportQuantity(10);
        reportRatingEntityRepository.save(reportEntity.getReportRating());

        BanEntity banEntity = new BanEntity();
        banEntityRepository.save(banEntity);
        User user = new User();
        user.setBanEntity(banEntity);
        userRepository.save(user);

        NotActiveResource notActiveResource=new NotActiveResource();
        notActiveResource.setUserId(2l);
        notActiveResource.setReportId(1l);

        Optional<ReportEntity> report=adapter.markAsFalse(notActiveResource);

        User responseUser=report.get().getUser();
        assertTrue(responseUser.getBanEntity().isBanned());

    }

    @Test
    public  void setNotActiveReportTest() throws AuthenticationException, ExceptionModel {

        init(true);
        initreports();
        ReportEntity reportEntity=reportEntityRepository.findById(1l).get();
        reportEntity.getReportRating().setNotActiveCounter(10);
        reportRatingEntityRepository.save(reportEntity.getReportRating());

        BanEntity banEntity = new BanEntity();
        banEntityRepository.save(banEntity);
        User user = new User();
        user.setBanEntity(banEntity);
        userRepository.save(user);

        NotActiveResource notActiveResource=new NotActiveResource();
        notActiveResource.setUserId(2l);
        notActiveResource.setReportId(1l);

        Optional<ReportEntity> report=adapter.markAsNotActive(notActiveResource);

        assertEquals(11,report.get().getReportRating().getNotActiveCounter());
    }


    @Test
    public  void oneyearbanTest() throws AuthenticationException, DbException {
        init(true);
        initreports();

        User userToBan=userRepository.findById(1l).get();
        userToBan.getBanEntity().setBanCounter(3);
        banEntityRepository.save(userToBan.getBanEntity());


        ReportEntity reportEntity=reportEntityRepository.findById(1l).get();
        reportEntity.getReportRating().setFalseReportQuantity(10);
        reportRatingEntityRepository.save(reportEntity.getReportRating());

        BanEntity banEntity = new BanEntity();
        banEntityRepository.save(banEntity);
        User user = new User();
        user.setBanEntity(banEntity);
        userRepository.save(user);

        NotActiveResource notActiveResource=new NotActiveResource();
        notActiveResource.setUserId(2l);
        notActiveResource.setReportId(1l);

        Optional<ReportEntity> report=adapter.markAsFalse(notActiveResource);

        User responseUser=report.get().getUser();
        assertTrue(responseUser.getBanEntity().isBanned());
        assertEquals(4,responseUser.getBanEntity().getBanCounter());

    }
//
//
//    @Test
//    public void markAsFalse() throws DbException, AuthenticationException {
//
//
//    }

}