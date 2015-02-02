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

    private boolean checked = false;

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
                if(checked){
                    imgarea.setVisibility(VISIBLE);
                    checked = false;
                }else{
                    imgarea.setVisibility(GONE);
                    checked = true;
                }
            }
        });
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
