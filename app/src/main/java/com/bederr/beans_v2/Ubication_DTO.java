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

        if(area != null){
            this.area = area;
        }
        else{
            this.area = "-1";
        }
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getLatitude() {
        if(latLng != null){
            String lat = String.valueOf(latLng.latitude);
            if(lat != null){
                return lat;
            }else{
                return "0.0";
            }
        }else{
            return "0.0";
        }
    }

    public String getLongitude() {
        if(latLng != null){
            String lng = String.valueOf(latLng.longitude);
            if(lng != null){
                return lng;
            }else{
                return "0.0";
            }
        }else{
            return "0.0";
        }
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
