package com.bederr.fragments;

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

import com.bederr.beans.Usuario_DTO;
import com.bederr.operations.Operation_Registro;
import com.bederr.session.Session_Manager;

import pe.bederr.com.R;

import com.bederr.utils.Util_Fonts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Registro extends Fragment_Master implements DialogInterface.OnClickListener {

    private ProgressDialog progressDialog;

    public Fragment_Registro() {
        setId_layout(R.layout.fragment_registro);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Registro newInstance() {
        return new Fragment_Registro();
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
        Fragment_Registro.this.onFinishLoad(getView());

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

                builderSingle.setAdapter(arrayAdapter, Fragment_Registro.this);
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
                            public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                                ((TextView)getView().findViewById(R.id.edtcumpleanios)).setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        getView().findViewById(R.id.btnregistrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nombre = ((EditText) getView().findViewById(R.id.edtnombre)).getText().toString();
                String apellido = ((EditText) getView().findViewById(R.id.edtapellido)).getText().toString();
                String sexo = ((TextView) getView().findViewById(R.id.edtsexo)).getText().toString();
                String nacimiento = ((TextView) getView().findViewById(R.id.edtcumpleanios)).getText().toString();
                String email = ((EditText) getView().findViewById(R.id.edtmail)).getText().toString();
                String password = ((EditText) getView().findViewById(R.id.edtcontrasenia)).getText().toString();
                String nick = ((EditText) getView().findViewById(R.id.edtnick)).getText().toString();

                try {

                    JSONObject jsonObjectUsuario = new JSONObject();
                    jsonObjectUsuario.put("nombre", nombre);
                    jsonObjectUsuario.put("apellido", apellido);
                    jsonObjectUsuario.put("sexo", sexo);
                    jsonObjectUsuario.put("nacimiento", nacimiento);
                    jsonObjectUsuario.put("email", email);
                    jsonObjectUsuario.put("password", password);
                    jsonObjectUsuario.put("nick", "NO_NICK");
                    jsonObjectUsuario.put("facebook_id", "");
                    jsonObjectUsuario.put("tipo_registro", "1");

                    progressDialog = ProgressDialog.show(getActivity(), null, "Registrando...", true);

                    Operation_Registro operation_registro = new Operation_Registro(getActivity());
                    operation_registro.setJsonObjectUsuario(jsonObjectUsuario);
                    operation_registro.registrarUsuario();
                    operation_registro.setInterface_operation_registro(new Operation_Registro.Interface_Operation_Registro() {
                        @Override
                        public void registrarUsuario(boolean state, Usuario_DTO usuario_dto, String mensaje) {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                trans.remove(Fragment_Registro.this);
                trans.commit();
                manager.popBackStack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Registro.this.getView().setFocusableInTouchMode(true);
        Fragment_Registro.this.getView().requestFocus();
        Fragment_Registro.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Fragment_Registro.this);
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
        if(i == 0){
            ((TextView) getView().findViewById(R.id.edtsexo)).setText("Masculino");
        }else{
            ((TextView) getView().findViewById(R.id.edtsexo)).setText("Femenino");
        }
    }
}
