package com.bederr.retail_v2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bederr.beans_v2.CorporateOffer_DTO;
import com.bederr.main.Bederr;

import pe.bederr.com.R;
import com.bederr.retail_v2.fragments.Offer_Corporate_Detail_F;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

/**
 * Created by Gantz on 5/07/14.
 */
public class Offer_Corporate_V extends RelativeLayout implements View.OnClickListener {

    private CorporateOffer_DTO corporateOffer_dto;
    private String nombre_local;
    private int type;

    private TextView nombre;
    private TextView descripcion;
    private LinearLayout frame;

    public Offer_Corporate_V(Context context, CorporateOffer_DTO corporateOffer_dto, int type) {
        super(context);
        this.corporateOffer_dto = corporateOffer_dto;
        this.type = type;
        initView();
    }

    public Offer_Corporate_V(Context context, AttributeSet attrs, CorporateOffer_DTO corporateOffer_dto, int type) {
        super(context, attrs);
        this.corporateOffer_dto = corporateOffer_dto;
        this.type = type;
        initView();
    }

    public Offer_Corporate_V(Context context, AttributeSet attrs, int defStyle, CorporateOffer_DTO corporateOffer_dto, int type) {
        super(context, attrs, defStyle);
        this.corporateOffer_dto = corporateOffer_dto;
        this.type = type;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_cupon, this, true);
        setOnClickListener(this);

        nombre = (TextView) findViewById(R.id.txt_nombre_cupon);
        descripcion = (TextView) findViewById(R.id.txt_descripcion_cupon);
        frame = (LinearLayout) findViewById(R.id.card_tipo_cupon);

        nombre.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        descripcion.setTypeface(Util_Fonts.setPNALight(getContext()));
        nombre.setTextColor(getResources().getColor(R.color.color_letra));
        descripcion.setTextColor(getResources().getColor(R.color.color_letra));

        nombre.setText("Beneficio Corporativo");
        descripcion.setText(corporateOffer_dto.getTitle());
        frame.setBackgroundResource(R.drawable.holo_flat_button_plomo_claro);

        /*
        if (type == 0) {
            nombre.setText("Beneficio Corporativo");
            descripcion.setText(corporateOffer_dto.getTitle());
            frame.setBackgroundResource(R.drawable.holo_flat_button_plomo_claro);
        } else if (type == 1) {
            nombre.setText("Beneficio Corporativo");
            descripcion.setText(corporateOffer_dto.getTitle());
            frame.setBackgroundResource(R.drawable.holo_flat_button_plomo_claro);
        }
        */

        String urlempresa = "";
        //String urlempresa = corporateOffer_dto.getCompany_logo();
        if (!urlempresa.toString().matches("")) {
            Picasso.with(getContext()).load(urlempresa).placeholder(R.drawable.placeholder_empresa).error(R.drawable.placeholder_empresa).centerCrop().fit().transform(new RoundedTransformation(75, 0)).into(((ImageView) findViewById(R.id.img_empresa_cupon)));
        }
    }

    @Override
    public void onClick(View v) {
        ((Bederr) getContext()).setCorporateOffer_dto(corporateOffer_dto);
        ((Bederr) getContext()).getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.animator.abajo_arriba, R.animator.arriba_abajo).
                add
                        (
                                R.id.container, Offer_Corporate_Detail_F.newInstance(type),
                                Offer_Corporate_Detail_F.class.getName()
                        ).
                addToBackStack(null).
                commit();
    }

    public void setNombre_local(String nombre_local) {
        this.nombre_local = nombre_local;
    }
}
