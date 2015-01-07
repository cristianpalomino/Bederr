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
import android.widget.Toast;

import com.bederr.beans_v2.Benefit_Program_DTO;
import com.bederr.main.Bederr;
import com.bederr.benefits_v2.interfaces.OnSuccessDelete;

import pe.bederr.com.R;
import com.bederr.benefits_v2.fragments.Benefits_F;
import com.bederr.benefits_v2.fragments.Places_Programs_F;
import com.bederr.benefits_v2.services.Service_Programs;
import com.bederr.dialog.Dialog_Empresa;
import com.bederr.session.Session_Manager;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

/**
 * Created by Gantz on 20/07/14.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Benefit_Program_Corp_V extends LinearLayout implements View.OnClickListener, View.OnLongClickListener, Dialog_Empresa.Interface_Dialog_Empresa {

    private Benefit_Program_DTO benefit_program_dto;
    private TextView nombreempresa;
    private TextView descipcionempresa;
    private ImageView imagenempresa;
    private LinearLayout fondoempresa;

    public Benefit_Program_Corp_V(Context context, Benefit_Program_DTO benefit_program_dto) {
        super(context);
        this.benefit_program_dto = benefit_program_dto;
        initView();
    }

    public Benefit_Program_Corp_V(Context context, AttributeSet attrs, Benefit_Program_DTO benefit_program_dto) {
        super(context, attrs);
        this.benefit_program_dto = benefit_program_dto;
        initView();
    }

    public Benefit_Program_Corp_V(Context context, AttributeSet attrs, int defStyle, Benefit_Program_DTO benefit_program_dto) {
        super(context, attrs, defStyle);
        this.benefit_program_dto = benefit_program_dto;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.item_empresa, this, true);
        setOnClickListener(this);
        setOnLongClickListener(this);

        nombreempresa = (TextView) findViewById(R.id.nombreempresa);
        descipcionempresa = (TextView) findViewById(R.id.descripcionempresa);
        imagenempresa = (ImageView) findViewById(R.id.imgempresa);
        fondoempresa = (LinearLayout) findViewById(R.id.itemfondoempresa);

        nombreempresa.setText(benefit_program_dto.getName().toUpperCase());
        descipcionempresa.setText(benefit_program_dto.getDescription());
        descipcionempresa.setText(benefit_program_dto.getCompany_name());
        fondoempresa.setBackgroundColor(Color.parseColor("#" + benefit_program_dto.getColor()));

        String logo = "http://testv2.main.com/media/" + benefit_program_dto.getCompany_logo();
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
        ((Bederr) getContext()).setBenefit_program_dto(benefit_program_dto);
        ((Bederr) getContext()).
                getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).
                add(R.id.container, Places_Programs_F.newInstance(), Places_Programs_F.class.getName()).
                addToBackStack(null).commit();
    }

    @Override
    public boolean onLongClick(View v) {
        Dialog_Empresa dialog_empresa = new Dialog_Empresa(getContext());
        dialog_empresa.getWindow().setWindowAnimations(R.style.Dialog_Animation_DOWN_UP);
        dialog_empresa.setInterface_dialog_empresa(Benefit_Program_Corp_V.this);
        dialog_empresa.show();
        return true;
    }

    public void isClickable(boolean clickable) {
        if (clickable) {
            setOnClickListener(this);
            setOnLongClickListener(this);
        }
    }

    @Override
    public void onAcepted(final Dialog_Empresa dialog_cupon) {
        String token = new Session_Manager(getContext()).getUserToken();
        Service_Programs service_programs = new Service_Programs(getContext());
        service_programs.sendRequestDelete(token, String.valueOf(benefit_program_dto.getId()));
        service_programs.setOnSuccessDelete(new OnSuccessDelete() {
            @Override
            public void onSuccessDelete(boolean success, String message) {
                dialog_cupon.hide();
                Toast.makeText(getContext(), message , Toast.LENGTH_SHORT).show();
                updateView(success);
            }
        });
    }

    public void updateView(boolean state) {
        if (state) {
            ((Bederr) getContext()).getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.container, Benefits_F.newInstance(), Benefits_F.class.getName()).
                    commit();
        }
    }
}
