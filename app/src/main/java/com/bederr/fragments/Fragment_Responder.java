package com.bederr.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.main.Bederr;
import com.bederr.dialog.Dialog_Maven;
import com.bederr.utils.Util_Fonts;
import com.bederr.views.View_Local;
import com.bederr.views.View_Pregunta;

import pe.bederr.com.R;

import com.bederr.operations.Operation_Preguntas;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Responder extends Fragment_Master {

    public Fragment_Responder() {
        setId_layout(R.layout.fragment_responder);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Responder newInstance() {
        return new Fragment_Responder();
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

        final View_Pregunta view_pregunta = new View_Pregunta(getActivity(), ((Bederr) getActivity()).getPregunta_dto());
        view_pregunta.hideContainerRespuestas();
        view_pregunta.setClickable(false);
        getLayout().addView(view_pregunta);

        final View_Local view_local = new View_Local(getActivity(), ((Bederr) getActivity()).getLocal_dto());
        getLayout().addView(view_local);

        final EditText edt_respuesta = (EditText) getView().findViewById(R.id.edt_respuesta);
        edt_respuesta.requestFocus();
        edt_respuesta.setTypeface(Util_Fonts.setPNALight(getActivity()));

        getBederr().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        Button btn_responder = (Button) getView().findViewById(R.id.btn_responder);
        btn_responder.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        btn_responder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (edt_respuesta.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Ingrese una respuesta...!", Toast.LENGTH_SHORT).show();
                    } else {
                        final Dialog_Maven dialog_maven = new Dialog_Maven(getActivity());
                        dialog_maven.getWindow().setWindowAnimations(R.style.Dialog_Animation_DOWN_UP);
                        dialog_maven.show();

                        String idLocal = ((Bederr) getActivity()).getLocal_dto().getJsonObject().getString("idLocal");
                        String idPregunta = ((Bederr) getActivity()).getPregunta_dto().getJsonObject().getString("id");
                        String respuesta = edt_respuesta.getText().toString();

                        Operation_Preguntas operation_preguntas = new Operation_Preguntas(getActivity());
                        operation_preguntas.sendRespuesta(idLocal, idPregunta, respuesta);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
                trans.remove(Fragment_Responder.this);
                trans.commit();
                manager.popBackStack();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Responder.this.getView().setFocusableInTouchMode(true);
        Fragment_Responder.this.getView().requestFocus();
        Fragment_Responder.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Fragment_Responder.this);
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
