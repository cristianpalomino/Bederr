package com.bederr.views;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bederr.beans.Local_DTO;
import com.bederr.beans.Pregunta_DTO;
import com.bederr.main.Bederr;
import pe.bederr.com.R;
import com.bederr.database.Database_Maven;
import com.bederr.dialog.Dialog_Local_Pregunta;
import com.bederr.fragments.Fragment_Responder;
import com.bederr.session.Session_Manager;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gantz on 5/07/14.
 */
public class View_Local_Responder extends RelativeLayout implements View.OnClickListener {

    private Local_DTO local_dto;
    private Pregunta_DTO pregunta_dto;
    private Dialog_Local_Pregunta dialog_local_pregunta;

    private ActionBarActivity actionBarActivity;

    public View_Local_Responder(Context context, Local_DTO local_dto) {
        super(context);
        this.local_dto = local_dto;
        initView();
    }

    public View_Local_Responder(Context context, AttributeSet attrs, Local_DTO local_dto) {
        super(context, attrs);
        this.local_dto = local_dto;
        initView();
    }

    public View_Local_Responder(Context context, AttributeSet attrs, int defStyle, Local_DTO local_dto) {
        super(context, attrs, defStyle);
        this.local_dto = local_dto;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_local_responder, this, true);

        try {
            JSONObject jsonObject = local_dto.getJsonObject();

            ((TextView) findViewById(R.id.txt_nombre_local)).setText(jsonObject.getString("NombreLocal"));
            ((TextView) findViewById(R.id.txt_direccion_local)).setText(jsonObject.getString("Direccion"));
            ((TextView) findViewById(R.id.txt_categoria_local)).setText(jsonObject.getString("NombreCategoria"));

            //double distance = round(Double.parseDouble(jsonObject.getString("Distancia")), 2);
            //((TextView)findViewById(R.id.txt_distancia_local)).setText(String.valueOf(distance) + " mts.");
            ((TextView) findViewById(R.id.txt_distancia_local)).setVisibility(GONE);

            /*
            SET FONTS
             */
            ((TextView) findViewById(R.id.txt_nombre_local)).setTypeface(Util_Fonts.setPNASemiBold(getContext()));
            ((TextView) findViewById(R.id.txt_direccion_local)).setTypeface(Util_Fonts.setPNALight(getContext()));
            ((TextView) findViewById(R.id.txt_categoria_local)).setTypeface(Util_Fonts.setPNACursivaLight(getContext()));
            ((TextView) findViewById(R.id.txt_distancia_local)).setTypeface(Util_Fonts.setPNALight(getContext()));

            int resourceid = Util_Categorias.getImageCateogry(Integer.parseInt(jsonObject.getString("idCategoria")));
            Picasso.with(getContext()).load(resourceid).centerCrop().fit().transform(new RoundedTransformation(65, 0)).into(((ImageView) findViewById(R.id.img_categoria_local)));

            String logoempresa = jsonObject.getString("LogoEmpresa");
            if (!logoempresa.equals("")) {
                //Picasso.with(getContext()).load(resourceid).centerCrop().fit().transform(new RoundedTransformation(65, 0)).into(((ImageView) findViewById(R.id.img_categoria_local)));
            }

            JSONArray jsonArrayretail = jsonObject.getJSONArray("ListaCuponesRetail");
            JSONArray jsonArraylife = jsonObject.getJSONArray("ListaCuponesLife");

            if (new Session_Manager(getContext()).isLogin()) {
                Database_Maven database_maven = new Database_Maven(getContext());
                if(jsonArraylife.length() > 0){
                    if (!(database_maven.getAllEmpresaIds().size() == 0)) {
                        findViewById(R.id.img_cupon_plomo).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.img_cupon_plomo).setVisibility(View.GONE);
                    }
                }else{
                    findViewById(R.id.img_cupon_plomo).setVisibility(View.GONE);
                }
            }

            /*
            if(jsonArrayretail.length() < 0){
                findViewById(R.id.img_cupon_verde).setVisibility(View.GONE);
                findViewById(R.id.img_cupon_celeste).setVisibility(View.GONE);
            }
            else{
                for (int i = 0; i < jsonArrayretail.length(); i++) {
                    String beneficio = jsonArrayretail.getJSONObject(i).getString("Beneficio");
                    if(beneficio.equals("0")){
                        findViewById(R.id.img_cupon_verde).setVisibility(View.VISIBLE);
                    }else if(beneficio.equals("1")){
                        findViewById(R.id.img_cupon_celeste).setVisibility(View.GONE);
                    }else if(beneficio.equals("2")){
                        findViewById(R.id.img_cupon_plomo).setVisibility(View.VISIBLE);
                    }
                }
            }
            */
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dialog_local_pregunta.hide();
        ((Bederr)actionBarActivity).setLocal_dto(local_dto);
        actionBarActivity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b,R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Responder.newInstance(), "fragment_responder").addToBackStack(null).commit();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void setPregunta_dto(Pregunta_DTO pregunta_dto) {
        this.pregunta_dto = pregunta_dto;
    }

    public void setActionBarActivity(ActionBarActivity actionBarActivity) {
        this.actionBarActivity = actionBarActivity;
    }

    public void setDialog_local_pregunta(Dialog_Local_Pregunta dialog_local_pregunta) {
        this.dialog_local_pregunta = dialog_local_pregunta;
    }
}
