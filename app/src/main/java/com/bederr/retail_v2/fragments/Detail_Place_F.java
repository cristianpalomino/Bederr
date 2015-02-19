package com.bederr.retail_v2.fragments;

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

import com.bederr.beans_v2.CorporateOffer_DTO;
import com.bederr.beans_v2.Offer_DTO;
import com.bederr.main.Bederr_Mapa;
import com.bederr.fragments.Fragment_Master;
import com.bederr.retail_v2.views.Offer_Corporate_V;
import com.bederr.retail_v2.views.Offer_V;
import com.bederr.retail_v2.views.View_Detalle_Local_v2;
import com.bederr.utils.Util_GPS;
import com.bederr.beans_v2.Place_DTO;

import pe.bederr.com.R;

import com.bederr.retail_v2.views.View_Local_v2;
import com.bederr.utils.Util_Fonts;
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

/**
 * Created by Gantz on 5/07/14.
 */
public class Detail_Place_F extends Fragment_Master {

    private GoogleMap map;
    private String option;
    private OnBack onBack;

    public Detail_Place_F()
    {
        setId_layout(R.layout.fragment_detalle_local);
        setId_container(R.id.frame_container);
    }

    public void setOnBack(OnBack onBack) {
        this.onBack = onBack;
    }

    public static final Detail_Place_F newInstance(String option) {
        Detail_Place_F detail_place_f = new Detail_Place_F();
        Bundle bundle = new Bundle();
        bundle.putString("option",option);
        detail_place_f.setArguments(bundle);
        return detail_place_f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        option = getArguments().getString("option");
    }

    @Override
    protected void initView() {
        super.initView();
        closeKeyboard();
        Place_DTO place_dto = getBederr().getPlace_dto();

        LinearLayout cabecera_local = (LinearLayout) getView().findViewById(R.id.container_local);
        LinearLayout detalle_local = (LinearLayout) getView().findViewById(R.id.container_detalle_local);
        LinearLayout linearLayoutCupones = (LinearLayout) getView().findViewById(R.id.container_cupones);

        View_Local_v2 view_local_v2 = new View_Local_v2(getActivity(), place_dto , option);
        cabecera_local.addView(view_local_v2);

        View_Detalle_Local_v2 view_detalle_local_v2 = new View_Detalle_Local_v2(getActivity(), place_dto);
        detalle_local.addView(view_detalle_local_v2);

        /**
         * Validad Cupones
         */

        if(option.equals("Explore"))
        {
            if(place_dto.getInplace_offers().size() > 0){
                for (int i = 0; i < place_dto.getInplace_offers().size() ; i++) {
                    Offer_DTO offer_dto = place_dto.getInplace_offers().get(i);
                    Offer_V offer_v = new Offer_V(getBederr(),offer_dto,0);
                    linearLayoutCupones.addView(offer_v);
                }
            }

            if(place_dto.getSpecial_offers().size() > 0){
                for (int i = 0; i < place_dto.getSpecial_offers().size() ; i++) {
                    Offer_DTO offer_dto = place_dto.getSpecial_offers().get(i);
                    Offer_V offer_v = new Offer_V(getBederr(),offer_dto,1);
                    linearLayoutCupones.addView(offer_v);
                }
            }

            if(place_dto.getCorporate_offers().size() > 0){
                for (int i = 0; i < place_dto.getCorporate_offers().size() ; i++) {
                    CorporateOffer_DTO corporateOffer_dto = place_dto.getCorporate_offers().get(i);
                    Offer_Corporate_V offer_corporate_v = new Offer_Corporate_V(getBederr(),corporateOffer_dto,-1);
                    linearLayoutCupones.addView(offer_corporate_v);
                }
            }

            if(place_dto.getLegacy_offers().size() > 0){
                for (int i = 0; i < place_dto.getLegacy_offers().size() ; i++) {
                    CorporateOffer_DTO corporateOffer_dto = place_dto.getLegacy_offers().get(i);
                    Offer_Corporate_V offer_corporate_v = new Offer_Corporate_V(getBederr(),corporateOffer_dto,-1);
                    linearLayoutCupones.addView(offer_corporate_v);
                }
            }
        }
        else if(option.equals("Benefits"))
        {
            if(place_dto.getCorporate_offers().size() > 0){
                for (int i = 0; i < place_dto.getCorporate_offers().size() ; i++) {
                    CorporateOffer_DTO corporateOffer_dto = place_dto.getCorporate_offers().get(i);
                    Offer_Corporate_V offer_corporate_v = new Offer_Corporate_V(getBederr(),corporateOffer_dto,-1);
                    linearLayoutCupones.addView(offer_corporate_v);
                }
            }

            if(place_dto.getLegacy_offers().size() > 0) {
                for (int i = 0; i < place_dto.getLegacy_offers().size(); i++) {
                    CorporateOffer_DTO corporateOffer_dto = place_dto.getLegacy_offers().get(i);
                    Offer_Corporate_V offer_corporate_v = new Offer_Corporate_V(getBederr(), corporateOffer_dto, -1);
                    linearLayoutCupones.addView(offer_corporate_v);

                }
            }
        }

        linearLayoutCupones.setVisibility(View.VISIBLE);
        Detail_Place_F.this.onFinishLoad(getView());
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
                trans.remove(Detail_Place_F.this);
                trans.commit();
                manager.popBackStack();
                onBack.onBack();
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
        Detail_Place_F.this.getView().setFocusableInTouchMode(true);
        Detail_Place_F.this.getView().requestFocus();
        Detail_Place_F.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Detail_Place_F.this);
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

