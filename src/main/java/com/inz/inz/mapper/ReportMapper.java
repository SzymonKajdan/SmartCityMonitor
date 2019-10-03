package com.inz.inz.mapper;

import com.inz.inz.ExcpetionHandler.DbException;
import com.inz.inz.ExcpetionHandler.EnumExcpetion;
import com.inz.inz.ExcpetionHandler.Field;
import com.inz.inz.entity.CityEntity;
import com.inz.inz.entity.ReportEntity;
import com.inz.inz.entity.enums.ReportType;
import com.inz.inz.repository.CityEntityRepository;
import com.inz.inz.resoruce.ReportLight;
import com.inz.inz.resoruce.ReportResourcePost;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Optional;

@Mapper(componentModel="spring")
public abstract class ReportMapper {

    @Autowired
    CityEntityRepository cityEntityRepository;


    @Mappings({
            @Mapping(target = "id",source = "res.id"),
            @Mapping(target = "longitude", source = "res.longitude"),
            @Mapping(target = "latitude", source = "res.latitude"),
            @Mapping(target = "description", source = "res.description"),
            @Mapping(target = "photo", source = "res.photo"),
            @Mapping(target = "video", source = "res.video"),

    })
    public  abstract ReportLight mapToReportLigth(ReportEntity res);

    @Mappings({
            @Mapping(target = "longitude", source = "res.longitude"),
            @Mapping(target = "latitude", source = "res.latitude"),
            @Mapping(target = "description", source = "res.description"),
            @Mapping(target = "photo", source = "res.photo"),
            @Mapping(target = "video", source = "res.video"),
            @Mapping(target = "reportType",ignore = true)
    })
    public abstract ReportEntity mapToEntity(ReportResourcePost res) throws DbException, EnumExcpetion;

    @AfterMapping
    protected void fillReportEntityFields(ReportResourcePost res, @MappingTarget ReportEntity entity) throws DbException, EnumExcpetion {
        Optional<CityEntity> cityEntity = cityEntityRepository.findByName(res.getCityName());
        if (!cityEntity.isPresent()) {
            Field field = new Field();
            field.setField(null);
            field.setDetails("City doesn't exist");
            throw new DbException("Error at getting info from cityEntity ", "D3", field);
        } else {
            entity.setCity(cityEntity.get());
        }
        entity.setDateReport(new Date());

        try {
            entity.setReportType(ReportType.valueOf(res.getReportType()));
        } catch (IllegalArgumentException ex) {
            throw new EnumExcpetion("ReportTypeNotExist", "E1", new Field());
        }
    }
}
