package com.inz.inz.adapter;


import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.resoruce.CityPostResource;
import com.inz.inz.resoruce.cityresource.CityResource;
import com.inz.inz.resoruce.cityresource.CityResourceGetLight;

import java.util.List;

public interface CityAdapter {

    List<CityResourceGetLight> getCities() throws DbException;

    CityResource getCity(Long id, boolean reportsAcitve) throws DbException;

    void addCity(CityPostResource cityPostResource) throws DbException;
}
