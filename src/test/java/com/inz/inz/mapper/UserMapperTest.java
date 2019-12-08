package com.inz.inz.mapper;

import com.inz.inz.entity.*;
import com.inz.inz.entity.enums.ReportType;
import com.inz.inz.resoruce.userresource.UserAuthResoruce;
import com.inz.inz.resoruce.userresource.UserResourcePost;
import com.inz.inz.entity.User;
import com.inz.inz.emailservice.EmailServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {


    @Autowired
    ReportMapper reportMapper = new ReportMapperImpl();
    @Autowired
    UserMapper userMapper = new UserMapperImpl();

    @InjectMocks
    EmailServiceImpl emailService;


    @Test
    public void mapToAtuhResourceTest() {
        UserRatingEntity ratingEntity = new UserRatingEntity();
        ratingEntity.setId(1l);
        ratingEntity.setQuantity(20);
        ratingEntity.setMarks(5);

        BanEntity banEntity = new BanEntity();
        banEntity.setBanned(false);
        banEntity.setDate(null);
        banEntity.setBanCounter(1);

        CityEntity cityEntity = new CityEntity();
        cityEntity.setName("ldz");
        cityEntity.setId(1l);


        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setId(1l);
        reportEntity.setCity(cityEntity);
        reportEntity.setDateReport(new Date());
        reportEntity.setReportType(ReportType.COLLISION);
        reportEntity.setDescription("opis");
        reportEntity.setLatitude("1");
        reportEntity.setLongitude("1");
        reportEntity.setReportRating(new ReportRatingEntity());


        User user = new User();
        user.setId(1l);
        user.setUserRatingEntity(ratingEntity);
        user.setReportsList(Collections.singletonList(reportEntity));
        user.setBanEntity(banEntity);
        user.setUsername("adam");
        user.setFirstname("adam");
        user.setLastname("adamD");
        UserAuthResoruce authResoruce = userMapper.mapToUserAuthResource(user);

        assertEquals("adam",authResoruce.getFirstname());
        assertEquals("adam",authResoruce.getUsername());
        assertEquals("adamD",authResoruce.getSurname());
        assertFalse(authResoruce.getIsBanned());
        assertEquals(Long.valueOf(1L),authResoruce.getId());
        assertEquals(1,authResoruce.getReports().size());

        user.setReportsList(null);
        authResoruce = userMapper.mapToUserAuthResource(user);
        assertEquals(0,authResoruce.getReports().size());
    }

    @Test
    public void  mapToentityTest(){
        UserResourcePost userResourcePost=new UserResourcePost();
        userResourcePost.setEmail("adam@adam");
        userResourcePost.setFirstname("adam");
        userResourcePost.setLastname("mada");
        userResourcePost.setUsername("XYz");
        userResourcePost.setPassword("dsadsadsa");

        User user=userMapper.mapUserResourcePostToUser(userResourcePost);

        assertEquals("adam",user.getFirstname());
        assertEquals(userResourcePost.getEmail(),user.getEmail());
        assertEquals(userResourcePost.getUsername(),user.getUsername());
        assertEquals(userResourcePost.getLastname(),user.getLastname());


    }


    @Test
    public void nullTest(){

        User user=userMapper.mapUserResourcePostToUser(null);
        assertNull(user);

        assertNull(userMapper.mapToUserAuthResource(null));
    }


}
