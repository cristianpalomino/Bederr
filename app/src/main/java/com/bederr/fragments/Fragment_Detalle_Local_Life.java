package com.bederr.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bederr.utils.Util_GPS;
import com.bederr.views.View_Local_Life;
import com.bederr.beans.Cupon_DTO;
import com.bederr.beans.Local_DTO;
import com.bederr.main.Bederr_Mapa;
import pe.bederr.com.R;
import com.bederr.database.Database_Maven;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;
import com.bederr.views.View_Cupon_Life;
import com.bederr.views.View_Detalle_Local;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gantz on 5/07/14.
 */
public class Fragment_Detalle_Local_Life extends Fragment_Master {

    private GoogleMap map;

    public Fragment_Detalle_Local_Life() {
        setId_layout(R.layout.fragment_detalle_local);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Detalle_Local_Life newInstance() {
        return new Fragment_Detalle_Local_Life();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();

        Fragment_Detalle_Local_Life.this.onFinishLoad(getView());
        Local_DTO local_dto = getBederr().getLocal_dto();

        LinearLayout cabecera_local = (LinearLayout) getView().findViewById(R.id.container_local);
        LinearLayout detalle_local = (LinearLayout) getView().findViewById(R.id.container_detalle_local);
        LinearLayout linearLayoutCupones = (LinearLayout) getView().findViewById(R.id.container_cupones);

        View_Local_Life view_local = new View_Local_Life(getActivity(),local_dto);
        view_local.setClickable(false);
        cabecera_local.addView(view_local);

        View_Detalle_Local view_detalle_local = new View_Detalle_Local(getActivity(),local_dto);
        view_detalle_local.setClickable(false);
        detalle_local.addView(view_detalle_local);

        try {
            String nombre_local = local_dto.getJsonObject().getString("NombreLocal");
            JSONObject jsonObject = local_dto.getJsonObject();
            JSONArray jsonArrayCuponesLife = jsonObject.getJSONArray("ListaCuponesLife");

            linearLayoutCupones.setVisibility(View.VISIBLE);

            if(new Session_Manager(getActivity()).isLogin()){
                Database_Maven database_maven = new Database_Maven(getActivity());
                if(!(database_maven.getAllEmpresaIds().size() == 0)){
                    if (jsonArrayCuponesLife.length() > 0) {
                        for (int i = 0; i < jsonArrayCuponesLife.length(); i++) {
                            Cupon_DTO cupon_dto = new Cupon_DTO(jsonArrayCuponesLife.getJSONObject(i));
                            View_Cupon_Life view_cupon_life = new View_Cupon_Life(getActivity(), cupon_dto);
                            view_cupon_life.setNombre_local(nombre_local);
                            linearLayoutCupones.addView(view_cupon_life);
                        }
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        ImageView action_left = (ImageView) getView().findViewById(R.id.action_left);
        ImageView action_right = (ImageView) getView().findViewById(R.id.action_right);
        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));

        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                trans.remove(Fragment_Detalle_Local_Life.this);
                trans.commit();
                manager.popBackStack();
            }
        });

        action_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(R.array.opciones_mapa, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Detalle_Local_Life.this.getView().setFocusableInTouchMode(true);
        Fragment_Detalle_Local_Life.this.getView().requestFocus();
        Fragment_Detalle_Local_Life.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Fragment_Detalle_Local_Life.this);
                        trans.commit();
                        manager.popBackStack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void initMap() {
        super.initMap();
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if(status == ConnectionResult.SUCCESS) {
            try {
                final Local_DTO local_dto = getBederr().getLocal_dto();
                JSONObject jsonObject = local_dto.getJsonObject();

                FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
                SupportMapFragment mSupportMapFragment = (SupportMapFragment) mFragmentManager.findFragmentById(R.id.map);
                map = mSupportMapFragment.getMap();

                map.getUiSettings().setAllGesturesEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(false);

                map.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(jsonObject.getString("Latitud")), Double.parseDouble(jsonObject.getString("Longitud"))))
                        .title(jsonObject.getString("NombreLocal"))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                CameraPosition camPos = new CameraPosition.Builder()
                        .target(new LatLng(Double.parseDouble(jsonObject.getString("Latitud")), Double.parseDouble(jsonObject.getString("Longitud"))))
                        .zoom(16)
                        .build();
                CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
                map.animateCamera(camUpd3);

                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        getBederr().setLocal_dto(local_dto);
                        Intent intent = new Intent(getBederr(),Bederr_Mapa.class);
                        intent.putExtra("local_dto",getBederr().getLocal_dto().getJsonObject().toString());
                        startActivity(intent);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    DialogInterface.OnClickListener mDialogListener = new Dialog.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            Util_GPS mGpsUtil = new Util_GPS(getActivity());
            Intent intent;
            switch (which) {
                case 0:
                    try {

                        Local_DTO local_dto = getBederr().getLocal_dto();
                        JSONObject jsonObject = local_dto.getJsonObject();

                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + mGpsUtil.getLatitude() + "," + mGpsUtil.getLongitude() + "&daddr=" + Double.parseDouble(jsonObject.getString("Latitud")) + "," + Double.parseDouble(jsonObject.getString("Longitud")) + "&mode=driving"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    try{
                        Local_DTO local_dto = getBederr().getLocal_dto();
                        JSONObject jsonObject = local_dto.getJsonObject();

                        String url = "waze://?ll="+Double.parseDouble(jsonObject.getString("Latitud"))+","+Double.parseDouble(jsonObject.getString("Longitud"))+"&navigate=yes";
                        intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                        startActivity( intent );
                    }catch (Exception ex){
                        intent = new Intent( Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                        startActivity(intent);
                    }break;
            }
        }
    };
}

