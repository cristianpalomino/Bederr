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
import com.bederr.utils.Connectivity;
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


        Connectivity connectivity = new Connectivity();
        connectivity.getNetworkInfo(this);
        if (connectivity.isConnected(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    validateSession();
                }
            }, 2000);
        }else{
            Toast.makeText(this,"Se necesita conexión a Internet.",Toast.LENGTH_SHORT).show();
        }

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
