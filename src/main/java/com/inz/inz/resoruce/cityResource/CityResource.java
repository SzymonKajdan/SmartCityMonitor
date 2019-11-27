package com.inz.inz.resoruce.cityResource;

import com.inz.inz.resoruce.reportResource.ReportLight;
import lombok.Data;

import java.util.List;

@Data
public class CityResource extends CityResourceGetLight {

    private String longitude;

    private String latitude;

    private List<ReportLight> reports;
}
