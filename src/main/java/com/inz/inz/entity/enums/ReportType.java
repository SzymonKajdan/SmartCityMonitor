package com.inz.inz.entity.enums;

public enum ReportType {

    VANDALISM("wandalizm"),
    HOLE_IN_THE_ROAD("dziura w drodze"),
    COLLISION("kolizja"),
    PNTH("człowiek potrzebujący pomocy"),
    INAPPROPRIATE_BEHAVIURS("niedozowlone zachowanie"),
    POTENTIAL_BULLYING("potęcjalne łamanie prawa(zastraszanie)");

    private String type;

    ReportType(String type) {
        this.type=type;
    }

    public  String getType(){
        return type;
    }


}
