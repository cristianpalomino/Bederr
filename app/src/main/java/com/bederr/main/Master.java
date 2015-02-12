package com.bederr.main;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gantz on 4/02/15.
 */
public class Master extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private Location location;
    private Maven_Application application;
    private OnSuccessArea onSuccessArea;
    public boolean flag = false;

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setOnSuccessArea(OnSuccessArea onSuccessArea) {
        this.onSuccessArea = onSuccessArea;
    }

    @Override
    protected void onStart() {
        super.onStart();
        application = (Maven_Application) getApplication();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(flag){
            restart();
            flag = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            if(location.getLatitude() != 0.0 && location.getLongitude() != 0.0){
                String lat = String.valueOf(location.getLatitude());
                String lng = String.valueOf(location.getLongitude());
                initGeocode(lat, lng);
            }else{
                new Ubication_D(this,true,null,this).show();
            }
        } else {
            Ubication_DTO dto = application.getUbication();
            if (dto != null) {
                if (dto.getArea().equals("-1")) {
                    new Ubication_D(this,true,null,this).show();
                }else{
                    showMessage(dto.getArea());
                }
            }else{
                new Ubication_D(this,true,null,this).show();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        showMessage(connectionResult.toString());
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void showMessage(String message) {
        Toast.makeText(Master.this, message, Toast.LENGTH_SHORT).show();
    }

    private void initGeocode(final String lat, final String lng) {
        Service_Country service_country = new Service_Country(this);
        service_country.sendRequest();
        service_country.setOnSuccessCounty(new OnSuccessCounty() {
            @Override
            public void onSuccessCountry(boolean success,
                                         final ArrayList<Country_DTO> country_dtos,
                                         String message) {

                if (success) {
                    String URL = Bederr_WS.BEDERR_GEOCODE.
                            replace("LAT", lat).
                            replace("LON", lng);
                    AsyncHttpClient httpClient = new AsyncHttpClient();
                    httpClient.get(Master.this, URL, null, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            String area = "1";
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
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    showMessage("No match Geocode");
                                    LatLng latLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                                    Ubication_DTO dto = new Ubication_DTO(latLng,"1");
                                    application.setUbication(dto);
                                    onSuccessArea.onSuccessArea(true,dto);
                                }

                                for (int i = 0; i < country_dtos.size(); i++) {
                                    Country_DTO country_dto = country_dtos.get(i);
                                    for (int j = 0; j < country_dto.getAreas().size(); j++) {
                                        Area_DTO area_dto = country_dto.getAreas().get(j);
                                        if (area.equals(area_dto.getName())) {
                                            LatLng latLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                                            Ubication_DTO dto = new Ubication_DTO(latLng,area_dto.getId());
                                            application.setUbication(dto);
                                            showMessage("GPS : ON" + "\n" +
                                                    "LAT : " + lat + "\n" +
                                                    "LNG : " + lng + "\n" +
                                                    "CIUDAD : " + area + "\n" +
                                                    "CODE : " + area_dto.getId());
                                            onSuccessArea.onSuccessArea(true,dto);
                                            break;
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                showMessage("Fallo Geocode");
                                LatLng latLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                                Ubication_DTO dto = new Ubication_DTO(latLng,"1");
                                application.setUbication(dto);
                                onSuccessArea.onSuccessArea(true,dto);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            onSuccessArea.onSuccessArea(false, null);
                            LatLng latLng = new LatLng(Double.parseDouble("0.0"),Double.parseDouble("0.0"));
                            Ubication_DTO dto = new Ubication_DTO(latLng,"1");
                            application.setUbication(dto);
                            showMessage("Fallo obtener ciudad");
                        }
                    });
                } else {
                    LatLng latLng = new LatLng(Double.parseDouble("0.0"),Double.parseDouble("0.0"));
                    Ubication_DTO dto = new Ubication_DTO(latLng,"1");
                    application.setUbication(dto);
                    showMessage("Fallo obtener ciudad");
                }
            }
        });
    }

    public interface OnSuccessArea {
        public void onSuccessArea(boolean success, Ubication_DTO ubication_dto);
    }


    public void restart(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeKeyboard();
    }

    public void closeKeyboard(){
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
