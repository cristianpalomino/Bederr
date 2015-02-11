package com.bederr.questions_v2.fragments;

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

import com.bederr.beans_v2.Question_DTO;
import com.bederr.fragments.Fragment_Master;
import com.bederr.main.Bederr;
import com.bederr.dialog.Dialog_Distritos;
import com.bederr.dialog.Dialog_Maven;
import com.bederr.beans.Distrito_DTO;

import pe.bederr.com.R;

import com.bederr.operations.Operation_Distritos;
import com.bederr.operations.Operation_Preguntas;
import com.bederr.questions_v2.interfaces.OnSuccessQuestion;
import com.bederr.questions_v2.services.Service_Question;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Create_Question_F extends Fragment_Master {

    public Button btn_mi_ubicacion;
    protected String id = "-1";

    public Create_Question_F() {
        setId_layout(R.layout.fragment_preguntar);
        setId_container(R.id.frame_container);
    }

    public static final Create_Question_F newInstance() {
        return new Create_Question_F();
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
                    } catch (Exception e) {
                        id = "-1";
                    }

                    Service_Question service_question = new Service_Question(getBederr());
                    service_question.sendQuestion(new Session_Manager(getBederr()).getUserToken(), getBederr().getCategoria_dto().getCantidadcategoria(), edt_pregunta.getText().toString(),getUbication().getArea());
                    service_question.setOnSuccessQuestion(new OnSuccessQuestion() {
                        @Override
                        public void onSuccessQuestion(boolean success,
                                                      ArrayList<Question_DTO> question_dtos,
                                                      String count,
                                                      String next,
                                                      String previous) {
                            if (success) {
                                dialog_maven.hide();
                                ((Bederr) getActivity()).clearHistory();
                                getActivity().
                                        getSupportFragmentManager().
                                        beginTransaction().
                                        setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda).
                                        replace(R.id.container, Question_F.newInstance(), Question_F.class.getName()).
                                        commit();
                            }
                        }
                    });
                }
                closeKeyboard();
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
                trans.remove(Create_Question_F.this);
                trans.commit();
                manager.popBackStack();
                closeKeyboard();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Create_Question_F.this.getView().setFocusableInTouchMode(true);
        Create_Question_F.this.getView().requestFocus();
        Create_Question_F.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b);
                        trans.remove(Create_Question_F.this);
                        trans.commit();
                        manager.popBackStack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public Button getBtn_mi_ubicacion() {
        return btn_mi_ubicacion;
    }
}
