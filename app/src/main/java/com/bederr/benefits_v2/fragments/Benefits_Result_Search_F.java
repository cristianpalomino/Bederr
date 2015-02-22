package com.bederr.benefits_v2.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bederr.beans.Categoria_DTO;
import com.bederr.beans_v2.Category_DTO;
import com.bederr.beans_v2.Locality_DTO;
import com.bederr.main.Bederr;
import com.bederr.fragments.Fragment_Master;
import com.bederr.retail_v2.adapters.Places_A;
import com.bederr.beans_v2.Place_DTO;

import pe.bederr.com.R;

import com.bederr.retail_v2.fragments.Detail_Place_F;
import com.bederr.retail_v2.interfaces.OnSuccessPlaces;
import com.bederr.retail_v2.services.Service_Places;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 5/07/14.
 */
public class Benefits_Result_Search_F extends Fragment_Master implements AdapterView.OnItemClickListener, Detail_Place_F.OnBack {

    private String s;
    private String distrito;
    private boolean isLoading = false;
    protected ListView lista_locales_busquedas;
    private Places_A places_a;

    public Benefits_Result_Search_F() {
        setId_layout(R.layout.fragment_resultado_busquedas);
        setId_container(R.id.frame_container);
    }

    public static final Benefits_Result_Search_F newInstance(String s, String distrito) {
        Benefits_Result_Search_F result_search = new Benefits_Result_Search_F();
        Bundle bundle = new Bundle();
        bundle.putString("s", s);
        bundle.putString("distrito", distrito);
        result_search.setArguments(bundle);
        return result_search;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        s = getArguments().getString("s");
        distrito = getArguments().getString("distrito");
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

        TextView txt_criterio = (TextView) getView().findViewById(R.id.txtcriterio);
        txt_criterio.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));

        if (s.equals("") || distrito.equals("")) {
            txt_criterio.setText(s + distrito);
        } else {
            txt_criterio.setText(s + " - " + distrito);
        }

        txt_criterio.setVisibility(View.VISIBLE);
        txt_criterio.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.abajo_arriba_b));

        lista_locales_busquedas = (ListView) getView().findViewById(R.id.lista_locales_busquedas);
        lista_locales_busquedas.setOnItemClickListener(this);
        lista_locales_busquedas.setVisibility(View.GONE);

        String name = "";
        String cat = "";

        if (validateCategory()) {
            cat = getCategory(s).getCantidadcategoria();
        } else {
            name = s;
        }

        String locality = validateLocality();

        ArrayList<Place_DTO> places = getBederr().getPlace_dtos();
        ArrayList<Place_DTO> mplaces = new ArrayList<Place_DTO>();

        for (int i = 0; i < places.size(); i++) {
            Place_DTO place_dto = places.get(i);
            String mname = place_dto.getName();
            String mcat = place_dto.getCategory_code();
            String mdis = place_dto.getCity();

            if (!cat.matches("") && !locality.matches("")) {
                if (!cat.matches("")) {
                    if (cat.equals(mcat) && locality.equals(mdis)) {
                        mplaces.add(place_dto);
                    } else {
                        lista_locales_busquedas.setVisibility(View.GONE);
                    }
                } else {
                    if (name.equals(mname) && locality.equals(mdis)) {
                        mplaces.add(place_dto);
                    } else {
                        lista_locales_busquedas.setVisibility(View.GONE);
                    }
                }
            } else {
                if (!cat.matches("")) {
                    if (mcat.equals(cat)) {
                        mplaces.add(place_dto);
                    } else {
                        lista_locales_busquedas.setVisibility(View.GONE);
                    }
                } else {
                    lista_locales_busquedas.setVisibility(View.GONE);
                }

                if (!name.matches("")) {
                    if (name.equals(mname)) {
                        mplaces.add(place_dto);
                    } else {
                        lista_locales_busquedas.setVisibility(View.GONE);
                    }
                } else {
                    lista_locales_busquedas.setVisibility(View.GONE);
                }

                if (!locality.matches("")) {
                    if (locality.equals(mdis)) {
                        mplaces.add(place_dto);
                    } else {
                        lista_locales_busquedas.setVisibility(View.GONE);
                    }
                } else {
                    lista_locales_busquedas.setVisibility(View.GONE);
                }
            }
        }

        places_a = new Places_A(getBederr(), mplaces, 0, "Benefits");
        lista_locales_busquedas.setAdapter(places_a);
        getEmptyView(lista_locales_busquedas);

        if (lista_locales_busquedas.getAdapter().getCount() == 0) {
            lista_locales_busquedas.setVisibility(View.GONE);
        }

    }

    private boolean validateCategory() {
        if (getBederr().getCategoria_dtos() != null) {
            for (int i = 0; i < getBederr().getCategoria_dtos().size(); i++) {
                Categoria_DTO category_dto = getBederr().getCategoria_dtos().get(i);
                if (s.equals(category_dto.getNombrecategoria())) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private Categoria_DTO getCategory(String name) {
        for (int i = 0; i < getBederr().getCategoria_dtos().size(); i++) {
            if (name.equals(getBederr().getCategoria_dtos().get(i).getNombrecategoria())) {
                return getBederr().getCategoria_dtos().get(i);
            }
        }
        return null;
    }

    private String validateLocality() {
        if (getBederr().getLocality_dtos() != null) {
            for (int i = 0; i < getBederr().getLocality_dtos().size(); i++) {
                Locality_DTO locality_dto = getBederr().getLocality_dtos().get(i);
                if (distrito.equals(locality_dto.getName())) {
                    return String.valueOf(locality_dto.getName());
                }
            }
        }
        return "";
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
                trans.remove(Benefits_Result_Search_F.this);
                trans.commit();
                manager.popBackStack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Benefits_Result_Search_F.this.getView().setFocusableInTouchMode(true);
        Benefits_Result_Search_F.this.getView().requestFocus();
        Benefits_Result_Search_F.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Benefits_Result_Search_F.this);
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
        Place_DTO place_dto = (Place_DTO) parent.getItemAtPosition(position);
        ((Bederr) getActivity()).setPlace_dto(place_dto);

        lista_locales_busquedas.setOnItemClickListener(null);
        lista_locales_busquedas.setClickable(false);

        Detail_Place_F detail_place_f = Detail_Place_F.newInstance("Benefits");
        getActivity().getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).
                add(R.id.container, detail_place_f, Detail_Place_F.class.getName()).
                addToBackStack(null).commit();
        detail_place_f.setOnBack(Benefits_Result_Search_F.this);
    }

    @Override
    public void onBack() {
        lista_locales_busquedas.setOnItemClickListener(this);
        lista_locales_busquedas.setClickable(true);
    }
}