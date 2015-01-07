package com.bederr.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bederr.beans.Empresa_DTO;
import com.bederr.main.Bederr;
import pe.bederr.com.R;
import com.bederr.dialog.Dialog_Life;
import com.bederr.fragments.Fragment_Beneficios;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gantz on 20/07/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class View_Empresa extends LinearLayout implements View.OnClickListener, Dialog_Life.Interface_Dialog_Life {

    private Empresa_DTO empresa_dto;

    public View_Empresa(Context context, Empresa_DTO empresa_dto) {
        super(context);
        this.empresa_dto = empresa_dto;
        initView();
    }

    public View_Empresa(Context context, AttributeSet attrs, Empresa_DTO empresa_dto) {
        super(context, attrs);
        this.empresa_dto = empresa_dto;
        initView();
    }

    public View_Empresa(Context context, AttributeSet attrs, int defStyle, Empresa_DTO empresa_dto) {
        super(context, attrs, defStyle);
        this.empresa_dto = empresa_dto;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.item_empresa, this, true);
        setOnClickListener(this);
        try {
            JSONObject jsonObject = empresa_dto.getJsonObject();

            TextView nombreempresa = (TextView) findViewById(R.id.nombreempresa);
            TextView descipcionempresa = (TextView) findViewById(R.id.descripcionempresa);
            ImageView imagenempresa = (ImageView) findViewById(R.id.imgempresa);
            LinearLayout fondoempresa = (LinearLayout) findViewById(R.id.itemfondoempresa);

            nombreempresa.setText(jsonObject.getString("Nombre"));
            descipcionempresa.setText("No existe");
            //descipcionempresa.setText(jsonObject.getString("subtitle"));
            fondoempresa.setBackgroundColor(Color.parseColor("#" + jsonObject.getString("background_android")));

            String urlempresa = jsonObject.getString("Logo");
            Log.e("EMPRESAS",urlempresa);

            if (urlempresa != null) {
                Picasso.with(getContext()).load(urlempresa).centerCrop().fit().placeholder(R.drawable.placeholder_empresa).transform(new RoundedTransformation(65, 0)).into(imagenempresa);
            }

            nombreempresa.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
            descipcionempresa.setTypeface(Util_Fonts.setPNALight(getContext()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Dialog_Life dialog_life = new Dialog_Life(getContext(), empresa_dto);
        dialog_life.getWindow().setWindowAnimations(R.style.Dialog_Animation_UP_DOWN);
        dialog_life.setInterface_dialog_life(this);
        dialog_life.show();
    }

    @Override
    public void updateView(boolean state) {
        if (state) {
            ((Bederr) getContext()).sm_empresas.toggle();
            ((Bederr) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.container, Fragment_Beneficios.newInstance(), Fragment_Beneficios.class.getName()).commit();
        }
    }
}
