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

import com.bederr.account_v2.interfaces.OnSuccessPassword;
import com.bederr.account_v2.services.Service_Password;

import pe.bederr.com.R;

import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

/**
 * Created by Gantz on 7/08/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Dialog_Contrasenia extends AlertDialog implements View.OnClickListener {

    private View view;
    private EditText edt_contrasenia;
    private EditText edt_new_contrasenia;
    private EditText edt_re_new_contrasenia;
    private EditText edt_correo;
    private Button btn_cambiar_contrasenia;

    public Dialog_Contrasenia(Context context) {
        super(context);
        initDialog();
    }

    public Dialog_Contrasenia(Context context, int theme) {
        super(context, theme);
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.dialog_contrasenia, null);
        setView(view);

        edt_contrasenia = (EditText) view.findViewById(R.id.txt_password);
        edt_new_contrasenia = (EditText) view.findViewById(R.id.txt_new_password);
        edt_re_new_contrasenia = (EditText) view.findViewById(R.id.txt_re_new_password);
        edt_correo = (EditText) view.findViewById(R.id.txt_correo);

        btn_cambiar_contrasenia = (Button) view.findViewById(R.id.btn_cambiar_password);
        btn_cambiar_contrasenia.setOnClickListener(this);

        ((TextView) view.findViewById(R.id.action_middle)).setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        edt_contrasenia.setTypeface(Util_Fonts.setPNALight(getContext()));
        edt_new_contrasenia.setTypeface(Util_Fonts.setPNALight(getContext()));
        edt_re_new_contrasenia.setTypeface(Util_Fonts.setPNALight(getContext()));
        edt_correo.setTypeface(Util_Fonts.setPNALight(getContext()));
        btn_cambiar_contrasenia.setTypeface(Util_Fonts.setPNASemiBold(getContext()));

        initActionBar(view);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(final View v) {
        if (!edt_correo.getText().toString().matches("")) {
            if (!edt_contrasenia.getText().toString().matches("")) {
                if (!edt_new_contrasenia.getText().toString().matches("")) {
                    v.setVisibility(View.GONE);
                    view.findViewById(R.id.img_logo_beeder).setVisibility(View.VISIBLE);

                    String email = edt_correo.getText().toString();
                    String old = edt_contrasenia.getText().toString();
                    String nueva = edt_new_contrasenia.getText().toString();
                    String token = new Session_Manager(getContext()).getUserToken();

                    Service_Password service_password = new Service_Password(getContext());
                    service_password.sendRequest(email, nueva, old, token);
                    service_password.setOnSuccessPassword(new OnSuccessPassword() {
                        @Override
                        public void onSuccessPassword(boolean success, String message) {
                            view.findViewById(R.id.img_logo_beeder).setVisibility(View.GONE);
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            if (success) {
                                Dialog_Contrasenia.this.dismiss();
                            }else{
                                v.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Ingrese su nueva contraseña...!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Ingrese su contraseña actual...!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Ingrese su correo...!", Toast.LENGTH_SHORT).show();
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
