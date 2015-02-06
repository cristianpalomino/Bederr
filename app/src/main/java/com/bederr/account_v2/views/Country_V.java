package com.bederr.account_v2.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.beans_v2.Area_DTO;
import com.bederr.beans_v2.Country_DTO;
import com.bederr.beans_v2.Offer_DTO;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.bederr.com.R;

/**
 * Created by Gantz on 8/01/15.
 */
public class Country_V extends LinearLayout implements Area_V.OnChecked {

    private View view;
    private Country_DTO country_dto;

    private TextView pais;
    private ImageView imagenpais;
    private LinearLayout container;

    private OnChecked onChecked;

    public void setOnChecked(OnChecked onChecked) {
        this.onChecked = onChecked;
    }

    public Country_V(Context context,Country_DTO country_dto) {
        super(context);
        this.country_dto=country_dto;
        initView();
    }

    public Country_V(Context context, AttributeSet attrs,Country_DTO country_dto) {
        super(context, attrs);
        this.country_dto=country_dto;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Country_V(Context context, AttributeSet attrs, int defStyle,Country_DTO country_dto) {
        super(context, attrs, defStyle);
        this.country_dto=country_dto;
        initView();
    }

    private void initView(){
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_country, this, true);

        pais = (TextView) view.findViewById(R.id.pais);
        imagenpais = (ImageView) view.findViewById(R.id.imgpais);
        container = (LinearLayout) view.findViewById(R.id.containerarea);

        pais.setText(country_dto.getName());
        pais.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        Picasso.with(getContext()).load(country_dto.getFlag_image()).into(imagenpais);

        ArrayList<Area_DTO> area_dtos = country_dto.getAreas();
        for (int i = 0; i < area_dtos.size() ; i++) {
            Area_V area_v = new Area_V(getContext(),area_dtos.get(i));
            area_v.setOnChecked(this);
            container.addView(area_v);
        }
    }

    @Override
    public void onChecked(boolean checked,Object tag) {
        onChecked.onChecked(checked,tag);
    }

    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface OnChecked{
        public void onChecked(boolean checked,Object tag);
    }

    public ArrayList<Area_V> getAreas(){
        ArrayList<Area_V> areas = new ArrayList<Area_V>();
        for (int i = 0; i < container.getChildCount(); i++) {
            Area_V area_v = (Area_V) container.getChildAt(i);
            areas.add(area_v);
        }

        return areas;
    }
}
