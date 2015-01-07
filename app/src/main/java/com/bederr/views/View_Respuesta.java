package com.bederr.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bederr.beans.Local_DTO;
import com.bederr.beans.Respuesta_DTO;
import com.bederr.main.Bederr;
import com.bederr.utils.PrettyTime;

import pe.bederr.com.R;
import com.bederr.fragments.Fragment_Detalle_Local;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gantz on 5/07/14.
 */
public class View_Respuesta extends RelativeLayout implements View.OnClickListener {

    private Respuesta_DTO respuesta_dto;

    public View_Respuesta(Context context, Respuesta_DTO respuesta_dto) {
        super(context);
        this.respuesta_dto = respuesta_dto;
        initView();
    }

    public View_Respuesta(Context context, AttributeSet attrs, Respuesta_DTO respuesta_dto) {
        super(context, attrs);
        this.respuesta_dto = respuesta_dto;
        initView();
    }

    public View_Respuesta(Context context, AttributeSet attrs, int defStyle, Respuesta_DTO respuesta_dto) {
        super(context, attrs, defStyle);
        this.respuesta_dto = respuesta_dto;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_respuesta, this, true);

        try {
            JSONObject jsonObject = respuesta_dto.getJsonObject();

            JSONObject localObject = jsonObject.getJSONObject("local");
            Local_DTO local_dto = new Local_DTO(localObject);

            TextView nombreusuario = (TextView) findViewById(R.id.nombreusuario);
            TextView tiempopregunta = (TextView) findViewById(R.id.tiempopregunta);
            TextView texto_respuesta = (TextView) findViewById(R.id.texto_respuesta);

            TextView nombre_local = (TextView) findViewById(R.id.nombre_local);
            TextView direccion_local = (TextView) findViewById(R.id.direccion_local);
            TextView categoria_local = (TextView) findViewById(R.id.categoria_local);

            ImageView imagenusuario = (ImageView) findViewById(R.id.imgusuariopregunta);
            ImageView img_categoria_local = (ImageView) findViewById(R.id.img_categoria_local);

            int resourceid = Util_Categorias.getImageCateogry(Integer.parseInt(localObject.getString("idCategoria")));
            Picasso.with(getContext()).load(resourceid).centerCrop().fit().transform(new RoundedTransformation(65, 0)).into(img_categoria_local);

            if (jsonObject.getString("FacebookId") != null) {
                String str = "https://graph.facebook.com/" + jsonObject.getString("FacebookId") + "/picture";
                Picasso.with(getContext()).load(str).placeholder(R.drawable.placeholder_usuario).transform(new RoundedTransformation(75, 0)).into(imagenusuario);
            }

            /**
             * Tipos de Cupon
             */
            ImageView img_verde = (ImageView) findViewById(R.id.img_cupon_verde);
            ImageView img_azul = (ImageView) findViewById(R.id.img_cupon_celeste);
            ImageView img_plomo = (ImageView) findViewById(R.id.img_cupon_plomo);

            img_azul.setVisibility(local_dto.isAzul());
            img_verde.setVisibility(local_dto.isVerde());

            JSONArray jsonArraylife = local_dto.getJsonObject().getJSONArray("ListaCuponesLife");

            if(jsonArraylife.length() > 0){
                img_plomo.setVisibility(local_dto.isPlomo(getContext()));
            }else{
                img_plomo.setVisibility(local_dto.isPlomo(getContext()));
            }

            /*
            SET FONTS
             */
            nombreusuario.setTypeface(Util_Fonts.setPNALight(getContext()));
            tiempopregunta.setTypeface(Util_Fonts.setPNACursivaLight(getContext()));
            texto_respuesta.setTypeface(Util_Fonts.setPNALight(getContext()));
            nombre_local.setTypeface(Util_Fonts.setPNASemiBold(getContext()));
            direccion_local.setTypeface(Util_Fonts.setPNALight(getContext()));
            categoria_local.setTypeface(Util_Fonts.setPNALight(getContext()));

            nombre_local.setText(localObject.getString("NombreLocal"));
            direccion_local.setText(localObject.getString("Direccion"));
            categoria_local.setText(localObject.getString("NombreCategoria"));
            nombreusuario.setText(jsonObject.getString("NombreUsuario") + " respondió:");
            Date localDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(jsonObject.getString("date_register"));
            tiempopregunta.setText(new PrettyTime(getContext()).getTimeAgo(localDate));
            texto_respuesta.setText(jsonObject.getString("respuesta"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            Local_DTO local_dto = new Local_DTO(respuesta_dto.getJsonObject().getJSONObject("local"));
            ((Bederr)getContext()).setLocal_dto(local_dto);
            ((Bederr)getContext()).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b,R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Detalle_Local.newInstance(),Fragment_Detalle_Local.class.getName()).addToBackStack(null).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
