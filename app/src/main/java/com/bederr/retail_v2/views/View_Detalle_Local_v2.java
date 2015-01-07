package com.bederr.retail_v2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bederr.beans_v2.Place_DTO;
import pe.bederr.com.R;
import com.bederr.utils.Util_Fonts;

/**
 * Created by Gantz on 5/07/14.
 */
public class View_Detalle_Local_v2 extends RelativeLayout {

    private Place_DTO place_dto;
    private TextView direccion;
    private TextView telefono;
    private TextView horario;

    public View_Detalle_Local_v2(Context context, Place_DTO place_dto) {
        super(context);
        this.place_dto = place_dto;
        initView();
    }

    public View_Detalle_Local_v2(Context context, AttributeSet attrs, Place_DTO place_dto) {
        super(context, attrs);
        this.place_dto = place_dto;
        initView();
    }

    public View_Detalle_Local_v2(Context context, AttributeSet attrs, int defStyle, Place_DTO place_dto) {
        super(context, attrs, defStyle);
        this.place_dto = place_dto;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_ubicacion, this, true);

        direccion = (TextView) findViewById(R.id.txt_direccion);
        telefono = (TextView) findViewById(R.id.txt_telefono);
        horario = (TextView) findViewById(R.id.txt_horario);

        direccion.setText(place_dto.getAddress());
        telefono.setText(place_dto.getTelephone());
        horario.setText(place_dto.getMf_hours_from() + " - " + place_dto.getMf_hours_to());

        direccion.setTypeface(Util_Fonts.setPNALight(getContext()));
        telefono.setTypeface(Util_Fonts.setPNALight(getContext()));
        horario.setTypeface(Util_Fonts.setPNALight(getContext()));
    }
}
