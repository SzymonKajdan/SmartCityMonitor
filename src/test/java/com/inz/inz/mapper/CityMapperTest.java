package com.inz.inz.mapper;

import com.inz.inz.entity.CityEntity;
import com.inz.inz.entity.ReportEntity;
import com.inz.inz.entity.ReportRatingEntity;
import com.inz.inz.entity.enums.ReportType;
import com.inz.inz.resoruce.cityResource.CityResource;
import com.inz.inz.resoruce.cityResource.CityResourceGetLight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CityMapperTest {
    @Autowired
    ReportMapper reportMapper = new ReportMapperImpl();

    @Autowired
    CityMapper cityMapper = new CityMapperImpl();


    @Test
    public void mapToEntityTest() {
        CityEntity resource = new CityEntity();
        resource.setId(1l);
        resource.setLatitude("1");
        resource.setLongitude("2");
        resource.setName("Ldz");
        resource.setReportList(null);

        CityResource cityEntity = cityMapper.mapToCityResource(resource);
        assertEquals(resource.getId(), cityEntity.getId());
        assertEquals(resource.getLatitude(), cityEntity.getLatitude());
        assertEquals(resource.getLongitude(), cityEntity.getLongitude());
        assertEquals(resource.getName(), cityEntity.getCityName());
        assertEquals(0, cityEntity.getReports().size());

        cityEntity = cityMapper.mapToCityResource(null);
        assertNull(cityEntity);


        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setId(1l);
        reportEntity.setCity(resource);
        reportEntity.setDateReport(new Date());
        reportEntity.setReportType(ReportType.COLLISION);
        reportEntity.setDescription("opis");
        reportEntity.setLatitude("1");
        reportEntity.setLongitude("1");
        reportEntity.setReportRating(new ReportRatingEntity());

        resource.setReportList(Arrays.asList(reportEntity));

        cityEntity = cityMapper.mapToCityResource(resource);

        assertEquals(1, cityEntity.getReports().size());

    }

    @Test
    public void maptoCityLigthTest() {
        CityEntity resource = new CityEntity();
        resource.setId(1l);
        resource.setLatitude("1");
        resource.setLongitude("2");
        resource.setName("Ldz");
        resource.setReportList(new ArrayList<>());

        CityResourceGetLight c = cityMapper.mapToCityResourceGetLight(resource);

        assertNotNull(c);
        assertEquals("Ldz",c.getCityName());
        assertEquals(Long.valueOf(1L),c.getId());

        c=cityMapper.mapToCityResourceGetLight(null);
        assertNull(c);

    }
}
