package com.bederr.benefits_v2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bederr.beans_v2.Benefit_Program_DTO;
import com.bederr.benefits_v2.interfaces.OnSuccessPrograms;
import com.bederr.fragments.Fragment_Master;
import com.bederr.session.Session_Manager;

import pe.bederr.com.R;

import com.bederr.benefits_v2.services.Service_Programs;
import com.bederr.benefits_v2.views.Benefit_Program_V;

import java.util.ArrayList;

/**
 * Created by Gantz on 1/08/14.
 */
public class Benefit_Programs_F extends Fragment_Master {

    public Benefit_Programs_F() {
        setId_layout(R.layout.fragment_empresas);
        setId_container(R.id.frame_container);
    }

    public static final Benefit_Programs_F newInstance() {
        return new Benefit_Programs_F();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        final LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.frame_container);

        String token = new Session_Manager(getBederr()).getUserToken();

        Service_Programs service_programs = new Service_Programs(getBederr());
        service_programs.sendRequest(token);
        service_programs.setOnSuccessPrograms(new OnSuccessPrograms() {
            @Override
            public void onSuccessPrograms(boolean success,
                                          ArrayList<Benefit_Program_DTO> benefit_program_dtos,
                                          String count,
                                          String next,
                                          String previous) {
                try {
                    if (success) {
                        for (int i = 0; i < benefit_program_dtos.size(); i++) {
                            Benefit_Program_DTO benefit_program_dto = benefit_program_dtos.get(i);
                            Benefit_Program_V benefit_program_v = new Benefit_Program_V(getBederr(), benefit_program_dto);
                            linearLayout.addView(benefit_program_v);
                            Benefit_Programs_F.this.onFinishLoad(linearLayout);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
