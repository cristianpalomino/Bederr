package com.bederr.retail_v2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bederr.beans_v2.Offer_DTO;
import com.bederr.main.Bederr;
import com.bederr.retail_v2.fragments.Offer_Detail_F;

import pe.bederr.com.R;

import com.bederr.utils.Util_Fonts;

/**
 * Created by Gantz on 5/07/14.
 */
public class Offer_V extends RelativeLayout implements View.OnClickListener {

    private Offer_DTO offer_dto;
    private String nombre_local;
    private int type;

    private TextView nombre;
    private TextView descripcion;
    private LinearLayout frame;

    public Offer_V(Context context, Offer_DTO offer_dto,int type) {
        super(context);
        this.offer_dto = offer_dto;
        this.type = type;
        initView();
    }

    public Offer_V(Context context, AttributeSet attrs, Offer_DTO offer_dto,int type) {
        super(context, attrs);
        this.offer_dto = offer_dto;
        this.type = type;
        initView();
    }

    public Offer_V(Context context, AttributeSet attrs, int defStyle, Offer_DTO offer_dto,int type) {
        super(context, attrs, defStyle);
        this.offer_dto = offer_dto;
        this.type = type;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_cupon, this, true);
        setTag(offer_dto);
        setOnClickListener(this);

        nombre = (TextView) findViewById(R.id.txt_nombre_cupon);
        descripcion = (TextView) findViewById(R.id.txt_descripcion_cupon);
        frame = (LinearLayout) findViewById(R.id.card_tipo_cupon);

        nombre.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        descripcion.setTypeface(Util_Fonts.setPNALight(getContext()));

        if (type == 0) {
            nombre.setText("Oferta en Tienda");
            descripcion.setText(offer_dto.getTitle());
            frame.setBackgroundResource(R.drawable.holo_flat_button_azul_claro);
        } else if (type == 1) {
            nombre.setText("Oferta Especial");
            descripcion.setText(offer_dto.getTitle());
            frame.setBackgroundResource(R.drawable.holo_flat_button_verde_claro);
        }

        findViewById(R.id.img_empresa_cupon).setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        ((Bederr) getContext()).setOffer_dto(offer_dto);
        ((Bederr) getContext()).getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.animator.abajo_arriba, R.animator.arriba_abajo).
                add
                        (
                                R.id.container, Offer_Detail_F.newInstance(type),
                                Offer_Detail_F.class.getName()
                        ).
                addToBackStack(null).
                commit();
    }

    public void setNombre_local(String nombre_local) {
        this.nombre_local = nombre_local;
    }
}
