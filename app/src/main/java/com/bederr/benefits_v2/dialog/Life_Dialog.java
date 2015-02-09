package com.bederr.benefits_v2.dialog;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.account_v2.dialogs.Update_Profile_Dialog;
import com.bederr.beans_v2.Benefit_Program_DTO;
import com.bederr.benefits_v2.interfaces.OnSuccessAdd;
import com.bederr.session.Session_Manager;

import pe.bederr.com.R;

import com.bederr.benefits_v2.services.Service_Programs;
import com.bederr.utils.Util_Fonts;

/**
 * Created by Gantz on 7/08/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Life_Dialog extends AlertDialog implements View.OnClickListener {

    private Benefit_Program_DTO benefit_program_dto;
    private EditText edtcodigolife;
    private Interface_Dialog_Life interface_dialog_life;
    private ActionBarActivity activity;

    public Life_Dialog(Context context, Benefit_Program_DTO benefit_program_dto) {
        super(context);
        this.benefit_program_dto = benefit_program_dto;
        initDialog();
    }

    public Life_Dialog(Context context, int theme, Benefit_Program_DTO benefit_program_dto) {
        super(context, theme);
        this.benefit_program_dto = benefit_program_dto;
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_life, null);
        setView(view);

        TextView txtdescripcionlife = (TextView) view.findViewById(R.id.txtavisodialoglife);
        Button btnaceptarlife = (Button) view.findViewById(R.id.btnaceptarlife);
        edtcodigolife = (EditText) view.findViewById(R.id.edtcodigolife);
        edtcodigolife.setVisibility(View.GONE);

        edtcodigolife.setTypeface(Util_Fonts.setPNALight(getContext()));
        txtdescripcionlife.setTypeface(Util_Fonts.setPNALight(getContext()));
        btnaceptarlife.setTypeface(Util_Fonts.setPNASemiBold(getContext()));

        btnaceptarlife.setOnClickListener(this);

        /*
        if (!benefit_program_dto.isValidate()) {
            edtcodigolife.setVisibility(View.GONE);
        } else {
            edtcodigolife.setVisibility(View.VISIBLE);
        }
        */
    }

    @Override
    public void onClick(View v) {
        String token = new Session_Manager(getContext()).getUserToken();
        String id = String.valueOf(benefit_program_dto.getId());

        Service_Programs service_programs = new Service_Programs(getContext());
        service_programs.sendRequestAdd(token,id);
        service_programs.setOnSuccessAdd(new OnSuccessAdd() {
            @Override
            public void onSuccessAdd(boolean success, String message) {
                Life_Dialog.this.hide();
                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setActivity(ActionBarActivity activity) {
        this.activity = activity;
    }

    public ActionBarActivity getActivity() {
        return activity;
    }

    public void setInterface_dialog_life(Interface_Dialog_Life interface_dialog_life) {
        this.interface_dialog_life = interface_dialog_life;
    }

    public interface Interface_Dialog_Life {
        void updateView(boolean state);
    }
}
