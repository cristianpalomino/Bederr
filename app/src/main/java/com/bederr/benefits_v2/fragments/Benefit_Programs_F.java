package com.bederr.benefits_v2.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bederr.beans_v2.Benefit_Program_DTO;
import com.bederr.benefits_v2.adapter.Benefit_A;
import com.bederr.benefits_v2.dialog.Life_Dialog;
import com.bederr.benefits_v2.interfaces.OnSuccessPrograms;
import com.bederr.fragments.Fragment_Master;
import com.bederr.main.Bederr;
import com.bederr.session.Session_Manager;

import pe.bederr.com.R;

import com.bederr.benefits_v2.services.Service_Programs;
import com.bederr.benefits_v2.views.Benefit_Program_V;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Gantz on 1/08/14.
 */
public class Benefit_Programs_F extends Fragment_Master implements AdapterView.OnItemClickListener, Life_Dialog.Interface_Dialog_Life {

    private ListView listaCorp;
    private Benefit_A benefit_a;

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
        listaCorp = (ListView) getView().findViewById(R.id.lists_corp);
        listaCorp.setOnItemClickListener(this);
    }


    private void validateInitBenefits() throws Exception {
        Session_Manager session_manager = new Session_Manager(getBederr());

        /**
         * Pregunta sobre la UBICACIÓN
         */
        if (getUbication() != null) {
            String token = session_manager.getUserToken();
            String area = getUbication().getArea();

            if (session_manager.isLogin()) {
                Service_Programs service_programs = new Service_Programs(getBederr());
                service_programs.sendRequest(token, area);
                service_programs.setOnSuccessPrograms(new OnSuccessPrograms() {
                    @Override
                    public void onSuccessPrograms(boolean success,
                                                  ArrayList<Benefit_Program_DTO> program_dtos,
                                                  String count,
                                                  String next,
                                                  String previous) {
                        if (success) {
                            if (!program_dtos.isEmpty()) {
                                benefit_a = new Benefit_A(getActivity(), program_dtos, 0);
                                listaCorp.setAdapter(benefit_a);
                                getEmptyView(listaCorp);
                            } else {
                                setEmptyView();
                            }
                        } else {
                            setEmptyView();
                        }
                    }
                });
            } else {
                showMessage("Debe iniciar Sesión");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int type = (int) view.findViewById(R.id.nombreempresa).getTag();
        if (type == 0) {
            Benefit_Program_DTO program_dto = (Benefit_Program_DTO) parent.getItemAtPosition(position);
            Life_Dialog life_dialog = new Life_Dialog(getActivity(), program_dto);
            life_dialog.setActivity(getBederr());
            life_dialog.getWindow().setWindowAnimations(R.style.Dialog_Animation_UP_DOWN);
            life_dialog.setInterface_dialog_life(this);
            life_dialog.show();
        }
        showMessage(type + "");
    }

    @Override
    public void updateView(boolean state) {
        if (state) {
            getBederr().sm_empresas.toggle();
            getBederr().getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.container, Benefits_F.newInstance(), Benefits_F.class.getName()).
                    commit();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            validateInitBenefits();
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Intentelo de nuevo.");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
