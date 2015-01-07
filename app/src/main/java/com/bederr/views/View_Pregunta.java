package com.bederr.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bederr.beans.Pregunta_DTO;
import com.bederr.main.Bederr;
import com.bederr.fragments.Fragment_Detalle_Pregunta;
import com.bederr.utils.PrettyTime;

import pe.bederr.com.R;

import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gantz on 5/07/14.
 */
public class View_Pregunta extends RelativeLayout implements View.OnClickListener{

    private Pregunta_DTO pregunta_dto;

    public View_Pregunta(Context context, Pregunta_DTO pregunta_dto) {
        super(context);
        this.pregunta_dto = pregunta_dto;
        initView();
    }

    public View_Pregunta(Context context, AttributeSet attrs, Pregunta_DTO pregunta_dto) {
        super(context, attrs);
        this.pregunta_dto = pregunta_dto;
        initView();
    }

    public View_Pregunta(Context context, AttributeSet attrs, int defStyle, Pregunta_DTO pregunta_dto) {
        super(context, attrs, defStyle);
        this.pregunta_dto = pregunta_dto;
        initView();
    }

    private void initView(){
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_pregunta,this,true);

        try {
            JSONObject jsonObject = pregunta_dto.getJsonObject();

            TextView nombreusuario = (TextView)findViewById(R.id.nombreusuario);
            TextView tiempopregunta = (TextView)findViewById(R.id.tiempopregunta);
            TextView cantidadrespuesta = (TextView)findViewById(R.id.cantidadrespuesta);
            TextView pregunta = (TextView)findViewById(R.id.textopregunta);

            ImageView imagenusuario = (ImageView)findViewById(R.id.imgusuariopregunta);
            ImageView imagencategoriapregunta = (ImageView)findViewById(R.id.imgcategoriapregunta);

            int resourceid = Util_Categorias.getImageCateogry(Integer.parseInt(jsonObject.getString("idCategoria")));
            Picasso.with(getContext()).load(resourceid).centerCrop().fit().transform(new RoundedTransformation(65, 0)).into(imagencategoriapregunta);

            if (jsonObject.getString("FacebookId") != null){
                String str = "https://graph.facebook.com/" + jsonObject.getString("FacebookId") + "/picture";
                Picasso.with(getContext()).load(str).placeholder(R.drawable.placeholder_usuario).centerCrop().fit().transform(new RoundedTransformation(75, 0)).into(imagenusuario);
            }

            /*
            SET FONTS
             */
            nombreusuario.setTypeface(Util_Fonts.setPNALight(getContext()));
            tiempopregunta.setTypeface(Util_Fonts.setPNACursivaLight(getContext()));
            pregunta.setTypeface(Util_Fonts.setPNALight(getContext()));
            cantidadrespuesta.setTypeface(Util_Fonts.setPNACursivaLight(getContext()));

            nombreusuario.setText(jsonObject.getString("NombreUsuario") + " preguntò:");

            Date localDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(jsonObject.getString("fecha_creacion"));
            tiempopregunta.setText(new PrettyTime(getContext()).getTimeAgo(localDate));

            cantidadrespuesta.setText(jsonObject.getString("totalRespuestas") + " respuesta(s).");
            pregunta.setText(jsonObject.getString("pregunta"));



        }catch (Exception e){
            e.printStackTrace();
        }

        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ((Bederr)getContext()).setPregunta_dto(pregunta_dto);
        ((Bederr)getContext()).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b,R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Detalle_Pregunta.newInstance(),Fragment_Detalle_Pregunta.class.getName()).addToBackStack(null).commit();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void hideContainerRespuestas(){
        findViewById(R.id.container_view_respuestas).setVisibility(GONE);
    }
}
