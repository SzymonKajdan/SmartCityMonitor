package com.inz.inz.adapter.adapterImpl;

import com.inz.inz.entity.CityEntity;
import com.inz.inz.entity.ReportEntity;
import com.inz.inz.entity.ReportRatingEntity;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.mapper.CityMapper;
import com.inz.inz.repository.CityEntityRepository;
import com.inz.inz.resoruce.cityResource.CityResource;
import com.inz.inz.resoruce.cityResource.CityResourceGetLight;
import com.inz.inz.service.InitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class CityAdapterImplTest {

    @MockBean
    private InitService startupConfiguration;

    @MockBean
    CityEntityRepository cityEntityRepository;

    @MockBean
    CityMapper cityMapper;

    @Autowired
    CityAdapterImpl cityAdapter;

    public CityEntity getCityEntity() {
        CityEntity cityEntity = new CityEntity();
        cityEntity.setId(1l);
        cityEntity.setName("ldz");
        cityEntity.setLatitude("1");
        cityEntity.setLongitude("2");
        cityEntity.setReportList(new ArrayList<>());
        return cityEntity;
    }

    public CityResourceGetLight cityResourceGetLight() {
        CityResourceGetLight cityResourceGetLight = new CityResourceGetLight();
        cityResourceGetLight.setId(1l);
        cityResourceGetLight.setCityName("ldz");
        return cityResourceGetLight;
    }

    public CityEntity initCity() {
        ReportRatingEntity reportRatingEntity = new ReportRatingEntity();
        ReportEntity entity = new ReportEntity();
        entity.setReportRating(reportRatingEntity);
        CityEntity cityEntity = new CityEntity();
        cityEntity.setReportList(Collections.singletonList(entity));
        return cityEntity;
    }

    @Test
    public void getCities() throws DbException {
        when(cityEntityRepository.findAll()).thenReturn(Collections.singletonList(getCityEntity()));
        when(cityMapper.mapToCityResourceGetLight(getCityEntity())).thenReturn(cityResourceGetLight());
        List<CityResourceGetLight> respone = cityAdapter.getCities();
        assertEquals(cityResourceGetLight(), respone.get(0));
    }

    @Test(expected = DbException.class)
    public void getCitiesExeptionTest() throws DbException {
        when(cityEntityRepository.findAll()).thenReturn(new ArrayList<>());
        cityAdapter.getCities();
    }

    @Test
    public void getCityTest() throws DbException {
        CityResource cityResource = new CityResource();
        cityResource.setReports(new ArrayList<>());

        when(cityEntityRepository.findById(1l)).thenReturn(Optional.ofNullable(initCity()));
        when(cityMapper.mapToCityResource(initCity())).thenReturn(new CityResource());
        CityResource respone = cityAdapter.getCity(1l, false);
        assertEquals(new CityResource(), respone);

    }
    @Test(expected = DbException.class)
    public void getCityExceptionTest() throws DbException {
        CityResource cityResource = new CityResource();
        cityResource.setReports(new ArrayList<>());

        when(cityEntityRepository.findById(1l)).thenReturn(Optional.ofNullable(null));
        when(cityMapper.mapToCityResource(initCity())).thenReturn(new CityResource());
        CityResource respone = cityAdapter.getCity(1l, false);
    }
}