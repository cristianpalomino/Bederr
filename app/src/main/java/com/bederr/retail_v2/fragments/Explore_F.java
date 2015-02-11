package com.bederr.retail_v2.fragments;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bederr.application.Maven_Application;
import com.bederr.beans_v2.Question_DTO;
import com.bederr.beans_v2.Ubication_DTO;
import com.bederr.fragments.Fragment_Master;
import com.bederr.main.Master;
import com.bederr.questions_v2.interfaces.OnSuccessQuestion;
import com.bederr.questions_v2.services.Service_Question;
import com.bederr.retail_v2.adapters.Places_A;
import com.bederr.adapter.Adapter_Locales;
import com.bederr.beans.Local_DTO;
import com.bederr.beans_v2.Place_DTO;
import com.bederr.main.Bederr;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import pe.bederr.com.R;

import com.bederr.operations.Operation_Locales_Cercanos;
import com.bederr.retail_v2.dialog.Ubication_D;
import com.bederr.retail_v2.interfaces.OnSuccessPlaces;
import com.bederr.retail_v2.services.Service_Places;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Explore_F extends Fragment_Master implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, Bederr.BederrOnSuccessArea {

    private ListView lista_locales;
    protected Places_A places_a;
    private boolean isLoading = false;

    public Explore_F() {
        setId_layout(R.layout.fragment_explorar_v2);
        setId_container(R.id.frame_container);
    }

    public static final Explore_F newInstance() {
        return new Explore_F();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getBederr().setBederrOnSuccessArea(this);
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
        closeKeyboard();

        lista_locales = (ListView) getView().findViewById(R.id.lista_locales);
        lista_locales.setOnItemClickListener(this);
        lista_locales.setOnScrollListener(this);
        lista_locales.setVisibility(View.GONE);

        try {
            validateInitPlaces();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openLoginUser(String token, String lat, String lng, String name, String cat, String city, String area) {
        Service_Places service_places = new Service_Places(getBederr());
        service_places.sendRequestUser(token, lat, lng, name, cat, city, area);
        service_places.setOnSuccessPlaces(new OnSuccessPlaces() {
            @Override
            public void onSuccessPlaces(boolean success,
                                        ArrayList<Place_DTO> place_dtos,
                                        String count,
                                        String next,
                                        String previous) {
                try {
                    if (success) {
                        if (place_dtos.size() > 0) {
                            places_a = new Places_A(getBederr(), place_dtos, 0, "Explore");
                            lista_locales.setAdapter(places_a);
                            Explore_F.this.onFinishLoad(lista_locales);

                            NEXT = next;
                            PREVIOUS = previous;
                        } else {
                            //getEmptyView().setVisibility(View.VISIBLE);
                            lista_locales.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //getEmptyView().setVisibility(View.VISIBLE);
                    lista_locales.setVisibility(View.GONE);
                }
            }
        });
    }

    private void openLogin(String lat, String lng, String name, String cat, String city, String area) {
        Service_Places service_places = new Service_Places(getBederr());
        service_places.sendRequest(lat, lng, name, cat, city, area);
        service_places.setOnSuccessPlaces(new OnSuccessPlaces() {
            @Override
            public void onSuccessPlaces(boolean success,
                                        ArrayList<Place_DTO> place_dtos,
                                        String count,
                                        String next,
                                        String previous) {
                try {
                    if (success) {
                        if (place_dtos.size() > 0) {
                            places_a = new Places_A(getBederr(), place_dtos, 0, "Explore");
                            lista_locales.setAdapter(places_a);
                            Explore_F.this.onFinishLoad(lista_locales);

                            NEXT = next;
                            PREVIOUS = previous;
                        } else {
                            //getEmptyView().setVisibility(View.VISIBLE);
                            lista_locales.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //getEmptyView().setVisibility(View.VISIBLE);
                    lista_locales.setVisibility(View.GONE);
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
                add(R.id.container, Detail_Place_F.newInstance("Explore"), Detail_Place_F.class.getName()).
                addToBackStack(null).commit();
    }

    /**
     * Callback method to be invoked while the list view or grid view is being scrolled. If the
     * view is being scrolled, this method will be called before the next frame of the scroll is
     * rendered. In particular, it will be called before any calls to
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Explore_F.this.getView().setFocusableInTouchMode(true);
        Explore_F.this.getView().requestFocus();
        Explore_F.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getBederr().clearHistory();
                        getBederr().finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void bederrOnSuccessArea(boolean success, Ubication_DTO ubication_dto) {
        try {
            validateInitPlaces();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateInitPlaces() {
        boolean isLatLng = !getUbication().getLatitude().equals("0.0") && !getUbication().getLongitude().equals("0.0");
        Session_Manager session_manager = new Session_Manager(getBederr());

        /**
         * Pregunta sobre la UBICACIÓN
         */
        if (getUbication() != null) {
            String token = session_manager.getUserToken();
            String lat = getUbication().getLatitude();
            String lng = getUbication().getLongitude();
            String name = "";
            String cat = "";
            String city = "";
            String area = getUbication().getArea();

            if (session_manager.isLogin()) {
                /**
                 * Si tiene LAT y LONG
                 */
                if (isLatLng) {
                    openLoginUser(token, lat, lng, name, cat, city, "");
                }
                /**
                 * Si tiene AREA
                 */
                else {
                    openLoginUser(token, "", "", name, cat, city, area);
                }
            } else {
                /**
                 * Si tiene LAT y LONG
                 */
                if (isLatLng) {
                    openLogin(lat, lng, name, cat, city, "");
                }
                /**
                 * Si tiene AREA
                 */
                else {
                    openLogin("", "", name, cat, city, area);
                }
            }
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
                ((Bederr) getActivity()).sm_menu.toggle();
            }
        });

        action_middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBederr().getSupportFragmentManager().beginTransaction().
                        setCustomAnimations(R.animator.abajo_arriba, R.animator.arriba_abajo).
                        add(R.id.container, Search_F.newInstance(), Search_F.class.getName()).
                        addToBackStack("busquedas").commit();
            }
        });
    }

    /*
    private View getEmptyView(){
        View view = getView().findViewById(R.id.empty_view);
        TextView message = (TextView) view.findViewById(R.id.text_type_message_no_data);
        message.setTypeface(Util_Fonts.setPNASemiBold(getBederr()));
        return view;
    }
    */

}
