package com.bederr.dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pe.bederr.com.R;

import com.bederr.ws.WS_Maven;
import com.bederr.utils.Util_Fonts;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Gantz on 7/08/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Dialog_Editar_Perfil extends AlertDialog implements View.OnClickListener, DialogInterface.OnClickListener {

    private View view;

    private EditText edtnombres;
    private EditText edtapellidos;
    private EditText edtcorreo;
    private TextView txtsexo;
    private TextView txtcumpleanios;

    private Button btn_editar;

    public Dialog_Editar_Perfil(Context context) {
        super(context);
        initDialog();
    }

    public Dialog_Editar_Perfil(Context context, int theme) {
        super(context, theme);
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.dialog_editar_perfil, null);
        setView(view);

        edtnombres = (EditText) view.findViewById(R.id.txt_nombres);
        edtapellidos = (EditText) view.findViewById(R.id.txt_apellidos);
        edtcorreo = (EditText) view.findViewById(R.id.txt_correo);
        txtcumpleanios = (TextView) view.findViewById(R.id.txt_cumpleanios);
        txtsexo = (TextView) view.findViewById(R.id.txt_sexo);

        btn_editar = (Button) view.findViewById(R.id.btn_cambiar_password);
        btn_editar.setOnClickListener(this);

        ((TextView) view.findViewById(R.id.action_middle)).setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        edtnombres.setTypeface(Util_Fonts.setPNALight(getContext()));
        edtapellidos.setTypeface(Util_Fonts.setPNALight(getContext()));
        edtcorreo.setTypeface(Util_Fonts.setPNALight(getContext()));
        txtcumpleanios.setTypeface(Util_Fonts.setPNALight(getContext()));
        txtsexo.setTypeface(Util_Fonts.setPNALight(getContext()));
        btn_editar.setTypeface(Util_Fonts.setPNASemiBold(getContext()));

        txtcumpleanios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                                txtcumpleanios.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        txtsexo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Builder builderSingle = new Builder(getContext());
                builderSingle.setIcon(R.drawable.ic_launcher);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.simple_list_item_1);
                arrayAdapter.add("Masculino");
                arrayAdapter.add("Femenino");

                builderSingle.setAdapter(arrayAdapter,Dialog_Editar_Perfil.this);
                builderSingle.create();
                builderSingle.show();
            }
        });

        initActionBar(view);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        v.setVisibility(View.GONE);
        view.findViewById(R.id.img_logo_beeder).setVisibility(View.VISIBLE);

        RequestParams params = new RequestParams();
        params.put("token", WS_Maven.TOKEN_MAVEN);
        params.put("email", edtcorreo.getText().toString());
        params.put("nombre", edtnombres.getText().toString());
        params.put("apellido", edtapellidos.getText().toString());
        params.put("fechanacimiento", txtcumpleanios.getText().toString());
        params.put("sexo", txtsexo.getText().toString());

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(180000);
        asyncHttpClient.post(getContext(), WS_Maven.WS_EDITAR_PERFIL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (!response.isNull("mensaje")) {
                    try {
                        Toast.makeText(getContext(), response.getString("mensaje"), Toast.LENGTH_SHORT).show();
                        dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Ocurrio un Error...!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getContext(), "Ocurrio un Error...!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if(i == 0){
            txtsexo.setText("Masculino");
        }else{
            txtsexo.setText("Femenino");
        }
    }

    protected void initActionBar(final View view) {
        ImageView action_left = (ImageView) view.findViewById(R.id.action_right);

        TextView action_middle = (TextView) view.findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
    }
}
