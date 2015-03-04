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

import com.bederr.beans.Lista_DTO;
import com.bederr.main.Bederr;
import com.bederr.fragments.Fragment_Master;
import com.bederr.adapter.Adapter_Listas;
import com.bederr.beans_v2.Listing_DTO;

import pe.bederr.com.R;

import com.bederr.operations.Operation_Listas;
import com.bederr.questions_v2.adapters.Answer_A;
import com.bederr.retail_v2.adapters.Listing_A;
import com.bederr.retail_v2.interfaces.OnSuccessListings;
import com.bederr.retail_v2.services.Service_Listings;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Listing_F extends Fragment_Master implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, Detail_Listing_F.OnBack {

    protected ListView lista_listas;
    protected Listing_A listing_a;
    protected boolean isLoading = false;

    public Listing_F() {
        setId_layout(R.layout.fragment_listas);
        setId_container(R.id.frame_container);
    }

    public static final Listing_F newInstance() {
        return new Listing_F();
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

        lista_listas = (ListView) getView().findViewById(R.id.lista_listas);
        lista_listas.setOnItemClickListener(this);
        lista_listas.setOnScrollListener(this);

        try {
            if (getUbication() != null) {
                Session_Manager session_manager = new Session_Manager(getBederr());
                String area = getUbication().getArea();
                if (session_manager.isLogin()) {
                    Service_Listings service_listings = new Service_Listings(getBederr());
                    service_listings.sendRequestUser(session_manager.getUserToken(), area);
                    service_listings.setOnSuccessListings(new OnSuccessListings() {
                        @Override
                        public void onSuccessListings(boolean success,
                                                      ArrayList<Listing_DTO> listing_dtos,
                                                      String count,
                                                      String next,
                                                      String previous) {
                            try {
                                if (success) {
                                    if (!listing_dtos.isEmpty()) {
                                        listing_a = new Listing_A(getBederr(), listing_dtos);
                                        lista_listas.setAdapter(listing_a);
                                        getEmptyView(lista_listas);

                                        /**
                                         * Stacks
                                         */
                                        PREVIOUS = previous;
                                        NEXT = next;
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
                } else {
                    Service_Listings service_listings = new Service_Listings(getBederr());
                    service_listings.sendRequest(area);
                    service_listings.setOnSuccessListings(new OnSuccessListings() {
                        @Override
                        public void onSuccessListings(boolean success,
                                                      ArrayList<Listing_DTO> listing_dtos,
                                                      String count,
                                                      String next,
                                                      String previous) {
                            try {
                                if (success) {
                                    if (!listing_dtos.isEmpty()) {
                                        listing_a = new Listing_A(getBederr(), listing_dtos);
                                        lista_listas.setAdapter(listing_a);
                                        getEmptyView(lista_listas);

                                        /**
                                         * Stacks
                                         */
                                        PREVIOUS = previous;
                                        NEXT = next;
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
            }
        } catch (Exception e) {
            showMessage("No se ah especificado ninguna area");
        }
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        ImageView action_left = (ImageView) getView().findViewById(R.id.action_left);
        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Bederr) getActivity()).sm_menu.toggle();
            }
        });

        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
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
        Listing_DTO listing_dto = (Listing_DTO) parent.getItemAtPosition(position);
        final View mview = lista_listas.getAdapter().getView(position, null, lista_listas);

        lista_listas.setOnItemClickListener(null);
        lista_listas.setClickable(false);

        Detail_Listing_F detail_listing_f = Detail_Listing_F.newInstance();
        detail_listing_f.setOnBack(this);

        getBederr().setViewExtra(mview);
        getBederr().setListing_dto(listing_dto);
        getBederr().getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).
                add(R.id.container, detail_listing_f , Detail_Listing_F.class.getName()).
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

                Service_Listings service_listings = new Service_Listings(getBederr());
                service_listings.loadMore(NEXT);
                service_listings.setOnSuccessListings(new OnSuccessListings() {
                    @Override
                    public void onSuccessListings(boolean success,
                                                  ArrayList<Listing_DTO> listing_dtos,
                                                  String count,
                                                  String next,
                                                  String previous) {
                        try {
                            if (success) {
                                for (int i = 0; i < listing_dtos.size(); i++) {
                                    listing_a.add(listing_dtos.get(i));
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
    public void onBack() {
        lista_listas.setOnItemClickListener(this);
        lista_listas.setClickable(true);
    }
}