    @Override
    protected void initMap() {
        super.initMap();
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (status == ConnectionResult.SUCCESS) {
            FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
            SupportMapFragment mSupportMapFragment = (SupportMapFragment) mFragmentManager.findFragmentById(R.id.map);
            map = mSupportMapFragment.getMap();

            map.getUiSettings().setAllGesturesEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(false);

            map.addMarker(new MarkerOptions()
                    .position(
                            new LatLng(Double.parseDouble(getBederr().getPlace_dto().getPoint_dto().getLatitude()),
                                    Double.parseDouble(getBederr().getPlace_dto().getPoint_dto().getLongitude())))
                    .title(getBederr().getPlace_dto().getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            CameraPosition camPos = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(getBederr().getPlace_dto().getPoint_dto().getLatitude()),
                            Double.parseDouble(getBederr().getPlace_dto().getPoint_dto().getLongitude())))
                    .zoom(16)
                    .build();
            CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
            map.animateCamera(camUpd3);

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    getBederr().setPlace_dto(getBederr().getPlace_dto());
                    Intent intent = new Intent(getBederr(), Bederr_Mapa.class);
                    intent.putExtra("place_dto", getBederr().getPlace_dto().getDataSource().toString());
                    startActivity(intent);
                }
            });
        }
    }

    DialogInterface.OnClickListener mDialogListener = new Dialog.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Util_GPS mGpsUtil = new Util_GPS(getActivity());
            Intent intent;
            switch (which) {
                case 0:
                    intent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=" +
                                    mGpsUtil.getLatitude() + "," +
                                    mGpsUtil.getLongitude() + "&daddr=" +
                                    Double.parseDouble(getBederr().getPlace_dto().getPoint_dto().getLatitude()) + "," +
                                    Double.parseDouble(getBederr().getPlace_dto().getPoint_dto().getLongitude()) +
                                    "&mode=driving"));
                    startActivity(intent);
                    break;
                default:
                    try {
                        String url = "waze://?ll=" +
                                Double.parseDouble(getBederr().getPlace_dto().getPoint_dto().getLatitude()) +
                                "," +
                                Double.parseDouble(getBederr().getPlace_dto().getPoint_dto().getLongitude()) +
                                "&navigate=yes";
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } catch (Exception ex) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                        startActivity(intent);
                    }
                    break;
            }
        }
    };

    public interface OnBack {
        public void onBack();
    }
}

