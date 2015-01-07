package com.bederr.account_v2.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.account_v2.interfaces.OnSuccessLogin;
import com.bederr.main.Bederr;
import com.bederr.dialog.Dialog_Web;
import com.bederr.fragments.Fragment_Master;
import com.bederr.session.Session_Manager;
import com.bederr.account_v2.services.Service_Login;

import pe.bederr.com.R;
import com.bederr.dialog.Dialog_Olvidaste_Contrasenia;
import com.bederr.utils.Util_Fonts;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Login_v2 extends Fragment_Master {

    private ProgressDialog progressDialog;

    public Fragment_Login_v2() {
        setId_layout(R.layout.fragment_login_v2);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Login_v2 newInstance() {
        return new Fragment_Login_v2();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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
        initStyles();
        Fragment_Login_v2.this.onFinishLoad(getView());


        /**
         * Iniciar Session
         */
        Button btningresar = (Button) getView().findViewById(R.id.btningresar);
        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) getView().findViewById(R.id.edtmail)).getText().toString();
                String password = ((EditText) getView().findViewById(R.id.edtcontrasenia)).getText().toString();
                progressDialog = ProgressDialog.show(getActivity(), null, "Ingresando", true);
                Service_Login service_login = new Service_Login(getBederr());
                service_login.sendRequest(email, password);
                service_login.setOnSuccessLogin(new OnSuccessLogin() {
                    @Override
                    public void onSuccessLogin(boolean success, String token_access) {
                        progressDialog.hide();
                        if (success) {
                            Toast.makeText(getBederr(), "Correcto", Toast.LENGTH_SHORT).show();
                            Session_Manager session_manager = new Session_Manager(getBederr());
                            session_manager.crearSession_v2(token_access, 0);
                        } else {
                            Toast.makeText(getBederr(), token_access, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        TextView txt_terminos_y_condiciones = (TextView) getView().findViewById(R.id.txt_terminos_y_condiciones);
        TextView txt_olvidaste_password = (TextView) getView().findViewById(R.id.txt_olvidaste_contrasenia);
        TextView txt_si_aun = (TextView) getView().findViewById(R.id.txt_si_aun_no);

        btningresar.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        txt_terminos_y_condiciones.setTypeface(Util_Fonts.setPNALight(getActivity()));
        txt_olvidaste_password.setTypeface(Util_Fonts.setPNALight(getActivity()));
        txt_si_aun.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));

        getView().findViewById(R.id.btn_registrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Registro_v2.newInstance(), Fragment_Registro_v2.class.getName()).addToBackStack(null).commit();
            }
        });

        txt_olvidaste_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Olvidaste_Contrasenia dialog_web = new Dialog_Olvidaste_Contrasenia(getActivity());
                dialog_web.getWindow().setWindowAnimations(R.style.Dialog_Animation_DOWN_UP);
                dialog_web.show();
            }
        });

        txt_terminos_y_condiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Web dialog_web = new Dialog_Web(getActivity(), (Bederr) getActivity());
                dialog_web.setTitle("Terms y Condiciones");
                dialog_web.getWindow().setWindowAnimations(R.style.Dialog_Animation_DOWN_UP);
                dialog_web.show();
            }
        });
    }

    private void initStyles() {
        ((Button) getView().findViewById(R.id.btningresar)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        ((EditText) getView().findViewById(R.id.edtmail)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((EditText) getView().findViewById(R.id.edtcontrasenia)).setTypeface(Util_Fonts.setPNALight(getActivity()));
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();

        ImageView action_left = (ImageView) getView().findViewById(R.id.action_left);
        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));

        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBederr().finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Login_v2.this.getView().setFocusableInTouchMode(true);
        Fragment_Login_v2.this.getView().requestFocus();
        Fragment_Login_v2.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getBederr().finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
