package com.bederr.questions_v2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.beans_v2.Answer_DTO;
import com.bederr.fragments.Fragment_Master;
import com.bederr.main.Bederr;
import com.bederr.dialog.Dialog_Maven;
import com.bederr.questions_v2.interfaces.OnSuccessAnswer;
import com.bederr.questions_v2.services.Service_Answers;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;
import com.bederr.views.View_Local;
import com.bederr.views.View_Pregunta;

import pe.bederr.com.R;

import com.bederr.operations.Operation_Preguntas;
import com.facebook.Session;

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Answer_F extends Fragment_Master {

    private EditText edt_respuesta;

    public Answer_F() {
        setId_layout(R.layout.fragment_responder);
        setId_container(R.id.frame_container);
    }

    public static final Answer_F newInstance() {
        return new Answer_F();
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

        edt_respuesta = (EditText) getView().findViewById(R.id.edt_respuesta);
        edt_respuesta.requestFocus();
        edt_respuesta.setTypeface(Util_Fonts.setPNALight(getActivity()));

        Button btn_responder = (Button) getView().findViewById(R.id.btn_responder);
        btn_responder.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        btn_responder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (edt_respuesta.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Ingrese una respuesta...!", Toast.LENGTH_SHORT).show();
                    } else {
                        Session_Manager session_manager = new Session_Manager(getBederr());
                        Service_Answers service_answers = new Service_Answers(getBederr());
                        service_answers.sendRequest(session_manager.getUserToken(),
                                                    edt_respuesta.getText().toString(),
                                                    String.valueOf(getBederr().getPlace_dto().getId()),
                                                    String.valueOf(getBederr().getQuestion_dto().getId()));
                        service_answers.setOnSuccessAnswer(new OnSuccessAnswer() {
                            @Override
                            public void onSuccessAnswer(boolean success,
                                                        ArrayList<Answer_DTO> answer_dtos,
                                                        String count,
                                                        String next,
                                                        String previous) {
                                if(success){
                                    getActivity().
                                            getSupportFragmentManager().
                                            beginTransaction().
                                            replace(R.id.container, Detail_Question_F.newInstance(),Detail_Question_F.class.getName()).
                                            commit();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                InputMethodManager imm = (InputMethodManager)getBederr().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_respuesta.getWindowToken(), 0);
                closeKeyboard();
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
                trans.remove(Answer_F.this);
                trans.commit();
                manager.popBackStack();
                closeKeyboard();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeKeyboard();
        Answer_F.this.getView().setFocusableInTouchMode(true);
        Answer_F.this.getView().requestFocus();
        Answer_F.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Answer_F.this);
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
