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
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.application.Maven_Application;
import com.bederr.beans_v2.Ubication_DTO;
import com.bederr.main.Bederr;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Locator;
import com.bederr.utils.Util_Fonts;
import com.bederr.views.View_Message;

import java.util.HashMap;

import intercom.intercomsdk.Intercom;
import pe.bederr.com.R;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Master extends Fragment implements Interface_Load {

    public int id_container;
    public int id_layout;
    private boolean isReady = false;
    private View_Message view_message;
    private Location location;
    protected String token;
    private Ubication_DTO ubication;


    public String NEXT = "";
    public String PREVIOUS = "";

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

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

    public Ubication_DTO getUbication() {
        return ((Maven_Application) getBederr().getApplication()).getUbication();
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
        //getLayout().addView(view_message);
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

        /*
        if (isReady) {
            initView();
            initMap();
            //getLayout().removeView(getView_message());
        } else {
            getLayout().removeAllViews();
            getLayout().addView(view_message);
        }
        */

        initView();
        initMap();

        onBackPressed();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        new Locator(getActivity()).getLocation(Locator.Method.NETWORK_THEN_GPS, new Locator.Listener() {
            @Override
            public void onLocationFound(Location mlocation) {
                isReady = true;
                location = mlocation;
            }

            @Override
            public void onLocationNotFound() {
                try {
                    isReady = false;
                    view_message = new View_Message(getActivity(), 2);
                    location = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

    public String getToken() {
        token = new Session_Manager(getBederr()).getUserToken();
        return token;
    }

    public void closeKeyboard() {
        getBederr().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public View getEmptyView(ListView list) {
        LinearLayout empty = (LinearLayout) getView().findViewById(R.id.empty_view);
        empty.setVisibility(View.GONE);

        LinearLayout loader = (LinearLayout) getView().findViewById(R.id.loader);
        loader.setVisibility(View.GONE);

        getLayout().setVisibility(View.VISIBLE);
        list.setVisibility(View.VISIBLE);
        return empty;
    }

    public void showLoader() {
        LinearLayout loader = (LinearLayout) getView().findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
    }


    public void setEmptyView() {
        LinearLayout loader = (LinearLayout) getView().findViewById(R.id.loader);
        loader.setVisibility(View.GONE);

        View empty = getView().findViewById(R.id.empty_view);
        TextView message = (TextView) empty.findViewById(R.id.text_type_message_no_data);
        message.setTypeface(Util_Fonts.setPNASemiBold(getBederr()));
        empty.setVisibility(View.VISIBLE);

        getLayout().setVisibility(View.GONE);
    }

    public void openLoginIntercom(String mail) {
        showMessage(mail);
        Intercom.beginSessionWithEmail(mail,
                new Intercom.IntercomEventListener() {
                    @Override
                    public void onComplete(String error) {
                        // lets check if we get an error string
                        if (error != null) {
                            showMessage(error);
                        } else {
                            showMessage("LOGIN");
                            HashMap<String, Object> attributes = new HashMap<String, Object>();
                            HashMap<String, Object> customAttributes = new HashMap<String, Object>();
                            customAttributes.put("paid_subscriber", "Yes");
                            customAttributes.put("monthly_spend", 155.5);
                            customAttributes.put("team_mates", 3);
                            customAttributes.put("email", "ca.palomino.rivera@gmail.com");
                            customAttributes.put("user_id", "12");
                            attributes.put("custom_attributes", customAttributes);
                            Intercom.updateUser(attributes);
                        }
                    }


                });
    }
}

interface Interface_Load {
    void onFinishLoad(View view);
}
