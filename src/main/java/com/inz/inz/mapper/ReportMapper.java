package com.inz.inz.mapper;

import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.exceptionhandler.EnumExcpetion;
import com.inz.inz.exceptionhandler.ErrorSpecifcation;
import com.inz.inz.exceptionhandler.Field;
import com.inz.inz.entity.CityEntity;
import com.inz.inz.entity.ReportEntity;
import com.inz.inz.entity.enums.ReportType;
import com.inz.inz.repository.CityEntityRepository;
import com.inz.inz.resoruce.ReportLight;
import com.inz.inz.resoruce.ReportResource;
import com.inz.inz.resoruce.ReportResourcePost;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class ReportMapper {

    @Autowired
    CityEntityRepository cityEntityRepository;




    @Mappings({
            @Mapping(target = "id", source = "res.id"),
            @Mapping(target = "longitude", source = "res.longitude"),
            @Mapping(target = "latitude", source = "res.latitude"),
            @Mapping(target = "description", source = "res.description"),
            @Mapping(target = "photo", source = "res.photo"),
            @Mapping(target = "video", source = "res.video"),
            @Mapping(target = "isActive", expression = "java( res.getReportRating().getNotActiveCounter()<10&& res.getReportRating().getFalseReportQuantity()<10?true:false)")
    })
    public abstract ReportLight mapToReportLigth(ReportEntity res);

    @Mappings({
            @Mapping(target = "id", source = "res.id"),
            @Mapping(target = "longitude", source = "res.longitude"),
            @Mapping(target = "latitude", source = "res.latitude"),
            @Mapping(target = "description", source = "res.description"),
            @Mapping(target = "photo", source = "res.photo"),
            @Mapping(target = "video", source = "res.video"),
            @Mapping(target = "reportDate",expression = "java(res.getDateReport().toString())"),
            @Mapping(target = "userId",expression = "java(res.getUser().getId())"),
            @Mapping(target = "reportRating",ignore = true),
            @Mapping(target = "mark", expression = "java( res.getReportRating().getQuantity()!=0?res.getReportRating().getMarks()/res.getReportRating().getQuantity():0.0)"),
            @Mapping(target = "isActive", expression = "java( res.getReportRating().getNotActiveCounter()<10&& res.getReportRating().getFalseReportQuantity()<10?true:false)")
    })
    public abstract ReportResource mapToReport(ReportEntity res);

    @Mappings({
            @Mapping(target = "longitude", source = "res.longitude"),
            @Mapping(target = "latitude", source = "res.latitude"),
            @Mapping(target = "description", source = "res.description"),
            @Mapping(target = "photo", source = "res.photo"),
            @Mapping(target = "video", source = "res.video"),
            @Mapping(target = "reportType", ignore = true)
    })
    public abstract ReportEntity mapToEntity(ReportResourcePost res) throws DbException, EnumExcpetion;

    @AfterMapping
    protected void fillReportEntityFields(ReportResourcePost res, @MappingTarget ReportEntity entity) throws DbException, EnumExcpetion {
        Optional<CityEntity> cityEntity = cityEntityRepository.findByName(res.getCityName());
        if (!cityEntity.isPresent()) {
            Field field = new Field();
            field.setField("cityName");
            field.setDetails("City " + res.getCityName());
            throw new DbException(ErrorSpecifcation.RESURCENOTEXIST.getDetails(), ErrorSpecifcation.RESURCENOTEXIST.getCode(), field);
        } else {
            entity.setCity(cityEntity.get());
        }
        entity.setDateReport(new Date());

        try {
            entity.setReportType(ReportType.valueOf(res.getReportType()));
        } catch (IllegalArgumentException ex) {
            throw new EnumExcpetion(ErrorSpecifcation.RESURCENOTEXIST.getDetails() + res.getReportType(), ErrorSpecifcation.RESURCENOTEXIST.getCode(), new Field());
        }
    }


}
