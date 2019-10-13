package com.inz.inz.adapter.adapterImpl;

import com.inz.inz.entity.BanEntity;
import com.inz.inz.entity.CityEntity;
import com.inz.inz.entity.ReportEntity;
import com.inz.inz.entity.ReportRatingEntity;
import com.inz.inz.exceptionhandler.AuthenticationException;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.exceptionhandler.EnumExcpetion;
import com.inz.inz.mapper.ReportMapper;
import com.inz.inz.repository.*;
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
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest()
@RunWith(SpringRunner.class)
public class ReportAdapterTest {

    @MockBean
    private BanEntityRepository banEntityRepository;

    @MockBean
    private UserTokenReciver userTokenRecvier;

    @MockBean
    private ReportMapper reportMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ReportRatingEntityRepository reportRatingEntityRepository;

    @MockBean
    private ReportEntityRepository reportEntityRepository;

    @MockBean
    private CityEntityRepository cityEntityRepository;

    @MockBean
    private UserVotedRepository userVotedRepository;

    @MockBean
    private InitService startupConfiguration;


    @Autowired
    private ReportAdapterImpl adapter;

    @Test(expected = DbException.class)
    public void addMarkFailureTest() throws DbException, AuthenticationException, EnumExcpetion {

        HttpServletRequest servletRequest=new MockHttpServletRequest();

        User user=new User();
        user.setBanEntity(new BanEntity());
        user.setId(2l);

        ReportResourcePost post=new ReportResourcePost();
        post.setCityName("ldz");

        when(cityEntityRepository.findByName("ldz")).thenReturn(Optional.of(new CityEntity()));
        when(reportMapper.mapToEntity(post)).thenReturn(new ReportEntity());

        when(userTokenRecvier.getUserId(servletRequest)).thenReturn(2l);
        when(reportRatingEntityRepository.save(new ReportRatingEntity())).thenThrow(DataIntegrityViolationException.class);

        when(userRepository.findById(2l)).thenReturn(Optional.of(user));


        adapter.createReport(servletRequest,post);
    }



}
