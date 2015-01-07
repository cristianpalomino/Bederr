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
import android.widget.Toast;

import com.bederr.beans.Empresa_DTO;
import com.bederr.database.Database_Maven;
import com.bederr.operations.Operation_Registrar_Empresa;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

import pe.bederr.com.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gantz on 7/08/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Dialog_Life extends AlertDialog implements View.OnClickListener{

    private Empresa_DTO empresa_dto;
    private EditText edtcodigolife;
    private Interface_Dialog_Life interface_dialog_life;

    public Dialog_Life(Context context, Empresa_DTO empresa_dto) {
        super(context);
        this.empresa_dto = empresa_dto;
        initDialog();
    }

    public Dialog_Life(Context context, int theme, Empresa_DTO empresa_dto) {
        super(context, theme);
        this.empresa_dto = empresa_dto;
        initDialog();
    }

    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.dialog_life, null);
        setView(view);

        TextView txtdescripcionlife = (TextView) view.findViewById(R.id.txtavisodialoglife);
        Button btnaceptarlife = (Button) view.findViewById(R.id.btnaceptarlife);
        edtcodigolife = (EditText) view.findViewById(R.id.edtcodigolife);

        edtcodigolife.setTypeface(Util_Fonts.setPNALight(getContext()));
        txtdescripcionlife.setTypeface(Util_Fonts.setPNALight(getContext()));
        btnaceptarlife.setTypeface(Util_Fonts.setPNASemiBold(getContext()));

        btnaceptarlife.setOnClickListener(this);

        JSONObject jsonObject = empresa_dto.getJsonObject();
        try {
            String validacionempresa = jsonObject.getString("Validacion");
            if(validacionempresa.equals("0")){
                edtcodigolife.setVisibility(View.GONE);
            }else{
                edtcodigolife.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            String idUsuario = new Session_Manager(getContext()).getSession().getJsonObject().getString("idUsuario");
            final String idEmpresaLife = empresa_dto.getJsonObject().getString("Id");
            String dniUsuario = edtcodigolife.getText().toString();
            String validacion = empresa_dto.getJsonObject().getString("Validacion");

            Operation_Registrar_Empresa operation_registrar_empresa = new Operation_Registrar_Empresa(getContext());
            operation_registrar_empresa.registrarEmpresaLife(idUsuario,idEmpresaLife,dniUsuario,validacion);
            operation_registrar_empresa.setInterface_operation_registrar_empresa(new Operation_Registrar_Empresa.Interface_Operation_Registrar_Empresa() {
                @Override
                public void registarEmpresaLife(boolean status, JSONObject response, String mensaje) {
                    dismiss();
                    if(status){
                        Database_Maven database_maven = new Database_Maven(getContext());
                        Empresa_DTO empresa_dto = new Empresa_DTO();
                        empresa_dto.setEmpresaId(idEmpresaLife);
                        database_maven.addEmpresaId(empresa_dto);

                        interface_dialog_life.updateView(true);
                        Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                    }else{
                        interface_dialog_life.updateView(false);
                        Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setInterface_dialog_life(Interface_Dialog_Life interface_dialog_life) {
        this.interface_dialog_life = interface_dialog_life;
    }

    public interface Interface_Dialog_Life {
        void updateView(boolean state);
    }
}
