package com.bederr.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bederr.main.Bederr;
import com.bederr.questions_v2.fragments.Question_F;
import com.bederr.retail_v2.fragments.Explore_F;
import com.bederr.account_v2.fragments.Fragment_Entrar_v2;
import com.bederr.account_v2.fragments.Fragment_Perfil_v2;

import intercom.intercomsdk.Intercom;
import pe.bederr.com.R;
import com.bederr.benefits_v2.fragments.Benefits_F;
import com.bederr.retail_v2.fragments.Listing_F;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.HashMap;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Menu extends Fragment implements View.OnClickListener {

    private LinearLayout optionpreguntas;
    private LinearLayout optionexplorar;
    private LinearLayout optionperfil;
    private LinearLayout optionlistas;
    private LinearLayout optioncorporativo;

    public static final Fragment_Menu newInstance() {
        return new Fragment_Menu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initStyles();

        optionpreguntas = (LinearLayout) getView().findViewById(R.id.option_preguntas);
        optionexplorar = (LinearLayout) getView().findViewById(R.id.option_explorar);
        optionperfil = (LinearLayout) getView().findViewById(R.id.option_perfil);
        optioncorporativo = (LinearLayout) getView().findViewById(R.id.option_corporativo);
        optionlistas = (LinearLayout) getView().findViewById(R.id.option_listas);

        optionpreguntas.setOnClickListener(this);
        optionexplorar.setOnClickListener(this);
        optionperfil.setOnClickListener(this);
        optioncorporativo.setOnClickListener(this);
        optionlistas.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ((Bederr) getActivity()).sm_menu.setMode(SlidingMenu.LEFT);
        setMenuIndicator(v.getId());

        HashMap<String, Object> attributes = new HashMap <String, Object>();
        HashMap<String, Object> customAttributes = new HashMap <String, Object>();
        customAttributes.put("paid_subscriber","Yes");
        customAttributes.put("monthly_spend",155.5);
        customAttributes.put("team_mates",3);
        customAttributes.put("email","ca.palomino.rivera@gmail.com");
        attributes.put("custom_attributes", customAttributes);
        Intercom.updateUser(attributes);


        boolean session = new Session_Manager(getActivity()).isLogin();
        switch (v.getId()) {
            case R.id.option_preguntas:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, Question_F.newInstance(), "fragment_entrar").commit();
                break;
            case R.id.option_explorar:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, Explore_F.newInstance(), "fragment_explorar").commit();
                break;
            case R.id.option_listas:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, Listing_F.newInstance(),Fragment_Listas.class.getName()).commit();
                break;
            case R.id.option_perfil:
                if (session) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, Fragment_Perfil_v2.newInstance(),Fragment_Perfil_v2.class.getName()).commit();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, Fragment_Entrar_v2.newInstance("0"), Fragment_Entrar_v2.class.getName()).commit();
                }
                break;
            case R.id.option_corporativo:
                if (session) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, Benefits_F.newInstance(),Benefits_F.class.getName()).commit();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, Fragment_Entrar_v2.newInstance("0"), Fragment_Entrar_v2.class.getName()).commit();
                }
                break;
        }
        ((Bederr) getActivity()).sm_menu.toggle();
        ((Bederr)getActivity()).clearHistory();
    }

    private void initStyles() {
        ((TextView) getView().findViewById(R.id.fragment_menu_preguntas)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        ((TextView) getView().findViewById(R.id.fragment_menu_explorar)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        ((TextView) getView().findViewById(R.id.fragment_menu_listas)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        ((TextView) getView().findViewById(R.id.fragment_menu_corporativo)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        ((TextView) getView().findViewById(R.id.fragment_menu_perfil)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
    }

    private void setMenuIndicator(int res_id) {
        if(res_id == R.id.option_preguntas){
            optionpreguntas.setBackgroundResource(R.color.verde_claro_emerald);
            optionexplorar.setBackgroundResource(R.color.verde_maven);
            optionperfil.setBackgroundResource(R.color.verde_maven);
            optioncorporativo.setBackgroundResource(R.color.verde_maven);
            optionlistas.setBackgroundResource(R.color.verde_maven);
        }
        else if(res_id == R.id.option_explorar){
            optionpreguntas.setBackgroundResource(R.color.verde_maven);
            optionexplorar.setBackgroundResource(R.color.verde_claro_emerald);
            optionperfil.setBackgroundResource(R.color.verde_maven);
            optioncorporativo.setBackgroundResource(R.color.verde_maven);
            optionlistas.setBackgroundResource(R.color.verde_maven);
        }
        else if(res_id == R.id.option_listas){
            optionpreguntas.setBackgroundResource(R.color.verde_maven);
            optionexplorar.setBackgroundResource(R.color.verde_maven);
            optionperfil.setBackgroundResource(R.color.verde_maven);
            optioncorporativo.setBackgroundResource(R.color.verde_maven);
            optionlistas.setBackgroundResource(R.color.verde_claro_emerald);
        }
        else if(res_id == R.id.option_perfil){
            optionpreguntas.setBackgroundResource(R.color.verde_maven);
            optionexplorar.setBackgroundResource(R.color.verde_maven);
            optionperfil.setBackgroundResource(R.color.verde_claro_emerald);
            optioncorporativo.setBackgroundResource(R.color.verde_maven);
            optionlistas.setBackgroundResource(R.color.verde_maven);
        }
        if(res_id == R.id.option_corporativo){
            optionpreguntas.setBackgroundResource(R.color.verde_maven);
            optionexplorar.setBackgroundResource(R.color.verde_maven);
            optionperfil.setBackgroundResource(R.color.verde_maven);
            optioncorporativo.setBackgroundResource(R.color.verde_claro_emerald);
            optionlistas.setBackgroundResource(R.color.verde_maven);
        }
    }
}
