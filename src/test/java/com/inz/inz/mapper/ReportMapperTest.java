package com.inz.inz.mapper;

import com.inz.inz.entity.CityEntity;
import com.inz.inz.entity.ReportEntity;
import com.inz.inz.entity.ReportRatingEntity;
import com.inz.inz.entity.enums.ReportType;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.exceptionhandler.EnumExcpetion;
import com.inz.inz.repository.CityEntityRepository;
import com.inz.inz.resoruce.reportresource.ReportLight;
import com.inz.inz.resoruce.reportresource.ReportResource;
import com.inz.inz.resoruce.reportresource.ReportResourcePost;
import com.inz.inz.entity.User;
import com.inz.inz.service.InitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReportMapperTest {


    @MockBean
    InitService initService;
    @MockBean
    CityEntityRepository cityEntityRepository;

    @Autowired
    ReportMapper reportMapper = new ReportMapperImpl();

    @Test
    public void mapToReportLigthTest() {

        ReportRatingEntity reportRatingEntity = new ReportRatingEntity();
        reportRatingEntity.setNotActiveCounter(1);
        reportRatingEntity.setFalseReportQuantity(0);
        reportRatingEntity.setQuantity(1);
        reportRatingEntity.setMarks(5);
        reportRatingEntity.setId(1L);

        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setReportRating(reportRatingEntity);
        reportEntity.setLongitude("1");
        reportEntity.setLatitude("2");
        reportEntity.setCity(new CityEntity());
        reportEntity.setDateReport(new Date());
        reportEntity.setUser(new User());
        reportEntity.setVideo("dsadsadsa");
        reportEntity.setPhoto("saddsadsadsa");
        reportEntity.setDescription("adsa");
        reportEntity.setReportType(ReportType.HOLE_IN_THE_ROAD);

        ReportLight reportLight = reportMapper.mapToReportLigth(reportEntity);
        assertNotNull(reportLight);
        assertEquals(reportEntity.getDescription(), reportLight.getDescription());
        assertTrue(reportLight.getIsActive());
        assertEquals(reportEntity.getLatitude(), reportLight.getLatitude());
        assertEquals(reportEntity.getLongitude(), reportLight.getLongitude());
        assertEquals(reportEntity.getPhoto(), reportLight.getPhoto());
        assertEquals(reportEntity.getReportType().getType(), reportLight.getReportType());

        assertNull(reportMapper.mapToReportLigth(null));


        reportEntity.getReportRating().setNotActiveCounter(12);
        reportLight = reportMapper.mapToReportLigth(reportEntity);
        assertFalse(reportLight.getIsActive());


    }
    @Test
    public void mapToReportTest(){
    assertNull(reportMapper.mapToReport(null));

        ReportRatingEntity reportRatingEntity=new ReportRatingEntity();
        reportRatingEntity.setNotActiveCounter(1);
        reportRatingEntity.setFalseReportQuantity(0);
        reportRatingEntity.setQuantity(1);
        reportRatingEntity.setMarks(5);
        reportRatingEntity.setId(1L);

        ReportEntity reportEntity=new ReportEntity();
        reportEntity.setReportRating(reportRatingEntity);
        reportEntity.setLongitude("1");
        reportEntity.setLatitude("2");
        reportEntity.setCity(new CityEntity());
        reportEntity.setDateReport(new Date());
        reportEntity.setUser(new User());
        reportEntity.setVideo("dsadsadsa");
        reportEntity.setPhoto("saddsadsadsa");
        reportEntity.setDescription("adsa");
        reportEntity.setReportType(ReportType.HOLE_IN_THE_ROAD);

        ReportResource reportResource=reportMapper.mapToReport(reportEntity);

        assertEquals(reportEntity.getPhoto(),reportResource.getPhoto());
        assertEquals(reportEntity.getVideo(),reportResource.getVideo());
        assertEquals(reportEntity.getDescription(),reportResource.getDescription());
        assertEquals(reportEntity.getLongitude(),reportResource.getLongitude());
        assertEquals(reportEntity.getLatitude(),reportResource.getLatitude());
        assertTrue(reportResource.getIsActive());

    }


    @Test
    public void mapToEntityTest() throws DbException, EnumExcpetion {
        assertNull(reportMapper.mapToEntity(null));
        ReportResourcePost reportResourcePost=new ReportResourcePost();
        reportResourcePost.setCityName("ldz");
        reportResourcePost.setReportType(ReportType.HOLE_IN_THE_ROAD.name());
        reportResourcePost.setLatitude("1");
        reportResourcePost.setLongitude("1");
        reportResourcePost.setPhoto("dsa");

        CityEntity cityEntity=new CityEntity();
        cityEntity.setName("Ldz");

        when(cityEntityRepository.findByName("ldz")).thenReturn(java.util.Optional.of(cityEntity));
        ReportEntity reportEntity=reportMapper.mapToEntity(reportResourcePost);

        assertEquals("1",reportEntity.getLatitude());
        assertEquals("1",reportEntity.getLongitude());
        assertEquals(reportResourcePost.getPhoto(),reportEntity.getPhoto());
        assertEquals(ReportType.HOLE_IN_THE_ROAD,reportEntity.getReportType());

    }

    @Test(expected = EnumExcpetion.class)
    public void wrongReportTypeTest() throws DbException, EnumExcpetion {
        ReportResourcePost reportResourcePost=new ReportResourcePost();
        reportResourcePost.setCityName("ldz");
        reportResourcePost.setReportType("wrong");
        reportResourcePost.setLatitude("1");
        reportResourcePost.setLongitude("1");
        reportResourcePost.setPhoto("dsa");

        CityEntity cityEntity=new CityEntity();
        cityEntity.setName("Ldz");

        when(cityEntityRepository.findByName("ldz")).thenReturn(java.util.Optional.of(cityEntity));
        reportMapper.mapToEntity(reportResourcePost);
    }


    @Test(expected = DbException.class)
    public void worngCityNameTest() throws DbException, EnumExcpetion {
        ReportResourcePost reportResourcePost=new ReportResourcePost();
        reportResourcePost.setCityName("ldz");

        reportResourcePost.setLatitude("1");
        reportResourcePost.setLongitude("1");
        reportResourcePost.setPhoto("dsa");

        CityEntity cityEntity=new CityEntity();
        cityEntity.setName("Ldz");


        reportMapper.mapToEntity(reportResourcePost);
    }

}
