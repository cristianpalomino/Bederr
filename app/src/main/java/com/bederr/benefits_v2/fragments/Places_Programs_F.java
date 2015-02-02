package com.bederr.benefits_v2.fragments;

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

import com.bederr.beans_v2.Place_DTO;
import com.bederr.main.Bederr;
import com.bederr.benefits_v2.services.Service_Programs_Places;
import com.bederr.benefits_v2.views.Benefit_Program_V;
import com.bederr.fragments.Fragment_Busqueda_Life;
import com.bederr.fragments.Fragment_Master;
import com.bederr.retail_v2.adapters.Places_A;
import com.bederr.retail_v2.fragments.Detail_Place_F;
import com.bederr.retail_v2.interfaces.OnSuccessPlaces;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

import pe.bederr.com.R;

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Places_Programs_F extends Fragment_Master implements AdapterView.OnItemClickListener {

    private ListView lista_locales;
    private Places_A places_a;

    public Places_Programs_F() {
        setId_layout(R.layout.fragment_locales_life);
        setId_container(R.id.frame_container);
    }

    public static final Places_Programs_F newInstance() {
        return new Places_Programs_F();
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

        Benefit_Program_V benefit_program_v = new Benefit_Program_V(getActivity(),getBederr().getBenefit_program_dto());
        ((LinearLayout) getView().findViewById(R.id.header_list)).addView(benefit_program_v);

        lista_locales.setOnItemClickListener(this);
        lista_locales.setVisibility(View.GONE);


        Session_Manager session_manager = new Session_Manager(getBederr());

        String token = session_manager.getUserToken();
        String lat = "-12.0842643";
        String lng = "-77.0834144";
        String id = String.valueOf(getBederr().getBenefit_program_dto().getId());

        Service_Programs_Places service_programs_places = new Service_Programs_Places(getBederr());
        service_programs_places.sendRequestUser(token, lat, lng, id);
        service_programs_places.setOnSuccessPlaces(new OnSuccessPlaces() {
            @Override
            public void onSuccessPlaces(boolean success,
                                        ArrayList<Place_DTO> place_dtos,
                                        String count,
                                        String next,
                                        String previous) {
                try {
                    if (success) {
                        getBederr().setPlace_dtos(place_dtos);
                        places_a = new Places_A(getBederr(),place_dtos,0,"Benefits");
                        lista_locales.setAdapter(places_a);
                        lista_locales.setVisibility(View.VISIBLE);
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
        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNALight(getActivity()));

        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                trans.remove(Places_Programs_F.this);
                trans.commit();
                manager.popBackStack();
            }
        });

        action_middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lista_locales.getAdapter() != null) {
                    if (lista_locales.getAdapter().getCount() > 0) {
                        getBederr().getSupportFragmentManager().
                                beginTransaction().
                                setCustomAnimations(R.animator.abajo_arriba, R.animator.arriba_abajo).
                                add
                                        (
                                                R.id.container, Benefits_Search_F.newInstance(),
                                                Benefits_Search_F.class.getName()
                                        )
                                .addToBackStack("a").commit();
                    } else {
                        Toast.makeText(getBederr(), "No hay locales...!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBederr(), "No hay locales...!", Toast.LENGTH_LONG).show();
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
        Place_DTO place_dto = (Place_DTO) parent.getItemAtPosition(position);
        ((Bederr) getActivity()).setPlace_dto(place_dto);
        getActivity().getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).
                add(R.id.container, Detail_Place_F.newInstance("Benefits"), Detail_Place_F.class.getName()).
                addToBackStack(null).commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Places_Programs_F.this.getView().setFocusableInTouchMode(true);
        Places_Programs_F.this.getView().requestFocus();
        Places_Programs_F.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Places_Programs_F.this);
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
