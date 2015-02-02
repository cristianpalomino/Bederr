package com.bederr.beans_v2;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Gantz on 24/01/15.
 */
public class Ubication_DTO {

    private LatLng latLng;
    private String area;

    private String latitude;
    private String longitude;

    public Ubication_DTO(LatLng latLng, String area) {
        this.latLng = latLng;
        this.area = area;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getLatitude() {
        return String.valueOf(latLng.latitude);
    }

    public String getLongitude() {
        return String.valueOf(latLng.longitude);
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

}
