package com.bederr.dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gantz on 7/08/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Dialog_Olvidaste_Contrasenia extends AlertDialog implements View.OnClickListener {

    private View view;

    @Required(order = 5, message = "El campo email es requerido")
    @Email(order = 8, message = "El email es incorrecto")
    private EditText edt_correo;

    private Button btn_enviar_correo;
    private Validator validator;

    public Dialog_Olvidaste_Contrasenia(Context context) {
        super(context);
        initDialog();
    }

    public Dialog_Olvidaste_Contrasenia(Context context, int theme) {
        super(context, theme);
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.dialog_olvidaste_contrasenia, null);
        setView(view);

        validator = new Validator(this);
        edt_correo = (EditText) view.findViewById(R.id.txt_correo);

        btn_enviar_correo = (Button) view.findViewById(R.id.btn_cambiar_password);
        btn_enviar_correo.setOnClickListener(this);

        ((TextView) view.findViewById(R.id.action_middle)).setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        edt_correo.setTypeface(Util_Fonts.setPNALight(getContext()));
        btn_enviar_correo.setTypeface(Util_Fonts.setPNASemiBold(getContext()));

        initActionBar(view);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(final View v) {
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                v.setVisibility(View.GONE);
                view.findViewById(R.id.img_logo_beeder).setVisibility(View.VISIBLE);

                RequestParams params = new RequestParams();
                params.put("token", WS_Maven.TOKEN_MAVEN);
                params.put("correo", edt_correo.getText().toString());

                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.setTimeout(180000);
                asyncHttpClient.post(getContext(), WS_Maven.WS_RECUPERAR_CONTRASENIA, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (!response.isNull("mensaje")) {
                            try {
                                Toast.makeText(getContext(), response.getString("mensaje"), Toast.LENGTH_SHORT).show();
                                dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Correo no existe...!", Toast.LENGTH_SHORT).show();
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
            public void onValidationFailed(View failedView, Rule<?> failedRule) {
                ((EditText) failedView).setError(failedRule.getFailureMessage());
            }
        });
        validator.validate();
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
