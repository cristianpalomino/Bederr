package com.bederr.retail_v2.dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.account_v2.interfaces.OnSuccessCounty;
import com.bederr.account_v2.services.Service_Country;
import com.bederr.account_v2.views.Area_V;
import com.bederr.account_v2.views.Country_V;
import com.bederr.application.Maven_Application;
import com.bederr.beans_v2.Country_DTO;
import com.bederr.beans_v2.Location_DTO;
import com.bederr.beans_v2.Place_DTO;
import com.bederr.beans_v2.Ubication_DTO;
import com.bederr.main.Bederr;
import com.bederr.questions_v2.adapters.Places_Question_A;
import com.bederr.questions_v2.fragments.Answer_F;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

import pe.bederr.com.R;

/**
 * Created by Gantz on 7/08/14.
 */


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Ubication_D extends AlertDialog implements Country_V.OnChecked {

    private TextView mensaje;
    private Button aceptar;
    private Button seetings;
    private LinearLayout frame;
    private ActionBarActivity activity;

    public Ubication_D(Context context,ActionBarActivity activity) {
        super(context);
        this.activity = activity;
        initDialog();
    }

    public Ubication_D(Context context, int theme,ActionBarActivity activity) {
        super(context, theme);
        this.activity = activity;
        initDialog();
    }

    public Ubication_D(Context context, boolean cancelable, OnCancelListener cancelListener,ActionBarActivity activity) {
        super(context, cancelable, cancelListener);
        this.activity = activity;
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_ubication, null);
        setView(view);

        mensaje = (TextView) view.findViewById(R.id.mensaje);
        aceptar = (Button) view.findViewById(R.id.aceptar);
        seetings = (Button) view.findViewById(R.id.seetings);

        mensaje.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        aceptar.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        seetings.setTypeface(Util_Fonts.setPNASemiBold(getContext()));

        frame = (LinearLayout) view.findViewById(R.id.containerpaises);
        Service_Country service_country = new Service_Country(getContext());
        service_country.sendRequest();
        service_country.setOnSuccessCounty(new OnSuccessCounty() {
            @Override
            public void onSuccessCountry(boolean success,
                                         ArrayList<Country_DTO> country_dtos,
                                         String message) {
                try {
                    if (success) {
                        for (int i = 0; i < country_dtos.size(); i++) {
                            Country_V country_v = new Country_V(getContext(), country_dtos.get(i));
                            country_v.setOnChecked(Ubication_D.this);
                            frame.addView(country_v);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag = false;
                ArrayList<Area_V> areas = new ArrayList<Area_V>();
                Area_V area_v = null;

                for (int i = 0; i < frame.getChildCount(); i++) {
                    Country_V country_v = (Country_V) frame.getChildAt(i);
                    for (int j = 0; j < country_v.getAreas().size(); j++) {
                        areas.add(country_v.getAreas().get(j));
                    }
                }

                for (int i = 0; i < areas.size(); i++) {
                    if(areas.get(i).isChecked()){
                        flag = true;
                        area_v = areas.get(i);
                    }
                }

                if(!flag){
                    showMessage("Seleccione una Ciudad");
                }else{
                    Ubication_D.this.dismiss();
                    Ubication_D.this.hide();

                    Location_DTO location = new Location_DTO();
                    location.setPais(area_v.getCountry_v().getCountry_dto().getName());
                    location.setCiudad(area_v.getArea_dto().getName());
                    location.setFlag(area_v.getCountry_v().getCountry_dto().getFlag_image());
                    ((Maven_Application)activity.getApplication()).setLocation_dto(location);

                    showMessage("Pais : " + location.getPais() + "\n" +
                                "Ciudad : " + location.getCiudad());

                    Ubication_DTO dto = new Ubication_DTO(null,area_v.getArea_dto().getId());
                    ((Maven_Application)activity.getApplication()).setUbication(dto);
                    ((Bederr)activity).restart();
                }
            }
        });

        seetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ubication_D.this.dismiss();
                Ubication_D.this.hide();
                getContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                ((Bederr)activity).setFlag(true);
            }
        });
    }

    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChecked(boolean checked,Object tag,Country_V mcountry_v,Area_V marea_v) {

        ArrayList<Area_V> areas = new ArrayList<Area_V>();

        for (int i = 0; i < frame.getChildCount(); i++) {
            Country_V country_v = (Country_V) frame.getChildAt(i);
            for (int j = 0; j < country_v.getAreas().size(); j++) {
                areas.add(country_v.getAreas().get(j));
            }
        }

        for (int i = 0; i < areas.size(); i++) {
            if(!areas.get(i).getArea_dto().getId().equals(tag)){
                Area_V area_v = areas.get(i);
                area_v.setChecked();
            }
        }
    }

}
