package com.bederr.retail_v2.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.bederr.beans_v2.Category_DTO;
import com.bederr.beans_v2.Locality_DTO;
import com.bederr.main.Bederr;
import com.bederr.fragments.Fragment_Master;
import com.bederr.retail_v2.adapters.Places_A;
import com.bederr.beans_v2.Place_DTO;

import pe.bederr.com.R;

import com.bederr.retail_v2.interfaces.OnSuccessPlaces;
import com.bederr.retail_v2.services.Service_Places;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 5/07/14.
 */
public class Result_Search_F extends Fragment_Master implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private String s;
    private String distrito;
    private boolean isLoading = false;
    protected ListView lista_locales_busquedas;
    private Places_A places_a;

    public Result_Search_F() {
        setId_layout(R.layout.fragment_resultado_busquedas);
        setId_container(R.id.frame_container);
    }

    public static final Result_Search_F newInstance(String s, String distrito) {
        Result_Search_F result_search = new Result_Search_F();
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
        hideSoftKeyboard();
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
        lista_locales_busquedas.setOnScrollListener(this);
        lista_locales_busquedas.setVisibility(View.GONE);

        Session_Manager session_manager = new Session_Manager(getBederr());
        String token = session_manager.getUserToken();
        String name = "";
        String cat = "";

        if(validateCategory()){
            cat = getCategory(s).getId();
        }else{
            name = s;
        }

        String locality = validateLocality();
        String lng;
        String lat;
        if(locality.equals("-1")){
            locality = "";
            lng = getUbication().getLongitude();
            lat = getUbication().getLatitude();
        }else{
            lng = "";
            lat = "";
        }
        String area = getUbication().getArea();

        if(session_manager.isLogin()){
            Service_Places service_places = new Service_Places(getBederr());
            service_places.sendRequestUser(token,lat,lng,name,cat,locality,area);
            service_places.setOnSuccessPlaces(new OnSuccessPlaces() {
                @Override
                public void onSuccessPlaces(boolean success,
                                            ArrayList<Place_DTO> place_dtos,
                                            String count,
                                            String next,
                                            String previous) {
                    try {
                        if(success){
                            places_a = new Places_A(getActivity(), place_dtos , 0 , "Explore");
                            lista_locales_busquedas.setAdapter(places_a);
                            Result_Search_F.this.onFinishLoad(lista_locales_busquedas);
                        }

                        /**
                         * Stacks
                         */
                        NEXT = next;
                        PREVIOUS = previous;
                        isLoading = false;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }else{
            Service_Places service_places = new Service_Places(getBederr());
            service_places.sendRequest(lat,lng,name,cat,locality,area);
            service_places.setOnSuccessPlaces(new OnSuccessPlaces() {
                @Override
                public void onSuccessPlaces(boolean success,
                                            ArrayList<Place_DTO> place_dtos,
                                            String count,
                                            String next,
                                            String previous) {
                    try {
                        if(success){
                            places_a = new Places_A(getBederr(), place_dtos, 0 , "Explore");
                            lista_locales_busquedas.setAdapter(places_a);
                            Result_Search_F.this.onFinishLoad(lista_locales_busquedas);
                        }

                        /**
                         * Stacks
                         */
                        NEXT = next;
                        PREVIOUS = previous;
                        isLoading = false;

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private boolean validateCategory(){
        if(getBederr().getCategory_dtos() != null){
            for (int i = 0; i < getBederr().getCategory_dtos().size(); i++) {
                Category_DTO category_dto = getBederr().getCategory_dtos().get(i);
                if(s.equals(category_dto.getName())){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }
    
    private Category_DTO getCategory(String name){
        for (int i = 0; i < getBederr().getCategory_dtos().size() ; i++) {
            if(name.equals(getBederr().getCategory_dtos().get(i).getName())){
                return getBederr().getCategory_dtos().get(i);
            }
        }
        return null;
    }

    private String validateLocality(){
        if(getBederr().getLocality_dtos() != null){
            for (int i = 0; i < getBederr().getLocality_dtos().size() ; i++) {
                Locality_DTO locality_dto = getBederr().getLocality_dtos().get(i);
                if(distrito.equals(locality_dto.getName())){
                    return String.valueOf(locality_dto.getId());
                }
            }
        }
        return "-1";
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
                trans.remove(Result_Search_F.this);
                trans.commit();
                manager.popBackStack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Result_Search_F.this.getView().setFocusableInTouchMode(true);
        Result_Search_F.this.getView().requestFocus();
        Result_Search_F.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Result_Search_F.this);
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
        getActivity().getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).
                add(R.id.container, Detail_Place_F.newInstance("Explore"), Detail_Place_F.class.getName()).
                addToBackStack(null).commit();
    }

    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Callback method to be invoked while the list view or grid view is being scrolled. If the
     * view is being scrolled, this method will be called before the next frame of the scroll is
     * rendered. In particular, it will be called before any calls to
     * {@link Adapter#getView(int, android.view.View, android.view.ViewGroup)}.
     *
     * @param view        The view whose scroll state is being reported
     * @param scrollState The current scroll state. One of
     *                    {@link #SCROLL_STATE_TOUCH_SCROLL} or {@link #SCROLL_STATE_IDLE}.
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /**
     * Callback method to be invoked when the list or grid has been scrolled. This will be
     * called after the scroll has completed
     *
     * @param view             The view whose scroll state is being reported
     * @param firstVisibleItem the index of the first visible cell (ignore if
     *                         visibleItemCount == 0)
     * @param visibleItemCount the number of visible cells
     * @param totalItemCount   the number of items in the list adaptor
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
            if (!isLoading) {
                isLoading = true;
                Service_Places service_places = new Service_Places(getBederr());
                service_places.loadMore(NEXT);
                service_places.setOnSuccessPlaces(new OnSuccessPlaces() {
                    @Override
                    public void onSuccessPlaces(boolean success,
                                                ArrayList<Place_DTO> place_dtos,
                                                String count,
                                                String next,
                                                String previous) {
                        try {
                            if (success) {
                                for (int i = 0; i < place_dtos.size(); i++) {
                                    places_a.add(place_dtos.get(i));
                                }

                                /**
                                 * Stacks
                                 */
                                NEXT = next;
                                PREVIOUS = previous;
                                isLoading = false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}