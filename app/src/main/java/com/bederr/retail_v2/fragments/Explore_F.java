package com.bederr.retail_v2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bederr.fragments.Fragment_Master;
import com.bederr.retail_v2.adapters.Places_A;
import com.bederr.adapter.Adapter_Locales;
import com.bederr.beans.Local_DTO;
import com.bederr.beans_v2.Place_DTO;
import com.bederr.main.Bederr;
import pe.bederr.com.R;

import com.bederr.operations.Operation_Locales_Cercanos;
import com.bederr.retail_v2.interfaces.OnSuccessPlaces;
import com.bederr.retail_v2.services.Service_Places;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Explore_F extends Fragment_Master implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private ListView lista_locales;
    protected Adapter_Locales adapter_locales;

    protected int page = 1;
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
        lista_locales.setOnItemClickListener(this);
        lista_locales.setOnScrollListener(this);
        lista_locales.setVisibility(View.GONE);

        Session_Manager session_manager = new Session_Manager(getBederr());

        String lat = "-12.0842643";
        String lng = "-77.0834144";
        String name = "";
        String cat = "";
        String city = "";

        if (session_manager.isLogin()) {
            Service_Places service_places = new Service_Places(getBederr());
            service_places.sendRequestUser(session_manager.getUserToken(), lat, lng, name, cat, city);
            service_places.setOnSuccessPlaces(new OnSuccessPlaces() {
                @Override
                public void onSuccessPlaces(boolean success,
                                            ArrayList<Place_DTO> place_dtos,
                                            String count,
                                            String next,
                                            String previous) {
                    try {
                        if (success) {
                            Places_A places_a = new Places_A(getBederr(), place_dtos, 0);
                            lista_locales.setAdapter(places_a);
                            Explore_F.this.onFinishLoad(lista_locales);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Service_Places service_places = new Service_Places(getBederr());
            service_places.sendRequest(lat, lng, name, cat, city);
            service_places.setOnSuccessPlaces(new OnSuccessPlaces() {
                @Override
                public void onSuccessPlaces(boolean success,
                                            ArrayList<Place_DTO> place_dtos,
                                            String count,
                                            String next,
                                            String previous) {
                    try {
                        if (success) {
                            Places_A places_a = new Places_A(getBederr(), place_dtos, 0);
                            lista_locales.setAdapter(places_a);
                            Explore_F.this.onFinishLoad(lista_locales);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
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
                add(R.id.container, Detail_Place_F.newInstance(), Detail_Place_F.class.getName()).
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
                page++;
                loadMoreDAta(page);
            }
        }
    }

    private void loadMoreDAta(int page) {
        Operation_Locales_Cercanos operation_locales_cercanos = new Operation_Locales_Cercanos(getActivity());
        operation_locales_cercanos.setPage(page);
        operation_locales_cercanos.getLocalesCercanos();
        operation_locales_cercanos.setInterface_operation_locales_cercanos(new Operation_Locales_Cercanos.Interface_Operation_Locales_Cercanos() {
            @Override
            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages) {
                try {
                    for (int i = 0; i < local_dtos.size(); i++) {
                        adapter_locales.add(local_dtos.get(i));
                    }
                    isLoading = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
