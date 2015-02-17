package com.bederr.application;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import pe.bederr.com.R;

import com.bederr.beans_v2.Location_DTO;
import com.bederr.beans_v2.Ubication_DTO;
import com.bederr.retail_v2.dialog.Ubication_D;
import com.bederr.utils.Locator;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by Gantz on 28/07/14.
 */
public class Maven_Application extends Application {

    private Ubication_DTO ubication;
    private Ubication_D ubication_d;
    private Location_DTO location_dto;
    private boolean message;

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));
    }

    public Location_DTO getLocation_dto() {
        return location_dto;
    }

    public void setLocation_dto(Location_DTO location_dto) {
        this.location_dto = location_dto;
    }

    public Ubication_DTO getUbication() {
        return ubication;
    }

    public void setUbication(Ubication_DTO ubication) {
        this.ubication = ubication;
    }

    public Ubication_D getUbication_d() {
        return ubication_d;
    }

    public void setUbication_d(Ubication_D ubication_d) {
        this.ubication_d = ubication_d;
    }

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }
}
