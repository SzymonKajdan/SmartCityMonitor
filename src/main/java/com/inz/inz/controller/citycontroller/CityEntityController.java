package com.inz.inz.controller.citycontroller;

import com.inz.inz.adapter.adapterimpl.CityAdapterImpl;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.resoruce.CityPostResource;
import com.inz.inz.resoruce.cityresource.CityResource;
import com.inz.inz.resoruce.cityresource.CityResourceGetLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping({"cities", "/api/cities"})
public class CityEntityController {

    private static final String MEDIA_TYPE = "application/json";

    @Autowired
    CityAdapterImpl cityAdapter;

    @GetMapping(value = "/getCities", produces = MEDIA_TYPE)
    public ResponseEntity<List<CityResourceGetLight>> addReport() throws DbException {

        List<CityResourceGetLight> cities = cityAdapter.getCities();
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }


    @GetMapping(value = "/getCityById/{id}", produces = MEDIA_TYPE)
    public ResponseEntity<CityResource> addReport(
            @PathVariable Long id,
            @RequestParam(value = "reportsAcitve", required = false, defaultValue = "true") boolean reportsAcitve) throws DbException {
        CityResource city = cityAdapter.getCity(id, reportsAcitve);
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/addCity", produces = MEDIA_TYPE)
    public ResponseEntity addCity(@Valid @RequestBody CityPostResource cityPostResource) throws DbException {
        cityAdapter.addCity(cityPostResource);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
