package com.bederr.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bederr.views.View_Empresa;
import com.bederr.beans.Empresa_DTO;
import pe.bederr.com.R;
import com.bederr.operations.Operation_Empresas;

import java.util.ArrayList;

/**
 * Created by Gantz on 1/08/14.
 */
public class Fragment_Empresa extends Fragment_Master {

    public Fragment_Empresa() {
        setId_layout(R.layout.fragment_empresas);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Empresa newInstance(){
        return new Fragment_Empresa();
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

        Operation_Empresas operation_empresas = new Operation_Empresas(getActivity());
        operation_empresas.getEmpresasLife();
        operation_empresas.setInterface_operation_empresas(new Operation_Empresas.Interface_Operation_Empresas() {
            @Override
            public void getEmpresasLife(boolean status, ArrayList<Empresa_DTO> empresa_dtos, String mensaje) {
                if(status){
                    for (int i = 0; i < empresa_dtos.size(); i++) {
                        View_Empresa view_empresa = new View_Empresa(getActivity(),empresa_dtos.get(i));
                        linearLayout.addView(view_empresa);
                        Fragment_Empresa.this.onFinishLoad(linearLayout);
                    }
                }else{
                    Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
