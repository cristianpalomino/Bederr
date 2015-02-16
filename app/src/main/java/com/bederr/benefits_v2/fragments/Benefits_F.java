package com.bederr.benefits_v2.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.beans_v2.Benefit_Program_DTO;
import com.bederr.benefits_v2.adapter.Benefit_A;
import com.bederr.benefits_v2.dialog.Life_Dialog;
import com.bederr.benefits_v2.interfaces.OnSuccessDelete;
import com.bederr.benefits_v2.views.Benefit_Program_Corp_V;
import com.bederr.dialog.Dialog_Empresa;
import com.bederr.fragments.Fragment_Master;

import pe.bederr.com.R;

import com.bederr.benefits_v2.interfaces.OnSuccessPrograms;
import com.bederr.benefits_v2.services.Service_Programs;
import com.bederr.main.Bederr;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 31/07/14.
 */
public class Benefits_F extends Fragment_Master implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, Dialog_Empresa.Interface_Dialog_Empresa {

    private ListView listaCorp;
    private Benefit_A benefit_a;
    private Benefit_Program_DTO program_dto;

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

        listaCorp = (ListView) getView().findViewById(R.id.lists_corp);
        listaCorp.setOnItemClickListener(this);
        listaCorp.setOnItemLongClickListener(this);
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

    private void validateInitBenefits() throws Exception {
        Session_Manager session_manager = new Session_Manager(getBederr());

        /**
         * Pregunta sobre la UBICACIÓN
         */
        if (getUbication() != null) {
            String token = session_manager.getUserToken();

            if (session_manager.isLogin()) {
                Service_Programs service_programs = new Service_Programs(getBederr());
                service_programs.sendRequestMe(token);
                service_programs.setOnSuccessPrograms(new OnSuccessPrograms() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccessPrograms(boolean success,
                                                  ArrayList<Benefit_Program_DTO> program_dtos,
                                                  String count,
                                                  String next,
                                                  String previous) {
                        if (success) {
                            if (!program_dtos.isEmpty()) {
                                benefit_a = new Benefit_A(getActivity(), program_dtos, 1);
                                listaCorp.setAdapter(benefit_a);
                                getEmptyView().setVisibility(View.GONE);
                            } else {
                                setEmptyView(listaCorp);
                            }
                        } else {
                            setEmptyView(listaCorp);
                        }
                    }
                });
            } else {
                showMessage("Debe iniciar Sesión");
            }
        }
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

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int type = (int) view.findViewById(R.id.nombreempresa).getTag();
        program_dto = (Benefit_Program_DTO) parent.getItemAtPosition(position);
        if (type == 1) {
            getBederr().setBenefit_program_dto(program_dto);
            getBederr().
                    getSupportFragmentManager().
                    beginTransaction().
                    setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).
                    add(R.id.container, Places_Programs_F.newInstance(), Places_Programs_F.class.getName()).
                    addToBackStack(null).commit();
        }
    }

    /**
     * Callback method to be invoked when an item in this view has been
     * clicked and held.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need to access
     * the data associated with the selected item.
     *
     * @param parent   The AbsListView where the click happened
     * @param view     The view within the AbsListView that was clicked
     * @param position The position of the view in the list
     * @param id       The row id of the item that was clicked
     * @return true if the callback consumed the long click, false otherwise
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int type = (int) view.findViewById(R.id.nombreempresa).getTag();
        if (type == 1) {
            Dialog_Empresa dialog_empresa = new Dialog_Empresa(getBederr());
            dialog_empresa.getWindow().setWindowAnimations(R.style.Dialog_Animation_DOWN_UP);
            dialog_empresa.setInterface_dialog_empresa(Benefits_F.this);
            dialog_empresa.show();
        }
        return true;
    }

    @Override
    public void onAcepted(final Dialog_Empresa dialog_cupon) {
        String token = new Session_Manager(getBederr()).getUserToken();
        Service_Programs service_programs = new Service_Programs(getBederr());
        service_programs.sendRequestDelete(token, String.valueOf(program_dto.getId()));
        service_programs.setOnSuccessDelete(new OnSuccessDelete() {
            @Override
            public void onSuccessDelete(boolean success, String message) {
                dialog_cupon.hide();
                showMessage(message);
                updateView(success);
            }
        });
    }

    public void updateView(boolean state) {
        if (state) {
            getBederr().getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.container, Benefits_F.newInstance(), Benefits_F.class.getName()).
                    commit();
        }
    }
}