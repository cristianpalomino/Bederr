package com.bederr.account_v2.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.beans_v2.Area_DTO;
import com.bederr.beans_v2.Area_DTO;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pe.bederr.com.R;

/**
 * Created by Gantz on 8/01/15.
 */
public class Area_V extends LinearLayout {

    private View view;
    private Area_DTO area_dto;

    private TextView area;
    private ImageView imgarea;

    private OnChecked onChecked;
    private boolean checked = false;

    private Country_V country_v;

    public void setOnChecked(OnChecked onChecked) {
        this.onChecked = onChecked;
    }

    public Area_V(Context context, Area_DTO area_dto) {
        super(context);
        this.area_dto = area_dto;
        initView();
    }

    public Area_V(Context context, AttributeSet attrs, Area_DTO area_dto) {
        super(context, attrs);
        this.area_dto = area_dto;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Area_V(Context context, AttributeSet attrs, int defStyle, Area_DTO area_dto) {
        super(context, attrs, defStyle);
        this.area_dto = area_dto;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_city, this, true);

        area = (TextView) view.findViewById(R.id.city);
        imgarea = (ImageView) view.findViewById(R.id.imgcity);

        area.setTypeface(Util_Fonts.setPNALight(getContext()));
        area.setText(area_dto.getName());

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (!checked) {
                        //Un - Checked
                        imgarea.setVisibility(VISIBLE);
                        checked = true;
                        onChecked.onChecked(true, getArea_dto().getId(),Area_V.this);
                    } else {
                        //Checked
                        imgarea.setVisibility(GONE);
                        checked = false;
                        onChecked.onChecked(false, getArea_dto().getId(),Area_V.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void setCountry_v(Country_V country_v) {
        this.country_v = country_v;
    }

    public Country_V getCountry_v() {
        return country_v;
    }

    public Area_DTO getArea_dto() {
        return area_dto;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked() {
        //Un - Checked
        imgarea.setVisibility(GONE);
        checked = false;
        //onChecked.onChecked(false,getArea_dto().getId());
    }

    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface OnChecked {
        public void onChecked(boolean checked, Object tag,Area_V area_v);
    }
}
