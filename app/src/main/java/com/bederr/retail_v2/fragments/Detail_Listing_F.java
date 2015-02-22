package com.bederr.retail_v2.fragments;

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
import android.widget.ListView;
import android.widget.TextView;

import com.bederr.beans_v2.Listing_DTO;
import com.bederr.main.Bederr;
import com.bederr.fragments.Fragment_Master;
import com.bederr.retail_v2.adapters.Places_A;
import com.bederr.beans_v2.Place_DTO;

import pe.bederr.com.R;

import com.bederr.retail_v2.interfaces.OnSuccessListings;
import com.bederr.retail_v2.interfaces.OnSuccessPlaces;
import com.bederr.retail_v2.services.Service_Listings;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Detail_Listing_F extends Fragment_Master implements AdapterView.OnItemClickListener, Detail_Place_F.OnBack {

    protected ListView lista_locales;
    private OnBack onBack;

    public Detail_Listing_F() {
        setId_layout(R.layout.fragment_detalle_lista);
        setId_container(R.id.frame_container);
    }

    public static final Detail_Listing_F newInstance() {
        return new Detail_Listing_F();
    }

    public void setOnBack(OnBack onBack) {
        this.onBack = onBack;
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
        View view = getBederr().getViewExtra();
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.abajo_arriba_b));
        view.setClickable(false);
        view.setEnabled(false);
        view.setOnClickListener(null);

        lista_locales = (ListView) getView().findViewById(R.id.lista_locales);
        lista_locales.addHeaderView(view);
        lista_locales.setOnItemClickListener(this);

        Service_Listings service_listings = new Service_Listings(getBederr());
        service_listings.sendRequestListhing(String.valueOf(getBederr().getListing_dto().getId()));
        service_listings.setOnSuccessPlaces(new OnSuccessPlaces() {
            @Override
            public void onSuccessPlaces(boolean success,
                                        ArrayList<Place_DTO> place_dtos,
                                        String count,
                                        String next,
                                        String previous) {
                try {

                    if (success) {
                        if (!place_dtos.isEmpty()) {
                            Places_A places_a = new Places_A(getBederr(), place_dtos, 1, "Explore");
                            lista_locales.setAdapter(places_a);
                            getEmptyView(lista_locales);
                        } else {
                            setEmptyView();
                        }
                    } else {
                        setEmptyView();
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
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));

        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                trans.remove(Detail_Listing_F.this);
                trans.commit();
                manager.popBackStack();
                onBack.onBack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Detail_Listing_F.this.getView().setFocusableInTouchMode(true);
        Detail_Listing_F.this.getView().requestFocus();
        Detail_Listing_F.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Detail_Listing_F.this);
                        trans.commit();
                        manager.popBackStack();
                        onBack.onBack();
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

        lista_locales.setOnItemClickListener(null);
        lista_locales.setClickable(false);

        Detail_Place_F detail_place_f = Detail_Place_F.newInstance("Explore");
        getActivity().getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).
                add(R.id.container, detail_place_f, Detail_Place_F.class.getName()).
                addToBackStack(null).commit();
        detail_place_f.setOnBack(Detail_Listing_F.this);
    }


    @Override
    public void onBack() {
        lista_locales.setOnItemClickListener(this);
        lista_locales.setClickable(true);
    }

    public interface OnBack {
        public void onBack();
    }
}
