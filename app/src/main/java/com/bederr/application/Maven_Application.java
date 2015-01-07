package com.bederr.application;

import android.app.Application;

import pe.bederr.com.R;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by Gantz on 28/07/14.
 */
public class Maven_Application extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));
    }
}
