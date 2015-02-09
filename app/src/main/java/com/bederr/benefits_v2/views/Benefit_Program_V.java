package com.bederr.benefits_v2.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bederr.beans_v2.Benefit_Program_DTO;
import com.bederr.main.Bederr;
import com.bederr.benefits_v2.fragments.Benefits_F;

import pe.bederr.com.R;
import com.bederr.benefits_v2.dialog.Life_Dialog;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

/**
 * Created by Gantz on 20/07/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Benefit_Program_V extends LinearLayout implements View.OnClickListener,
                                                               Life_Dialog.Interface_Dialog_Life {

    private Benefit_Program_DTO benefit_program_dto;
    private TextView nombreempresa;
    private TextView descipcionempresa;
    private ImageView imagenempresa;
    private LinearLayout fondoempresa;

    public Benefit_Program_V(Context context, Benefit_Program_DTO benefit_program_dto) {
        super(context);
        this.benefit_program_dto = benefit_program_dto;
        initView();
    }

    public Benefit_Program_V(Context context, AttributeSet attrs, Benefit_Program_DTO benefit_program_dto) {
        super(context, attrs);
        this.benefit_program_dto = benefit_program_dto;
        initView();
    }

    public Benefit_Program_V(Context context, AttributeSet attrs, int defStyle, Benefit_Program_DTO benefit_program_dto) {
        super(context, attrs, defStyle);
        this.benefit_program_dto = benefit_program_dto;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.item_empresa, this, true);
        setOnClickListener(this);

        nombreempresa = (TextView) findViewById(R.id.nombreempresa);
        descipcionempresa = (TextView) findViewById(R.id.descripcionempresa);
        imagenempresa = (ImageView) findViewById(R.id.imgempresa);
        fondoempresa = (LinearLayout) findViewById(R.id.itemfondoempresa);

        nombreempresa.setText(benefit_program_dto.getName().toUpperCase());
        descipcionempresa.setText(benefit_program_dto.getDescription());
        descipcionempresa.setText(benefit_program_dto.getCompany_name());
        fondoempresa.setBackgroundColor(Color.parseColor("#" + benefit_program_dto.getColor()));

        String logo = benefit_program_dto.getCompany_logo();
        //String logo = "";
        Picasso.with(getContext()).
                load(logo).
                centerCrop().
                fit().
                placeholder(R.drawable.placeholder_empresa).
                transform(new RoundedTransformation(65, 0)).
                into(imagenempresa);

        nombreempresa.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
        descipcionempresa.setTypeface(Util_Fonts.setPNALight(getContext()));
    }

    @Override
    public void onClick(View v) {
        Life_Dialog life_dialog = new Life_Dialog(getContext(),benefit_program_dto);
        life_dialog.setActivity((Bederr)getContext());
        life_dialog.getWindow().setWindowAnimations(R.style.Dialog_Animation_UP_DOWN);
        life_dialog.setInterface_dialog_life(this);
        life_dialog.show();
    }

    @Override
    public void updateView(boolean state) {
        if (state) {
            ((Bederr) getContext()).sm_empresas.toggle();
            ((Bederr) getContext()).getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.container, Benefits_F.newInstance(), Benefits_F.class.getName()).
                    commit();
        }
    }
}
