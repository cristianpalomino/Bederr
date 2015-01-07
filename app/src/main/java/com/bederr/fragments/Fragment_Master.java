package com.bederr.fragments;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bederr.main.Bederr;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Locator;
import com.bederr.views.View_Message;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Master extends Fragment implements Interface_Load {

    public int id_container;
    public int id_layout;
    private boolean isReady = false;
    private View_Message view_message;

    protected Location location;
    protected String token;

    public void setId_container(int id_container) {
        this.id_container = id_container;
    }

    public void setId_layout(int id_layout) {
        this.id_layout = id_layout;
    }

    public int getId_container() {
        return id_container;
    }

    public int getId_layout() {
        return id_layout;
    }

    private Bederr bederr;

    public Fragment_Master() {
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    public ViewGroup getLayout() {
        return (ViewGroup) getView().findViewById(getId_container());
    }

    public View_Message getView_message() {
        return view_message;
    }

    protected void initView() {
        view_message = new View_Message(getActivity(), 0);
        getLayout().addView(view_message);
    }

    protected void initActionBar() {

    }

    protected void initMap() {

    }

    public boolean isOnline() {
        NetworkInfo localNetworkInfo = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return (localNetworkInfo != null) && (localNetworkInfo.isConnectedOrConnecting());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (isOnline()) {
            isReady = true;
            view_message = new View_Message(getActivity(), 2);
        } else {
            isReady = false;
            view_message = new View_Message(getActivity(), 1);
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        initActionBar();

        if (isReady) {
            initView();
            initMap();
            //getLayout().removeView(getView_message());
        } else {
            getLayout().removeAllViews();
            getLayout().addView(view_message);
        }

        onBackPressed();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        /*
        new Locator(getActivity()).getLocation(Locator.Method.NETWORK_THEN_GPS, new Locator.Listener() {
            @Override
            public void onLocationFound(Location location) {
                isReady = true;
            }

            @Override
            public void onLocationNotFound() {
                isReady = false;
                view_message = new View_Message(getActivity(),2);
            }
        });
         */
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        View localView = inflater.inflate(getId_layout(), viewGroup, false);
        localView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        return localView;
    }

    public Bederr getBederr() {
        return ((Bederr) getActivity());
    }

    public void onBackPressed() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFinishLoad(View view) {
        view.setVisibility(View.VISIBLE);
        getView_message().setVisibility(View.GONE);
    }

    public void startGPS(){
        new Locator(getBederr()).getLocation(Locator.Method.NETWORK_THEN_GPS,new Locator.Listener() {
            @Override
            public void onLocationFound(Location location) {
                Fragment_Master.this.location = location;
            }

            @Override
            public void onLocationNotFound() {
                Fragment_Master.this.location = null;
            }
        });
    }

    public Location getLocation() {
        return location;
    }

    public String getToken() {
        token = new Session_Manager(getBederr()).getUserToken();
        return token;
    }
}


interface Interface_Load {
    void onFinishLoad(View view);
}
