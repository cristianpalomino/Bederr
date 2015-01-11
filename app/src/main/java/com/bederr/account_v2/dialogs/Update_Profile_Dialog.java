package com.bederr.account_v2.dialogs;

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

import com.bederr.account_v2.services.Service_Update;
import com.bederr.account_v2.interfaces.OnSuccessUpdate;
import com.bederr.beans_v2.User_DTO;
import pe.bederr.com.R;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

import java.util.Calendar;

/**
 * Created by Gantz on 7/08/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Update_Profile_Dialog extends AlertDialog implements View.OnClickListener, DialogInterface.OnClickListener {

    private View view;

    private EditText edtnombres;
    private EditText edtapellidos;
    private EditText edtcorreo;
    private TextView txtsexo;
    private TextView txtcumpleanios;
    private TextView txtdni;

    private Button btn_editar;

    public Update_Profile_Dialog(Context context) {
        super(context);
        initDialog();
    }

    public Update_Profile_Dialog(Context context, int theme) {
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
        txtdni = (TextView) view.findViewById(R.id.txt_dni);

        btn_editar = (Button) view.findViewById(R.id.btn_cambiar_password);
        btn_editar.setOnClickListener(this);

        Session_Manager session_manager = new Session_Manager(getContext());
        if(session_manager.getSessionType() == 1){
            edtnombres.setVisibility(View.GONE);
            edtapellidos.setVisibility(View.GONE);
            edtcorreo.setVisibility(View.GONE);
            txtcumpleanios.setVisibility(View.GONE);
            txtsexo.setVisibility(View.GONE);
            txtdni.setVisibility(View.VISIBLE);
        }else{
            edtnombres.setVisibility(View.VISIBLE);
            edtapellidos.setVisibility(View.VISIBLE);
            edtcorreo.setVisibility(View.VISIBLE);
            txtcumpleanios.setVisibility(View.VISIBLE);
            txtsexo.setVisibility(View.VISIBLE);
            txtdni.setVisibility(View.VISIBLE);
        }

        ((TextView) view.findViewById(R.id.action_middle)).setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        edtnombres.setTypeface(Util_Fonts.setPNALight(getContext()));
        edtapellidos.setTypeface(Util_Fonts.setPNALight(getContext()));
        edtcorreo.setTypeface(Util_Fonts.setPNALight(getContext()));
        txtcumpleanios.setTypeface(Util_Fonts.setPNALight(getContext()));
        txtsexo.setTypeface(Util_Fonts.setPNALight(getContext()));
        txtdni.setTypeface(Util_Fonts.setPNALight(getContext()));
        btn_editar.setTypeface(Util_Fonts.setPNASemiBold(getContext()));

        User_DTO user_dto = new User_DTO();
        user_dto.setDataSource(new Session_Manager(getContext()).getSession_v2());
        if(user_dto != null){
            edtnombres.setText(user_dto.getFirst_name());
            edtapellidos.setText(user_dto.getLast_name());
            edtcorreo.setText(user_dto.getEmail());
            String gender = user_dto.getGender();
            if (gender.equals("f")) {
                txtsexo.setText("Femenino");
            } else {
                txtsexo.setText("Masculino");
            }
            txtcumpleanios.setText(user_dto.getBirthday());
        }

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
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txtcumpleanios.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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

                builderSingle.setAdapter(arrayAdapter, Update_Profile_Dialog.this);
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
    public void onClick(final View v) {
        v.setVisibility(View.GONE);
        view.findViewById(R.id.img_logo_beeder).setVisibility(View.VISIBLE);

        Session_Manager session_manager = new Session_Manager(getContext());
        String token = session_manager.getUserToken();
        User_DTO user_dto = new User_DTO();
        user_dto.setDataSource(session_manager.getSession_v2());

        Service_Update service_update = new Service_Update(getContext());
        service_update.sendRequest(token,user_dto,txtdni.getText().toString());
        service_update.setOnSuccessUpdate(new OnSuccessUpdate() {
            @Override
            public void onSuccessUpdate(boolean success, String message) {
                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                if(success){
                    Update_Profile_Dialog.this.hide();
                }else{
                    v.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.img_logo_beeder).setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == 0) {
            txtsexo.setText("Masculino");
        } else {
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
