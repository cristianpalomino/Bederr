package com.bederr.account_v2.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.fragments.Fragment_Master;
import com.bederr.account_v2.interfaces.OnSuccessRegister;
import com.bederr.account_v2.services.Service_Register;

import pe.bederr.com.R;

import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

import java.util.Calendar;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Registro_v2 extends Fragment_Master implements DialogInterface.OnClickListener {

    private ProgressDialog progressDialog;

    public Fragment_Registro_v2() {
        setId_layout(R.layout.fragment_registro);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Registro_v2 newInstance() {
        return new Fragment_Registro_v2();
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
        Fragment_Registro_v2.this.onFinishLoad(getView());

        getView().findViewById(R.id.edtsexo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getBederr());
                builderSingle.setIcon(R.drawable.ic_launcher);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getBederr(),
                        android.R.layout.simple_list_item_1);
                arrayAdapter.add("Masculino");
                arrayAdapter.add("Femenino");

                builderSingle.setAdapter(arrayAdapter, Fragment_Registro_v2.this);
                builderSingle.create();
                builderSingle.show();
            }
        });

        getView().findViewById(R.id.edtcumpleanios).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(getBederr(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                ((TextView) getView().findViewById(R.id.edtcumpleanios)).setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        /**
         * Registro de Usuario v2
         */
        getView().findViewById(R.id.btnregistrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = ((EditText) getView().findViewById(R.id.edtnombre)).getText().toString();
                String apellido = ((EditText) getView().findViewById(R.id.edtapellido)).getText().toString();
                String sexo = ((TextView) getView().findViewById(R.id.edtsexo)).getText().toString();
                String nacimiento = ((TextView) getView().findViewById(R.id.edtcumpleanios)).getText().toString();
                String email = ((EditText) getView().findViewById(R.id.edtmail)).getText().toString();
                String password = ((EditText) getView().findViewById(R.id.edtcontrasenia)).getText().toString();

                if (sexo.equals("Masculino")) {
                    sexo = "m";
                } else {
                    sexo = "f";
                }

                progressDialog = ProgressDialog.show(getActivity(), null, "Registrando", true);

                Service_Register service_register = new Service_Register(getBederr());
                service_register.sendRequest(nombre, apellido, email, nacimiento, sexo, password);
                service_register.setOnSuccessRegister(new OnSuccessRegister() {
                    @Override
                    public void onSuccessRegister(boolean success, String token_access) {
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
    }

    private void initStyles() {
        ((Button) getView().findViewById(R.id.btnregistrar)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        ((EditText) getView().findViewById(R.id.edtnombre)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((EditText) getView().findViewById(R.id.edtapellido)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((EditText) getView().findViewById(R.id.edtmail)).setTypeface(Util_Fonts.setPNALight(getActivity()));


        ((TextView) getView().findViewById(R.id.edtcumpleanios)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((TextView) getView().findViewById(R.id.edtcumpleanios)).setKeyListener(null);

        ((TextView) getView().findViewById(R.id.edtsexo)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((TextView) getView().findViewById(R.id.edtsexo)).setKeyListener(null);

        ((EditText) getView().findViewById(R.id.edtcontrasenia)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((EditText) getView().findViewById(R.id.edtnick)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((EditText) getView().findViewById(R.id.edtrepcontrasenia)).setTypeface(Util_Fonts.setPNALight(getActivity()));
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
                trans.remove(Fragment_Registro_v2.this);
                trans.commit();
                manager.popBackStack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Registro_v2.this.getView().setFocusableInTouchMode(true);
        Fragment_Registro_v2.this.getView().requestFocus();
        Fragment_Registro_v2.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Fragment_Registro_v2.this);
                        trans.commit();
                        manager.popBackStack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == 0) {
            ((TextView) getView().findViewById(R.id.edtsexo)).setText("Masculino");
        } else {
            ((TextView) getView().findViewById(R.id.edtsexo)).setText("Femenino");
        }
    }
}
