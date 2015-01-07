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
import android.widget.EditText;
import android.widget.ImageView;

import com.bederr.beans.Local_DTO;
import com.bederr.operations.Operation_Busquedas;
import com.bederr.operations.Operation_Locales_Cercanos;
import com.bederr.utils.Util_Fonts;
import com.bederr.views.View_Local_Responder;

import pe.bederr.com.R;

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Local_Pregunta extends Fragment_Master {

    public Fragment_Local_Pregunta() {
        setId_layout(R.layout.fragment_local_pregunta);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Local_Pregunta newInstance() {
        return new Fragment_Local_Pregunta();
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
        Operation_Locales_Cercanos operation_locales_cercanos = new Operation_Locales_Cercanos(getActivity());
        operation_locales_cercanos.setPage(1);
        operation_locales_cercanos.getLocalesCercanos();
        operation_locales_cercanos.setInterface_operation_locales_cercanos(new Operation_Locales_Cercanos.Interface_Operation_Locales_Cercanos() {
            @Override
            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages) {
                try {
                    for (int i = 0; i < local_dtos.size(); i++) {
                        Local_DTO local_dto = local_dtos.get(i);
                        View_Local_Responder view_local_responder = new View_Local_Responder(getActivity(), local_dto);
                        view_local_responder.setTag(local_dto);
                        getLayout().addView(view_local_responder);
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
        ImageView action_left = (ImageView) getView().findViewById(R.id.action_right);
        EditText action_middle = (EditText) getView().findViewById(R.id.edt_action_middle);
        action_middle.setTypeface(Util_Fonts.setPNALight(getActivity()));

        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b);
                trans.remove(Fragment_Local_Pregunta.this);
                trans.commit();
                manager.popBackStack();
            }
        });

        action_middle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                operation_busquedas.buscarLocalesPorNombre(s.toString(),1);
                operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                    @Override
                    public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int paramInt, String message) {
                        try {
                            if(status){
                                getLayout().removeAllViews();
                                for (int i = 0; i < local_dtos.size(); i++) {
                                    Local_DTO local_dto = local_dtos.get(i);
                                    View_Local_Responder view_local_responder = new View_Local_Responder(getActivity(), local_dto);
                                    view_local_responder.setTag(local_dto);
                                    getLayout().addView(view_local_responder);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        action_middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, Fragment_Busqueda.newInstance(), "fragment_busqueda").addToBackStack("a").commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Local_Pregunta.this.getView().setFocusableInTouchMode(true);
        Fragment_Local_Pregunta.this.getView().requestFocus();
        Fragment_Local_Pregunta.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b);
                        trans.remove(Fragment_Local_Pregunta.this);
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
