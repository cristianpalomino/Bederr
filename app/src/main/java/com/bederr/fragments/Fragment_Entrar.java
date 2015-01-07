package com.bederr.fragments;

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

import com.bederr.operations.Operation_Logeo;
import com.bederr.operations.Operation_Registro;
import com.bederr.beans.Usuario_DTO;

import pe.bederr.com.R;
import com.bederr.dialog.Dialog_Maven;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Entrar extends Fragment_Master implements View.OnClickListener{

    private String type;
    private Dialog_Maven progressDialog;

    public Fragment_Entrar() {
        setId_layout(R.layout.fragment_entrar);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Entrar newInstance(String type){
        Fragment_Entrar fragment_entrar = new Fragment_Entrar();
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
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

        Session session = Session.getActiveSession();
        if (session == null) {
            session = new Session(getActivity());
        }
        Session.setActiveSession(session);

        initStyles();

        getView().findViewById(R.id.btnlogeo).setOnClickListener(this);
        getView().findViewById(R.id.btnfacebook).setOnClickListener(this);
        getView().findViewById(R.id.txtregistro).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnlogeo:
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b,R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Login.newInstance(),Fragment_Login.class.getName()).addToBackStack(null).commit();
                break;

            case R.id.btnfacebook:
                loginFacebook();
                break;

            case R.id.txtregistro:
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b,R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Registro.newInstance(),Fragment_Registro.class.getName()).addToBackStack(null).commit();
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
        progressDialog = new Dialog_Maven(getActivity());
        progressDialog.show();
        List<String> permissions = Arrays.asList("email",
                "public_profile",
                "user_friends",
                "user_about_me",
                "user_birthday",
                "user_relationships",
                "user_location",
                "user_likes");
        ParseFacebookUtils.logIn(permissions, getActivity(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                progressDialog.hide();
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
        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        if (user != null) {

                            final String nombre = (String) user.getProperty("first_name");
                            String apellido = user.getLastName();
                            String sexo = (String) user.getProperty("gender");


                            String nacimiento = "00/00/00";
                            if (user.getBirthday() != null) {
                                nacimiento = user.getBirthday();
                            }

                            String email = (String) user.asMap().get("email");
                            String password = "";

                            String nick = "Maven";

                            if ((String) user.getProperty("middle_name") != null) {
                                nick = (String) user.getProperty("middle_name");
                            }

                            final String facebookId = user.getId();

                            try {

                                JSONObject jsonObjectUsuario = new JSONObject();
                                jsonObjectUsuario.put("nombre", nombre);
                                jsonObjectUsuario.put("apellido", apellido);
                                jsonObjectUsuario.put("sexo", sexo);
                                jsonObjectUsuario.put("nacimiento", nacimiento);
                                jsonObjectUsuario.put("email", email);
                                jsonObjectUsuario.put("password", password);
                                jsonObjectUsuario.put("nick", nick);
                                jsonObjectUsuario.put("facebook_id", facebookId);
                                jsonObjectUsuario.put("tipo_registro", "2");

                                Operation_Registro operation_registro = new Operation_Registro(getActivity());
                                operation_registro.setJsonObjectUsuario(jsonObjectUsuario);

                                operation_registro.registrarUsuario();
                                operation_registro.setInterface_operation_registro(new Operation_Registro.Interface_Operation_Registro() {
                                    @Override
                                    public void registrarUsuario(boolean state, Usuario_DTO usuario_dto, String mensaje) {
                                        if (state) {
                                            try {
                                                new Session_Manager(getActivity()).crearSession(usuario_dto,1);
                                                Toast.makeText(getActivity(), "Bienvenido", Toast.LENGTH_LONG).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Operation_Logeo operation_logeo = new Operation_Logeo(getActivity());
                                            operation_logeo.logearUsuarioFacebook(facebookId);
                                            operation_logeo.setInterface_operation_logeo(new Operation_Logeo.Interface_Operation_Logeo() {
                                                @Override
                                                public void logearUsuario(boolean state, Usuario_DTO usuario_dto, String mensaje) {
                                                    progressDialog.hide();
                                                    try {
                                                        new Session_Manager(getActivity()).crearSession(usuario_dto,1);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Toast.makeText(getActivity(), "Bienvenido " + nombre, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
        request.executeAsync();
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
                trans.remove(Fragment_Entrar.this);
                trans.commit();
                manager.popBackStack();
            }
        });

        if(type == "0") {
            action_left.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Entrar.this.getView().setFocusableInTouchMode(true);
        Fragment_Entrar.this.getView().requestFocus();
        Fragment_Entrar.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.abajo_arriba, R.animator.arriba_abajo);
                        trans.remove(Fragment_Entrar.this);
                        trans.commit();
                        manager.popBackStack();

                        if(type == "0") {
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
