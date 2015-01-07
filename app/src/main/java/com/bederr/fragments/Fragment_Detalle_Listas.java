package com.bederr.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bederr.main.Bederr;
import com.bederr.adapter.Adapter_Locales;
import com.bederr.beans.Local_DTO;

import pe.bederr.com.R;
import com.bederr.operations.Operation_Listas;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Detalle_Listas extends Fragment_Master implements AdapterView.OnItemClickListener {

    protected ListView lista_locales;

    public Fragment_Detalle_Listas() {
        setId_layout(R.layout.fragment_detalle_lista);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Detalle_Listas newInstance(){
        return new Fragment_Detalle_Listas();
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
        Fragment_Detalle_Listas.this.onFinishLoad(getView());


        LinearLayout header_lista = (LinearLayout)getView().findViewById(R.id.container_header);
        //header_lista.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.abajo_arriba_b));
        //header_lista.addView(getBederr().getLista_dto().getView_lista());

        lista_locales = (ListView) getView().findViewById(R.id.lista_locales);

        View view = getBederr().getLista_dto().getView_lista();
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.abajo_arriba_b));
        view.setClickable(false);

        lista_locales.addHeaderView(view);
        lista_locales.setOnItemClickListener(this);

        Operation_Listas operation_listas = new Operation_Listas(getActivity());
        operation_listas.getListas(getBederr().getLista_dto().getId_lista());
        operation_listas.setInterface_operation_locales_por_lista(new Operation_Listas.Interface_Operation_Locales_Por_Lista() {
            @Override
            public void getLocalesPorLista(boolean status, ArrayList<Local_DTO> local_dtos) {
                Adapter_Locales adapter_locales = new Adapter_Locales(getActivity(), local_dtos,0);
                lista_locales.setAdapter(adapter_locales);
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
                trans.remove(Fragment_Detalle_Listas.this);
                trans.commit();
                manager.popBackStack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Detalle_Listas.this.getView().setFocusableInTouchMode(true);
        Fragment_Detalle_Listas.this.getView().requestFocus();
        Fragment_Detalle_Listas.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Fragment_Detalle_Listas.this);
                        trans.commit();
                        manager.popBackStack();
                        return true;
                    }
                }
                return false;
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
        ((Bederr)getActivity()).setLocal_dto(local_dto);
        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b,R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Detalle_Local.newInstance(),Fragment_Detalle_Local.class.getName()).addToBackStack(null).commit();
    }
}
