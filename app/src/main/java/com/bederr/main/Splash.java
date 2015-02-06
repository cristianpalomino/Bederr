package com.bederr.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bederr.account_v2.interfaces.OnSuccessCounty;
import com.bederr.account_v2.services.Service_Country;
import com.bederr.application.Maven_Application;
import com.bederr.beans_v2.Area_DTO;
import com.bederr.beans_v2.Bederr_DTO;
import com.bederr.beans_v2.Country_DTO;
import com.bederr.beans_v2.Ubication_DTO;
import com.bederr.retail_v2.dialog.Ubication_D;
import com.bederr.util_v2.Bederr_WS;
import com.bederr.utils.GPSTracker;
import com.bederr.utils.GpsLocationTracker;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.LocationCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import pe.bederr.com.R;

/**
 * Created by Gantz on 8/10/14.
 */
public class Splash extends ActionBarActivity {

    public static final String PREFS_NAME = "Bederr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.main_splash);

        ImageView img_splash = (ImageView) findViewById(R.id.img_splash);
        img_splash.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));

        GpsLocationTracker mGpsLocationTracker = new GpsLocationTracker(Splash.this);
        /**
         * Set GPS Location fetched address
         */
        if (mGpsLocationTracker.canGetLocation()) {

        } else {
            mGpsLocationTracker.showSettingsAlert();
        }

        /*
        Service_Country service_country = new Service_Country(this);
        service_country.sendRequest();
        service_country.setOnSuccessCounty(new OnSuccessCounty() {
            @Override
            public void onSuccessCountry(boolean success,
                                         final ArrayList<Country_DTO> country_dtos,
                                         String message) {
                if (success) {
                    GPSTracker gpsTracker = new GPSTracker(Splash.this);
                    final Maven_Application application = ((Maven_Application) getApplication());
                    if (gpsTracker.canGetLocation()) {
                        final LatLng latlng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

                        String URL = Bederr_WS.BEDERR_GEOCODE.
                                replace("LAT", String.valueOf(latlng.latitude)).
                                replace("LON", String.valueOf(latlng.longitude));
                        Log.e("URL", URL);
                        AsyncHttpClient httpClient = new AsyncHttpClient();
                        httpClient.get(Splash.this, URL, null, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);

                                String area = "NO AREA";
                                try {
                                    Bederr_DTO bederr_dto = new Bederr_DTO();
                                    String success = bederr_dto.parseString("status", response);

                                    if (success.equals("OK")) {
                                        JSONObject object = bederr_dto.parseJSONArray("results", response).getJSONObject(0);
                                        JSONArray adresses = object.getJSONArray("address_components");
                                        for (int i = 0; i < adresses.length(); i++) {
                                            JSONObject address = adresses.getJSONObject(i);
                                            JSONArray types = address.getJSONArray("types");
                                            for (int j = 0; j < types.length(); j++) {
                                                String admin_area = "administrative_area_level_1";
                                                String type = types.getString(j);
                                                if (admin_area.equals(type)) {
                                                    area = bederr_dto.parseString("long_name", address);
                                                }
                                            }
                                        }
                                    }


                                    for (int i = 0; i < country_dtos.size(); i++) {
                                        Country_DTO country_dto = country_dtos.get(i);
                                        for (int j = 0; j < country_dto.getAreas().size(); j++) {
                                            Area_DTO area_dto = country_dto.getAreas().get(j);
                                            if (area.equals(area_dto.getName())) {
                                                Ubication_DTO ubication_dto = new Ubication_DTO(latlng,area_dto.getId());
                                                application.setUbication(ubication_dto);

                                                showMessage("GPS : ON" + "\n" +
                                                        "LAT : " + latlng.latitude + "\n" +
                                                        "LNG : " + latlng.longitude + "\n" +
                                                        "CIUDAD : " + area + "\n" +
                                                        "CODE : " + area_dto.getId());
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                validateSession();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                        });
                    } else {
                        Ubication_DTO ubication_dto = new Ubication_DTO(null,null);
                        application.setUbication(ubication_dto);
                        validateSession();
                    }
                }
            }
        });
        */
               /*
            Usa : 35.227672 - -97.734375
            Australia : -27.313214 - 131.132813
             */

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                validateSession();
            }
        }, 2000);
    }

    private void validateSession() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
        if (hasLoggedIn) {
            Intent i = new Intent(Splash.this, Bederr.class);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        } else {
            Intent i = new Intent(Splash.this, Tutorial.class);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }
}
