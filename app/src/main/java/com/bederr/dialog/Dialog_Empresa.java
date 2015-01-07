package com.bederr.dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bederr.beans.Empresa_DTO;
import pe.bederr.com.R;
import com.bederr.utils.Util_Fonts;

/**
 * Created by Gantz on 7/08/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Dialog_Empresa extends AlertDialog implements View.OnClickListener{

    private Empresa_DTO empresa_dto;
    private EditText edtcodigolife;
    private Interface_Dialog_Empresa interface_dialog_empresa;

    public Dialog_Empresa(Context context) {
        super(context);
        initDialog();
    }

    public Dialog_Empresa(Context context, int theme) {
        super(context, theme);
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_empresa, null);
        setView(view);

        Button btnaceptarlife = (Button) view.findViewById(R.id.btneliminarempresa);
        TextView txtdescripcionlife = (TextView) view.findViewById(R.id.txtavisodialoglife);

        btnaceptarlife.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        txtdescripcionlife.setTypeface(Util_Fonts.setPNALight(getContext()));
        btnaceptarlife.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        interface_dialog_empresa.onAcepted(this);
    }

    public void setInterface_dialog_empresa(Interface_Dialog_Empresa interface_dialog_empresa) {
        this.interface_dialog_empresa = interface_dialog_empresa;
    }

    public interface Interface_Dialog_Empresa {
        void onAcepted(Dialog_Empresa dialog_cupon);
    }
}
