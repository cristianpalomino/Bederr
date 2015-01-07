package com.bederr.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.beans.Usuario_DTO;
import com.bederr.main.Bederr;
import com.bederr.dialog.Dialog_Maven;
import com.bederr.dialog.Dialog_Olvidaste_Contrasenia;
import com.bederr.dialog.Dialog_Web;
import com.bederr.operations.Operation_Logeo;
import com.bederr.session.Session_Manager;

import pe.bederr.com.R;

import com.bederr.utils.Util_Fonts;

import org.json.JSONException;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Login extends Fragment_Master {

    private Dialog_Maven progressDialog;

    public Fragment_Login() {
        setId_layout(R.layout.fragment_login);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Login newInstance() {
        return new Fragment_Login();
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
        Fragment_Login.this.onFinishLoad(getView());

        Button btningresar = (Button) getView().findViewById(R.id.btningresar);
        btningresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = ((EditText) getView().findViewById(R.id.edtmail)).getText().toString();
                String password = ((EditText) getView().findViewById(R.id.edtcontrasenia)).getText().toString();

                progressDialog = new Dialog_Maven(getActivity());
                progressDialog.show();

                Operation_Logeo operation_logeo = new Operation_Logeo(getActivity());
                operation_logeo.logearUsuarioMaven(email, password);
                operation_logeo.setInterface_operation_logeo(new Operation_Logeo.Interface_Operation_Logeo() {
                    @Override
                    public void logearUsuario(boolean state, Usuario_DTO usuario_dto, String mensaje) {
                        progressDialog.hide();
                        if (state) {
                            try {
                                new Session_Manager(getActivity()).crearSession(usuario_dto, 0);
                                Toast.makeText(getActivity(), "Bienvenido", Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getActivity(), mensaje, Toast.LENGTH_LONG).show();
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
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b,R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Registro.newInstance(),Fragment_Registro.class.getName()).addToBackStack(null).commit();
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
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                trans.remove(Fragment_Login.this);
                trans.commit();
                manager.popBackStack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Login.this.getView().setFocusableInTouchMode(true);
        Fragment_Login.this.getView().requestFocus();
        Fragment_Login.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Fragment_Login.this);
                        trans.commit();
                        manager.popBackStack();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
