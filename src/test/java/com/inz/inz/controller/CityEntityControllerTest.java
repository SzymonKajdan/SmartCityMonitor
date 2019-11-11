package com.inz.inz.controller;

import com.inz.inz.adapter.adapterImpl.CityAdapterImpl;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.resoruce.cityResource.CityResource;
import com.inz.inz.resoruce.cityResource.CityResourceGetLight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public  class CityEntityControllerTest {

    @MockBean
    CityAdapterImpl cityAdapter;

    @Autowired
    CityEntityController cityEntityController;

    public static CityResourceGetLight getCityResourceGetLight(){
        CityResourceGetLight cityResourceGetLight=new CityResourceGetLight();
        cityResourceGetLight.setCityName("Test");
        cityResourceGetLight.setId(1l);
        return cityResourceGetLight;
    }


    @Test
    public void  getCitiesTest() throws DbException {

        List<CityResourceGetLight> cities= Arrays.asList(getCityResourceGetLight());
        when(cityAdapter.getCities()).thenReturn(Arrays.asList(getCityResourceGetLight()));
        ResponseEntity<List<CityResourceGetLight>> cityList =cityEntityController.addReport();
        assertEquals(1,cityList.getBody().size());
    }

    @Test
    public void  getCityByIdTest() throws DbException {
        CityResource cityResource=new CityResource();
        cityResource.setId(1l);
        when(cityAdapter.getCity(1l,true)).thenReturn(cityResource);
        ResponseEntity<CityResource> city =cityEntityController.addReport(1l,true);

        assertEquals(200,city.getStatusCode().value());
        assertEquals(cityResource.getId(),city.getBody().getId());
    }
}