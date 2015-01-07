package com.bederr.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import pe.bederr.com.R;
import com.bederr.utils.Util_Fonts;

public class View_Message extends FrameLayout {

    private int type_message;
    protected View view;

    public View_Message(Context context, int type_message) {
        super(context);
        this.type_message = type_message;
        initView(type_message);
    }

    public View_Message(Context context, AttributeSet paramAttributeSet, int type_message) {
        super(context, paramAttributeSet);
        this.type_message = type_message;
        initView(type_message);
    }

    public View_Message(Context context, AttributeSet paramAttributeSet, int type_message1, int type_message2) {
        super(context, paramAttributeSet, type_message1);
        this.type_message = type_message2;
        initView(type_message2);
    }

    private void initView(int type_message) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.view_message, this, true);

        LinearLayout frame_no_data = (LinearLayout) this.view.findViewById(R.id.frame_message_no_data);
        LinearLayout frame_no_gps = (LinearLayout) this.view.findViewById(R.id.frame_message_no_gps);
        LinearLayout frame_no_wifi = (LinearLayout) this.view.findViewById(R.id.frame_message_no_wifi);
        LinearLayout frame_cargando = (LinearLayout) this.view.findViewById(R.id.frame_message_cargando);

        TextView txt_message_title_cargando = (TextView) this.view.findViewById(R.id.text_type_message_cargando);
        TextView txt_message_title_no_data = (TextView) this.view.findViewById(R.id.text_type_message_no_data);
        TextView txt_message_title_no_wifi = (TextView) this.view.findViewById(R.id.text_type_message_no_wifi);
        TextView txt_message_title_no_gps = (TextView) this.view.findViewById(R.id.text_type_message_no_gps);
        TextView txt_message_des_no_data = (TextView) this.view.findViewById(R.id.text_type_message_desc_no_data);
        TextView txt_message_des_no_wifi = (TextView) this.view.findViewById(R.id.text_type_message_desc_no_wifi);
        TextView txt_message_des_no_gps = (TextView) this.view.findViewById(R.id.text_type_message_desc_no_gps);

        frame_no_data.setVisibility(GONE);
        frame_no_gps.setVisibility(GONE);
        frame_no_wifi.setVisibility(GONE);
        frame_cargando.setVisibility(GONE);

        txt_message_title_cargando.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        txt_message_title_no_data.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        txt_message_title_no_wifi.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        txt_message_title_no_gps.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        txt_message_des_no_data.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        txt_message_des_no_wifi.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        txt_message_des_no_gps.setTypeface(Util_Fonts.setPNASemiBold(getContext()));

        if (type_message == 0) {
            frame_no_data.setVisibility(GONE);
            frame_no_gps.setVisibility(GONE);
            frame_no_wifi.setVisibility(GONE);
            frame_cargando.setVisibility(VISIBLE);
        }
        if (type_message == 1) {
            frame_no_data.setVisibility(GONE);
            frame_no_gps.setVisibility(GONE);
            frame_no_wifi.setVisibility(VISIBLE);
            frame_cargando.setVisibility(GONE);
        }
        if (type_message == 2) {
            frame_no_data.setVisibility(GONE);
            frame_no_gps.setVisibility(VISIBLE);
            frame_no_wifi.setVisibility(GONE);
            frame_cargando.setVisibility(GONE);
        }
        if (type_message == 3) {
            frame_no_data.setVisibility(VISIBLE);
            frame_no_gps.setVisibility(GONE);
            frame_no_wifi.setVisibility(GONE);
            frame_cargando.setVisibility(GONE);
        }
    }
}