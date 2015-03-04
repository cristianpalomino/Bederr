package com.bederr.account_v2.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.account_v2.interfaces.OnSuccessLogin;
import com.bederr.dialog.Dialog_Maven;
import com.bederr.fragments.Fragment_Master;
import com.bederr.session.Session_Manager;
import com.bederr.account_v2.services.Service_Login;

import pe.bederr.com.R;

import com.bederr.fragments.Fragment_Registro;
import com.bederr.utils.Util_Fonts;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Entrar_v2 extends Fragment_Master implements View.OnClickListener {

    protected Dialog_Maven dialog_maven;
    protected Session_Manager session_manager;
    private String type;
    private boolean flag = true;

    private Button facebook;
    private Button logueo;
    private TextView registro;

    public Fragment_Entrar_v2() {
        setId_layout(R.layout.fragment_entrar);
        setId_container(R.id.frame_container);
    }


    public static final Fragment_Entrar_v2 newInstance(String type) {
        Fragment_Entrar_v2 fragment_entrar = new Fragment_Entrar_v2();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment_entrar.setArguments(bundle);
        return fragment_entrar;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        type = getArguments().getString("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(inflater, viewGroup, bundle);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override
    protected void initView() {
        super.initView();

        dialog_maven = new Dialog_Maven(getBederr());
        session_manager = new Session_Manager(getActivity());

        Session session = Session.getActiveSession();
        if (session == null) {
            session = new Session(getActivity());
        }
        Session.setActiveSession(session);

        initStyles();

        logueo = (Button) getView().findViewById(R.id.btnlogeo);
        facebook = (Button) getView().findViewById(R.id.btnfacebook);
        registro = (TextView) getView().findViewById(R.id.txtregistro);

        logueo.setOnClickListener(this);
        facebook.setOnClickListener(this);
        registro.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnlogeo:
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Login_v2.newInstance(), Fragment_Login_v2.class.getName()).addToBackStack(null).commit();
                break;

            case R.id.btnfacebook:
                flag = false;
                facebook.setOnClickListener(null);
                facebook.setClickable(false);
                loginFacebook();
                break;

            case R.id.txtregistro:
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Registro_v2.newInstance(), Fragment_Registro.class.getName()).addToBackStack(null).commit();
                break;
        }
    }

    private void initStyles() {
        ((Button) getView().findViewById(R.id.btnlogeo)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        ((Button) getView().findViewById(R.id.btnfacebook)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        ((TextView) getView().findViewById(R.id.txtregistro)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        ((TextView) getView().findViewById(R.id.fragment_central_project)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        ((TextView) getView().findViewById(R.id.fragment_entrar_titulo)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
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
        ParseFacebookUtils.logIn(permissions, getBederr(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                dialog_maven.hide();

                flag = true;
                facebook.setOnClickListener(Fragment_Entrar_v2.this);
                facebook.setClickable(true);

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
        Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                    try {
                        showMessage("FACEBOOK");
                        String email = (String) user.getProperty("email");
                        openLoginIntercom(email);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).executeAsync();


        String access_token_facebook = ParseFacebookUtils.getSession().getAccessToken();
        if (access_token_facebook != null || !access_token_facebook.equals(""))

        {
            Service_Login service_login = new Service_Login(getBederr());
            service_login.sendRequestFacebook(access_token_facebook);
            service_login.setOnSuccessLogin(new OnSuccessLogin() {
                @Override
                public void onSuccessLogin(boolean success, String token_access) {
                    dialog_maven.hide();

                    flag = true;
                    facebook.setOnClickListener(Fragment_Entrar_v2.this);
                    facebook.setClickable(true);

                    if (success) {
                        session_manager.crearSession_v2(token_access, 1);
                    } else {
                        Toast.makeText(getBederr(), token_access, Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();

        ImageView action_left = (ImageView) getView().findViewById(R.id.action_right);
        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.setCustomAnimations(R.animator.abajo_arriba, R.animator.arriba_abajo);
                trans.remove(Fragment_Entrar_v2.this);
                trans.commit();
                manager.popBackStack();
            }
        });
        if (type == "0") {
            action_left.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Entrar_v2.this.getView().setFocusableInTouchMode(true);
        Fragment_Entrar_v2.this.getView().requestFocus();
        Fragment_Entrar_v2.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.abajo_arriba, R.animator.arriba_abajo);
                        trans.remove(Fragment_Entrar_v2.this);
                        trans.commit();
                        manager.popBackStack();
                        if (type == "0") {
                            getActivity().finish();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
