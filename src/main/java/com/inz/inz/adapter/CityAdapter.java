package com.inz.inz.adapter;


import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.resoruce.cityResource.CityResource;
import com.inz.inz.resoruce.cityResource.CityResourceGetLight;

import java.util.List;

public interface CityAdapter {

    List<CityResourceGetLight> getCities() throws DbException;

    CityResource getCity(Long id, boolean reportsAcitve) throws DbException;
}
