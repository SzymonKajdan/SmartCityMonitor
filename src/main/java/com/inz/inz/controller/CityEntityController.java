package com.inz.inz.controller;

import com.inz.inz.ExcpetionHandler.DbException;
import com.inz.inz.adapter.adapterImpl.CityAdapterImpl;
import com.inz.inz.resoruce.CityResource;
import com.inz.inz.resoruce.CityResourceGetLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("cities")
public class CityEntityController {

    private  static final String  MEDIA_TYPE = "application/json";

    @Autowired
    CityAdapterImpl cityAdapter;

    @GetMapping(value = "/getCities", produces = MEDIA_TYPE )
    public ResponseEntity<List<CityResourceGetLight>> addReport() throws DbException {
       List<CityResourceGetLight> cities= cityAdapter.getCities();
       return new ResponseEntity<>(cities,HttpStatus.OK);
    }

    @GetMapping(value = "/getCityById/{id}", produces = MEDIA_TYPE )
    public ResponseEntity<CityResource> addReport(@PathVariable Long id) throws DbException {
        CityResource city= cityAdapter.getCity(id);
        return new ResponseEntity<>(city,HttpStatus.OK);
    }

}
