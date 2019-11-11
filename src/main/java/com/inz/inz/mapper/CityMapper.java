package com.inz.inz.mapper;

import com.inz.inz.entity.CityEntity;
import com.inz.inz.resoruce.cityResource.CityResource;
import com.inz.inz.resoruce.cityResource.CityResourceGetLight;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CityMapper {

    @Autowired
    ReportMapper reportMapper;

    @Mappings({
            @Mapping(target = "cityName", source = "cityEntity.name"),
            @Mapping(target = "id", source = "cityEntity.id")
    })
    public abstract CityResourceGetLight mapToCityResourceGetLight(CityEntity cityEntity);

    @Mappings({
            @Mapping(target = "longitude", source = "cityEntity.longitude"),
            @Mapping(target = "cityName", source = "cityEntity.name"),
            @Mapping(target = "id", source = "cityEntity.id"),
            @Mapping(target = "latitude", source = "cityEntity.latitude"),
            @Mapping(target = "reports", ignore = true)
    })
    public abstract CityResource mapToCityResource(CityEntity cityEntity);

    @AfterMapping
    protected void mapCityResourceFields(CityEntity cityEntity, @MappingTarget CityResource cityResource) {
        if (cityEntity.getReportList() != null) {
            cityResource.setReports(cityEntity.getReportList().stream().map(reportMapper::mapToReportLigth).collect(Collectors.toList()));
        } else {
            cityResource.setReports(new ArrayList<>());
        }
    }
}
