package com.bederr.main;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.bederr.utils.Util_GPS;
import com.bederr.beans_v2.Place_DTO;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import pe.bederr.com.R;

/**
 * Created by Gantz on 14/08/14.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Bederr_Mapa extends ActionBarActivity {

    private GoogleMap googleMap;
    private Place_DTO place_dto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mapa);
        getSupportActionBar().hide();

        place_dto = new Place_DTO();
        try {
            JSONObject object = null;
            object = new JSONObject(getIntent().getStringExtra("place_dto"));
            place_dto.setDataSource(object);
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.acb_img_ruta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Bederr_Mapa.this);
                builder.setItems(R.array.opciones_mapa, mDialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        findViewById(R.id.acb_img_cerrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bederr_Mapa.this.finish();
            }
        });
    }

    private void initilizeMap() {
        if (googleMap == null) {

            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(false);

            googleMap.addMarker(new MarkerOptions()
                    .position(
                            new LatLng(Double.parseDouble(place_dto.getPoint_dto().getLatitude()),
                                    Double.parseDouble(place_dto.getPoint_dto().getLongitude())))
                    .title(place_dto.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            CameraPosition camPos = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(place_dto.getPoint_dto().getLatitude()),
                            Double.parseDouble(place_dto.getPoint_dto().getLongitude())))
                    .zoom(16)
                    .build();
     
            CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
            googleMap.moveCamera(camUpd3);

            if (googleMap == null) {
                Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DialogInterface.OnClickListener mDialogListener = new Dialog.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            Util_GPS mGpsUtil = new Util_GPS(Bederr_Mapa.this);
            Intent intent;

            switch (which) {
                case 0:
                        intent = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=" +
                                        mGpsUtil.getLatitude() + "," +
                                        mGpsUtil.getLongitude() + "&daddr=" +
                                        Double.parseDouble(place_dto.getPoint_dto().getLatitude()) + "," +
                                        Double.parseDouble(place_dto.getPoint_dto().getLongitude()) +
                                        "&mode=driving"));
                        startActivity(intent);
                    break;
                default:
                    try {
                        String url = "waze://?ll=" +
                                     Double.parseDouble(place_dto.getPoint_dto().getLatitude()) +
                                     "," +
                                     Double.parseDouble(place_dto.getPoint_dto().getLongitude()) +
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
}
