package com.bederr.retail_v2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bederr.beans_v2.Place_DTO;
import com.bederr.main.Bederr;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;

import pe.bederr.com.R;

import com.squareup.picasso.Picasso;

/**
 * Created by Gantz on 5/07/14.
 */
public class View_Local_v2 extends RelativeLayout {

    private Place_DTO place_dto;

    private TextView nombre;
    private TextView direccion;
    private TextView categoria;
    private TextView distancia;
    private ImageView image;

    private ImageView img_verde;
    private ImageView img_azul;
    private ImageView img_plomo;

    private String option;

    public View_Local_v2(Context context, Place_DTO place_dto,String option) {
        super(context);
        this.place_dto = place_dto;
        this.option = option;
        initView();
    }

    public View_Local_v2(Context context, AttributeSet attrs, Place_DTO place_dto,String option) {
        super(context, attrs);
        this.place_dto = place_dto;
        this.option = option;
        initView();
    }

    public View_Local_v2(Context context, AttributeSet attrs, int defStyle, Place_DTO place_dto,String option) {
        super(context, attrs, defStyle);
        this.place_dto = place_dto;
        this.option = option;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_local, this, true);

        nombre = (TextView) findViewById(R.id.txt_nombre_local);
        direccion = (TextView) findViewById(R.id.txt_direccion_local);
        categoria = (TextView) findViewById(R.id.txt_categoria_local);
        distancia = (TextView) findViewById(R.id.txt_distancia_local);
        image = (ImageView) findViewById(R.id.img_categoria_local);


        img_verde = (ImageView) findViewById(R.id.img_cupon_verde);
        img_azul = (ImageView) findViewById(R.id.img_cupon_celeste);
        img_plomo = (ImageView) findViewById(R.id.img_cupon_plomo);

        nombre.setText(place_dto.getName());
        direccion.setText(place_dto.getAddress());
        categoria.setText(place_dto.getCategory_name());

        if(!place_dto.getDistance().equals("NULL")) {
            try {
                int metros = round(Double.parseDouble(place_dto.getDistance()), 0);
                String mtrs = "";
                if (metros < 1000) {
                    mtrs = String.valueOf(round(Double.parseDouble(place_dto.getDistance()), 0));
                    distancia.setText(mtrs + " Mts.");
                } else {
                    Double kms = Double.parseDouble(place_dto.getDistance()) / 1000;
                    kms = roundKM(kms, 1);
                    if (kms == 1.0 || kms == 2.0 || kms == 3.0 || kms == 4.0 || kms == 5.0) {
                        distancia.setText(kms.intValue() + " Kms.");
                    } else {
                        distancia.setText(kms + " Kms.");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                distancia.setVisibility(GONE);
            }
        }else{
            distancia.setVisibility(GONE);
        }

        nombre.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        direccion.setTypeface(Util_Fonts.setPNALight(getContext()));
        categoria.setTypeface(Util_Fonts.setPNACursivaLight(getContext()));
        distancia.setTypeface(Util_Fonts.setPNALight(getContext()));

        Picasso.with(getContext()).
                load(Util_Categorias.getImageCategory(place_dto.getCategory_name())).
                centerCrop().
                fit().
                transform(new RoundedTransformation(65, 0)).
                into(image);


        if(option.equals("Explore"))
        {
            if(place_dto.getInplace_offers().size() > 0){
                img_azul.setVisibility(View.VISIBLE);
            }

            if(place_dto.getSpecial_offers().size() > 0){
                img_verde.setVisibility(View.VISIBLE);
            }

            if(place_dto.getCorporate_offers().size() > 0){
                img_plomo.setVisibility(View.VISIBLE);
            }

            if(place_dto.getLegacy_offers().size() > 0){
                img_plomo.setVisibility(View.VISIBLE);
            }
        }
        else if(option.equals("Benefits"))
        {
            if(place_dto.getCorporate_offers().size() > 0){
                img_plomo.setVisibility(View.VISIBLE);
            }

            if(place_dto.getLegacy_offers().size() > 0){
                img_plomo.setVisibility(View.VISIBLE);
            }
        }
    }

    public static double roundKM(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static int round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (int) Math.round((double) tmp / factor);
    }

    public void hideSoftKeyboard() {
        if (((Bederr) getContext()).getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Bederr) getContext()).getCurrentFocus().getWindowToken(), 0);
        }
    }
}
