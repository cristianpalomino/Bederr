package com.bederr.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bederr.beans.Local_DTO;

import pe.bederr.com.R;
import com.bederr.utils.Util_Fonts;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gantz on 5/07/14.
 */
public class View_Detalle_Local extends RelativeLayout {

    private Local_DTO local_dto;

    public View_Detalle_Local(Context context, Local_DTO local_dto) {
        super(context);
        this.local_dto = local_dto;
        initView();
    }

    public View_Detalle_Local(Context context, AttributeSet attrs, Local_DTO local_dto) {
        super(context, attrs);
        this.local_dto = local_dto;
        initView();
    }

    public View_Detalle_Local(Context context, AttributeSet attrs, int defStyle, Local_DTO local_dto) {
        super(context, attrs, defStyle);
        this.local_dto = local_dto;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_ubicacion, this, true);

        try {
            JSONObject jsonObject = local_dto.getJsonObject();

            ((TextView) findViewById(R.id.txt_direccion)).setText(jsonObject.getString("Direccion"));

            String telefono = jsonObject.getString("Telefono");
            if(telefono.equals("0")){
                ((TextView) findViewById(R.id.txt_telefono)).setText("No disponible");
            }else{
                ((TextView) findViewById(R.id.txt_telefono)).setText(telefono);
            }

            String horario = jsonObject.getString("Horario");
            if(horario.equals("")){
                ((TextView) findViewById(R.id.txt_horario)).setText("No disponible");
            }else{
                ((TextView) findViewById(R.id.txt_horario)).setText(horario);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((TextView)findViewById(R.id.txt_direccion)).setTypeface(Util_Fonts.setPNALight(getContext()));
        ((TextView)findViewById(R.id.txt_telefono)).setTypeface(Util_Fonts.setPNALight(getContext()));
        ((TextView)findViewById(R.id.txt_horario)).setTypeface(Util_Fonts.setPNALight(getContext()));
    }
}
