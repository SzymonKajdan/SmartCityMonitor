package com.inz.inz.controller;

import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.adapter.adapterImpl.CityAdapterImpl;
import com.inz.inz.resoruce.cityResource.CityResource;
import com.inz.inz.resoruce.cityResource.CityResourceGetLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cities")
public class CityEntityController {

    private  static final String  MEDIA_TYPE = "application/json";

    @Autowired
    CityAdapterImpl cityAdapter;

    @GetMapping(value = "/getCities", produces = MEDIA_TYPE )
    public ResponseEntity<List<CityResourceGetLight>> addReport () throws DbException {

       List<CityResourceGetLight> cities= cityAdapter.getCities();
       return new ResponseEntity<>(cities,HttpStatus.OK);
    }



    @GetMapping(value = "/getCityById/{id}", produces = MEDIA_TYPE )
    public ResponseEntity<CityResource> addReport(
            @PathVariable Long id,
            @RequestParam (value="reportsAcitve", required = false, defaultValue = "true")
                    boolean reportsAcitve
                                                  ) throws DbException {
        CityResource city= cityAdapter.getCity(id,reportsAcitve);
        return new ResponseEntity<>(city,HttpStatus.OK);
    }

}
