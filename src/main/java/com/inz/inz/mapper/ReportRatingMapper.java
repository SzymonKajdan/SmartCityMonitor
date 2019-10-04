package com.inz.inz.mapper;

import com.inz.inz.entity.ReportRatingEntity;
import com.inz.inz.resoruce.ReportRatingResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class ReportRatingMapper {

    @Mappings({
            @Mapping(target = "mark", expression = "java(reportRatingEntity.getQuantity()!=0?reportRatingEntity.getMarks()/reportRatingEntity.getQuantity():0.0)"),
            @Mapping(target = "isActive", expression = "java(reportRatingEntity.getNotActiveCounter()<10?true:false)")
    })
    public abstract ReportRatingResource mapToReportRatingResource(ReportRatingEntity reportRatingEntity);


}
