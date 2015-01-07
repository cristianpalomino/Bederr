package com.bederr.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bederr.beans.Empresa_DTO;

import pe.bederr.com.R;
import com.bederr.operations.Operation_Empresas;
import com.bederr.utils.Util_Fonts;
import com.bederr.views.View_Empresa_Life;

import java.util.ArrayList;

/**
 * Created by Gantz on 31/07/14.
 */
public class Fragment_Beneficios extends Fragment_Master {


    public Fragment_Beneficios() {
        setId_layout(R.layout.fragment_beneficios);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Beneficios newInstance() {
        return new Fragment_Beneficios();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        getBederr().setActive_fragment(Fragment_Preguntas.newInstance());
        return super.onCreateView(inflater, viewGroup, bundle);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        final LinearLayout frameempty = (LinearLayout) getView().findViewById(R.id.frameempy);
        final LinearLayout frameempresas = (LinearLayout) getView().findViewById(R.id.frame_container);
        final LinearLayout framegeneral = (LinearLayout) getView().findViewById(R.id.frameempresageneral);

        ((TextView)getView().findViewById(R.id.txtmensajecorporativo)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));

        Operation_Empresas operation_empresas = new Operation_Empresas(getActivity());
        operation_empresas.getMisEmpresasLife();
        operation_empresas.setInterface_operation_mis_empresas(new Operation_Empresas.Interface_Operation_Mis_Empresas() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void getMisEmpresasLife(boolean status, ArrayList<Empresa_DTO> empresa_dtos, String mensaje) {
                try {
                    if (status) {
                        frameempty.setVisibility(View.GONE);
                        frameempresas.setVisibility(View.VISIBLE);
                        framegeneral.setBackground(getActivity().getResources().getDrawable(R.drawable.fondo_maven));

                        for (int i = 0; i < empresa_dtos.size(); i++) {
                            View_Empresa_Life view_empresa = new View_Empresa_Life(getActivity(), empresa_dtos.get(i));
                            view_empresa.isClickable(true);
                            frameempresas.addView(view_empresa);
                        }

                        Fragment_Beneficios.this.onFinishLoad(frameempresas);

                    } else {
                        frameempty.setVisibility(View.VISIBLE);
                        frameempresas.setVisibility(View.GONE);

                        framegeneral.setBackgroundColor(getActivity().getResources().getColor(R.color.verde_maven));
                    }
                }catch (Exception e){
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
                getBederr().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,Fragment_Empresa.newInstance()).commit();
            }
        });

        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
    }
}