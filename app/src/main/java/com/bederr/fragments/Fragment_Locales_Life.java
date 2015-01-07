package com.bederr.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.adapter.Adapter_Locales;
import com.bederr.beans.Local_DTO;
import com.bederr.main.Bederr;
import com.bederr.operations.Operation_Locales_Life;
import com.bederr.views.View_Empresa_Life;
import com.bederr.beans.Empresa_DTO;

import pe.bederr.com.R;

import com.bederr.utils.Util_Fonts;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Locales_Life extends Fragment_Master implements AdapterView.OnItemClickListener {

    private ListView lista_locales;

    public Fragment_Locales_Life() {
        setId_layout(R.layout.fragment_locales_life);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Locales_Life newInstance() {
        return new Fragment_Locales_Life();
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
        lista_locales = (ListView) getView().findViewById(R.id.lista_locales);

        View_Empresa_Life view = new View_Empresa_Life(getActivity(),getBederr().getEmpresa_dto());
        ((LinearLayout)getView().findViewById(R.id.header_list)).addView(view);

        lista_locales.setOnItemClickListener(this);
        lista_locales.setVisibility(View.GONE);

        try {
            Empresa_DTO empresa_dto = getBederr().getEmpresa_dto();
            String empresa_id = empresa_dto.getJsonObject().getString("EmpresaId");

            Operation_Locales_Life operation_locales_cercanos = new Operation_Locales_Life(getActivity());
            operation_locales_cercanos.setPage(1);
            operation_locales_cercanos.getLocalesCercanos(empresa_id);
            operation_locales_cercanos.setInterface_operation_locales_life(new Operation_Locales_Life.Interface_Operation_Locales_Life() {
                @Override
                public void getLocalesLife(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                    if(status){
                       if(local_dtos.size() > 0){
                           try {
                               Adapter_Locales adapter_locales = new Adapter_Locales(getActivity(), local_dtos,1);
                               lista_locales.setAdapter(adapter_locales);
                               lista_locales.setVisibility(View.VISIBLE);
                               getBederr().setLocal_dtos(local_dtos);
                           } catch (Exception e) {
                               e.printStackTrace();
                           }
                       }
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        ImageView action_left = (ImageView) getView().findViewById(R.id.action_left);
        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNALight(getActivity()));

        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                trans.remove(Fragment_Locales_Life.this);
                trans.commit();
                manager.popBackStack();
            }
        });

        action_middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lista_locales.getAdapter() != null){
                    if(lista_locales.getAdapter().getCount() > 0){
                        getBederr().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.abajo_arriba, R.animator.arriba_abajo).add(R.id.container, Fragment_Busqueda_Life.newInstance(), Fragment_Busqueda_Life.class.getName()).addToBackStack("a").commit();
                    }else{
                        Toast.makeText(getBederr(),"No hay datos...!",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getBederr(),"No hay datos...!",Toast.LENGTH_LONG).show();
                }
            }
        });
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
        Local_DTO local_dto = (Local_DTO) parent.getItemAtPosition(position);
        ((Bederr) getActivity()).setLocal_dto(local_dto);
        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Detalle_Local_Life.newInstance(), Fragment_Detalle_Local.class.getName()).addToBackStack(null).commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Locales_Life.this.getView().setFocusableInTouchMode(true);
        Fragment_Locales_Life.this.getView().requestFocus();
        Fragment_Locales_Life.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Fragment_Locales_Life.this);
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
