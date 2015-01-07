package com.bederr.beans_v2;

/**
 * Created by Gantz on 22/12/14.
 */
public class Point_DTO extends Bederr_DTO {

    private String latitude = "latitude";
    private String longitude = "longitude";

    public String getLatitude() {
        return parseString(latitude,getDataSource());
    }

    public String getLongitude() {
        return parseString(longitude,getDataSource());
    }
}
