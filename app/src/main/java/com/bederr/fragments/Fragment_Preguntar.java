package com.bederr.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.main.Bederr;
import com.bederr.dialog.Dialog_Distritos;
import com.bederr.dialog.Dialog_Maven;
import com.bederr.beans.Distrito_DTO;

import pe.bederr.com.R;

import com.bederr.operations.Operation_Distritos;
import com.bederr.operations.Operation_Preguntas;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Preguntar extends Fragment_Master {

    public Button btn_mi_ubicacion;
    protected String id = "-1";

    public Fragment_Preguntar() {
        setId_layout(R.layout.fragment_preguntar);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Preguntar newInstance() {
        return new Fragment_Preguntar();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        loadDistritos();
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

        final EditText edt_pregunta = (EditText) getView().findViewById(R.id.txt_pregunta);
        final TextView txt_cantidad = (TextView) getView().findViewById(R.id.txt_cantidad_pregunta);
        btn_mi_ubicacion = (Button) getView().findViewById(R.id.btn_mi_ubicacion);
        Button btn_postear = (Button) getView().findViewById(R.id.btn_postear);
        ImageView img_categoria_pregunta = (ImageView) getView().findViewById(R.id.img_categoria_pregunta);

        edt_pregunta.setTypeface(Util_Fonts.setPNALight(getActivity()));
        txt_cantidad.setTypeface(Util_Fonts.setPNACursivaLight(getActivity()));
        btn_mi_ubicacion.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        btn_postear.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        img_categoria_pregunta.setImageResource(((Bederr) getActivity()).getCategoria_dto().getImagencategoria());

        btn_mi_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog_Distritos dialog_distritos = new Dialog_Distritos(getActivity(), (Bederr) getActivity());
                dialog_distritos.getWindow().setWindowAnimations(R.style.Dialog_Animation_DOWN_UP);
                dialog_distritos.show();
            }
        });

        btn_postear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_pregunta.getText().toString().equals("") || edt_pregunta.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Ingrese una pregunta...!", Toast.LENGTH_SHORT).show();
                } else {
                    final Dialog_Maven dialog_maven = new Dialog_Maven(getActivity());
                    dialog_maven.getWindow().setWindowAnimations(R.style.Dialog_Animation_DOWN_UP);
                    dialog_maven.show();

                    try {
                        id = ((Bederr) getActivity()).getDistrito_dto().getIddistrito();
                    }catch (Exception e){
                        id = "-1";
                    }

                    Operation_Preguntas operation_preguntas = new Operation_Preguntas(getActivity());
                    operation_preguntas.agregarPregunta(
                            String.valueOf(((Bederr) getActivity()).getCategoria_dto().getCodigocategoria()),
                            edt_pregunta.getText().toString(),
                            id);

                    operation_preguntas.setInterface_enviar_respuesta(new Operation_Preguntas.Interface_Enviar_Respuesta() {
                        @Override
                        public void enviarRespuesta(boolean status, String mensaje) {
                            Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
                            dialog_maven.hide();
                            ((Bederr) getActivity()).clearHistory();
                            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda).replace(R.id.container, Fragment_Preguntas_v2.newInstance(), Fragment_Preguntas.class.getName()).commit();
                        }
                    });
                }
            }
        });

        edt_pregunta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int max_cant = Integer.parseInt(txt_cantidad.getText().toString());
                if (!(max_cant == 0)) {
                    txt_cantidad.setText(max_cant - 1 + "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        ImageView action_left = (ImageView) getView().findViewById(R.id.action_right);
        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b);
                trans.remove(Fragment_Preguntar.this);
                trans.commit();
                manager.popBackStack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Preguntar.this.getView().setFocusableInTouchMode(true);
        Fragment_Preguntar.this.getView().requestFocus();
        Fragment_Preguntar.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b);
                        trans.remove(Fragment_Preguntar.this);
                        trans.commit();
                        manager.popBackStack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void loadDistritos() {
        Operation_Distritos operation_distritos = new Operation_Distritos(getActivity());
        operation_distritos.getDistritos();
        operation_distritos.setInterface_operation_distritos(new Operation_Distritos.Interface_Operation_Distritos() {
            @Override
            public void getDistritos(boolean status, ArrayList<Distrito_DTO> distrito_dtos) {
                ((Bederr) getActivity()).setDistrito_dtos(distrito_dtos);
            }
        });
    }

    public Button getBtn_mi_ubicacion() {
        return btn_mi_ubicacion;
    }
}
