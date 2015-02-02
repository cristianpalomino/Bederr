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

import com.bederr.account_v2.interfaces.OnSuccessCounty;
import com.bederr.account_v2.services.Service_Country;
import com.bederr.account_v2.views.Country_V;
import com.bederr.beans_v2.Country_DTO;
import com.bederr.beans_v2.Place_DTO;
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
public class Ubication_D extends AlertDialog {

    private TextView mensaje;
    private Button aceptar;
    private Button seetings;

    public Ubication_D(Context context) {
        super(context);
        initDialog();
    }

    public Ubication_D(Context context, int theme) {
        super(context, theme);
        initDialog();
    }

    public Ubication_D(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_ubication, null);
        setView(view);

        mensaje = (TextView)view.findViewById(R.id.mensaje);
        aceptar = (Button)view.findViewById(R.id.aceptar);
        seetings = (Button)view.findViewById(R.id.seetings);

        mensaje.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        aceptar.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        seetings.setTypeface(Util_Fonts.setPNASemiBold(getContext()));

        final LinearLayout frame = (LinearLayout) view.findViewById(R.id.containerpaises);
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
                            Country_V country_v = new Country_V(getContext() , country_dtos.get(i));
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

            }
        });

        seetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ubication_D.this.hide();
                getContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
    }
}
