package com.bederr.dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import pe.bederr.com.R;
import com.bederr.utils.Util_Fonts;

/**
 * Created by Gantz on 7/08/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Dialog_Maven extends AlertDialog {

    private EditText edtcodigolife;

    public Dialog_Maven(Context context) {
        super(context);
        initDialog();
    }

    public Dialog_Maven(Context context, int theme) {
        super(context, theme);
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_maven, null);
        setView(view);

        TextView txtdescripcionlife = (TextView) view.findViewById(R.id.txtavisodialoglife);
        txtdescripcionlife.setTypeface(Util_Fonts.setPNALight(getContext()));
    }
}
