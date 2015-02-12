package com.bederr.benefits_v2.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bederr.beans_v2.Benefit_Program_DTO;
import com.bederr.benefits_v2.views.Benefit_Program_Corp_V;
import com.bederr.fragments.Fragment_Master;

import pe.bederr.com.R;

import com.bederr.benefits_v2.interfaces.OnSuccessPrograms;
import com.bederr.benefits_v2.services.Service_Programs;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 31/07/14.
 */
public class Benefits_F extends Fragment_Master {


    public Benefits_F() {
        setId_layout(R.layout.fragment_beneficios);
        setId_container(R.id.frame_container);
    }

    public static final Benefits_F newInstance() {
        return new Benefits_F();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        getBederr().setActive_fragment(Benefits_F.newInstance());
        return super.onCreateView(inflater, viewGroup, bundle);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();

        getBederr().
                getSupportFragmentManager().
                beginTransaction().
                replace(R.id.frame_container, Benefit_Programs_F.newInstance())
                .commit();

        final LinearLayout frameempty = (LinearLayout) getView().findViewById(R.id.frameempy);
        final LinearLayout frameempresas = (LinearLayout) getView().findViewById(R.id.frame_container);
        final LinearLayout framegeneral = (LinearLayout) getView().findViewById(R.id.frameempresageneral);

        ((TextView) getView().findViewById(R.id.txtmensajecorporativo)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));

        Session_Manager session_manager = new Session_Manager(getBederr());
        Service_Programs service_programs = new Service_Programs(getBederr());
        service_programs.sendRequestMe(session_manager.getUserToken());
        service_programs.setOnSuccessPrograms(new OnSuccessPrograms() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccessPrograms(boolean success,
                                          ArrayList<Benefit_Program_DTO> benefit_program_dtos,
                                          String count,
                                          String next,
                                          String previous) {
                try {
                    if (success) {
                        frameempty.setVisibility(View.GONE);
                        frameempresas.setVisibility(View.VISIBLE);
                        framegeneral.setBackground(getActivity().getResources().getDrawable(R.drawable.fondo_maven));
                        if (benefit_program_dtos.size() > 0) {
                            for (int i = 0; i < benefit_program_dtos.size(); i++) {
                                Benefit_Program_DTO benefit_program_dto = benefit_program_dtos.get(i);
                                Benefit_Program_Corp_V benefit_program_v = new Benefit_Program_Corp_V(getBederr(), benefit_program_dto);
                                frameempresas.addView(benefit_program_v);
                                Benefits_F.this.onFinishLoad(frameempresas);
                            }
                        } else {
                            frameempty.setVisibility(View.VISIBLE);
                            frameempresas.setVisibility(View.GONE);
                            framegeneral.setBackgroundColor(getActivity().getResources().getColor(R.color.verde_maven));
                        }
                    } else {
                        frameempty.setVisibility(View.VISIBLE);
                        frameempresas.setVisibility(View.GONE);
                        framegeneral.setBackgroundColor(getActivity().getResources().getColor(R.color.verde_maven));
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
        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBederr().sm_menu.toggle();
            }
        });

        ImageView action_right = (ImageView) getView().findViewById(R.id.action_right);
        action_right.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                getBederr().sm_empresas.showMenu();
            }
        });

        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
    }
}