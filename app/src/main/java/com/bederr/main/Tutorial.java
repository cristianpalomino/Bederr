package com.bederr.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.account_v2.interfaces.OnSuccessLogin;
import com.bederr.account_v2.services.Service_Login;
import com.bederr.dialog.Dialog_Maven;
import com.bederr.fragments.Fragment_Bederr;
import com.bederr.fragments.Fragment_Tutorial;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.util.Arrays;
import java.util.List;

import pe.bederr.com.R;

/**
 * Created by Gantz on 8/10/14.
 */
public class Tutorial extends ActionBarActivity {

    protected Adapter_Fragment mAdapter;
    protected ViewPager mPager;
    protected PageIndicator mIndicator;
    protected Dialog_Maven dialog_maven;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.main_tutorial);

        dialog_maven = new Dialog_Maven(this);
        mAdapter = new Adapter_Fragment(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        Button btn_bederr = (Button) findViewById(R.id.btnbederr);
        Button btn_fb = (Button) findViewById(R.id.btnfacebook);
        TextView txt_skip = (TextView) findViewById(R.id.txt_skip);

        txt_skip.setTypeface(Util_Fonts.setPNALight(Tutorial.this));
        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(Splash.PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();

                startActivity(new Intent(Tutorial.this, Bederr.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                Tutorial.this.finish();

                editor.putBoolean("hasLoggedIn", true);
                editor.commit();
            }
        });
        btn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFacebook();
            }
        });
        btn_bederr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(Splash.PREFS_NAME, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();

                Intent intent = new Intent(Tutorial.this, Bederr.class);
                intent.putExtra("flag",0);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                Tutorial.this.finish();

                editor.putBoolean("hasLoggedIn", true);
                editor.commit();
            }
        });

        btn_bederr.setTypeface(Util_Fonts.setPNASemiBold(Tutorial.this));
        btn_fb.setTypeface(Util_Fonts.setPNASemiBold(Tutorial.this));
    }

    class Adapter_Fragment extends FragmentPagerAdapter {

        int[] titulo = {R.string.titulo_tutorial_beneficios, R.string.titulo_tutorial_pregunta, R.string.titulo_tutorial_explora, R.string.titulo_tutorial_listas};
        int[] descripciones = {R.string.descripcion_tutorial_beneficios, R.string.descripcion_tutorial_pregunta, R.string.descripcion_tutorial_explora, R.string.descripcion_tutorial_lista};
        int[] iconos = {R.drawable.icono_beneficios_tutorial, R.drawable.icono_preguntar_tutorial, R.drawable.icono_explorar_tutorial, R.drawable.icono_listas_tutorial};
        int[] colores = {R.color.color_beneficios, R.color.color_pregunta, R.color.color_explora, R.color.color_listas};

        public Adapter_Fragment(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 4){
                return Fragment_Bederr.newInstance();
            }else{
                return Fragment_Tutorial.newInstance(titulo[position], descripciones[position], iconos[position], colores[position]);
            }
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titulo + "";
        }

        public void setCount(int count) {
            if (count > 0 && count <= 10) {
                titulo[count] = count;
                notifyDataSetChanged();
            }
        }
    }

    private void loginFacebook() {
        dialog_maven.show();
        List<String> permissions = Arrays.asList("email",
                "public_profile",
                "user_friends",
                "user_about_me",
                "user_birthday",
                "user_relationships",
                "user_location",
                "user_likes");
        ParseFacebookUtils.logIn(permissions,Tutorial.this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                dialog_maven.hide();
                if (user == null) {
                    Log.e("Facebook", err.getMessage());
                } else if (user.isNew()) {
                    makeMeRequest();
                } else {
                    makeMeRequest();
                }
            }
        });
    }

    private void makeMeRequest() {
        String access_token_facebook = ParseFacebookUtils.getSession().getAccessToken();
        if (access_token_facebook != null || !access_token_facebook.equals("")) {
            Service_Login service_login = new Service_Login(Tutorial.this);
            service_login.sendRequestFacebook(access_token_facebook);
            service_login.setOnSuccessLogin(new OnSuccessLogin() {
                @Override
                public void onSuccessLogin(boolean success, String token_access) {
                    dialog_maven.hide();
                    if (success) {
                        Toast.makeText(Tutorial.this, "Correcto", Toast.LENGTH_SHORT).show();
                        Session_Manager session_manager = new Session_Manager(Tutorial.this);
                        session_manager.crearSessionTutorial_v2(token_access, 1);
                    } else {
                        Toast.makeText(Tutorial.this, token_access, Toast.LENGTH_SHORT).show();
                    }

                    SharedPreferences settings = getSharedPreferences(Splash.PREFS_NAME, 0); // 0 - for private mode
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("hasLoggedIn", true);
                    editor.commit();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}
