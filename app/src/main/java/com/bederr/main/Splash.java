package com.bederr.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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

        ImageView img_splash = (ImageView)findViewById(R.id.img_splash);
        img_splash.startAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_in));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
                if(hasLoggedIn){
                    Intent i = new Intent(Splash.this, Bederr.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }else{
                    Intent i = new Intent(Splash.this, Tutorial.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }
        }, 2000);
    }
}
