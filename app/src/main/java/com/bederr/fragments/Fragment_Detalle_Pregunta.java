package com.bederr.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bederr.beans.Respuesta_DTO;
import com.bederr.main.Bederr;
import com.bederr.utils.PrettyTime;
import com.bederr.beans.Pregunta_DTO;

import pe.bederr.com.R;

import com.bederr.dialog.Dialog_Local_Pregunta_v2;
import com.bederr.session.Session_Manager;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;
import com.bederr.views.View_Respuesta;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Detalle_Pregunta extends Fragment_Master {

    protected Pregunta_DTO pregunta_dto;

    public Fragment_Detalle_Pregunta() {
        setId_layout(R.layout.fragment_detalle_pregunta);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Detalle_Pregunta newInstance() {
        return new Fragment_Detalle_Pregunta();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        pregunta_dto = ((Bederr) getActivity()).getPregunta_dto();
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override
    protected void initView() {
        super.initView();
        initHeader();
        try {
            JSONArray jsonArray = pregunta_dto.getJsonObject().getJSONArray("respuestas");
            for (int i = 0; i < jsonArray.length(); i++) {
                View_Respuesta view_respuesta = new View_Respuesta(getActivity(), new Respuesta_DTO(jsonArray.getJSONObject(i)));
                getLayout().addView(view_respuesta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getLayout().removeView(getView_message());
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();

        ImageView action_left = (ImageView) getView().findViewById(R.id.action_left);
        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));

        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                trans.remove(Fragment_Detalle_Pregunta.this);
                trans.commit();
                manager.popBackStack();
            }
        });
    }

    private void initHeader() {
        try {
            JSONObject jsonObject = pregunta_dto.getJsonObject();

            TextView nombreusuario = (TextView) getView().findViewById(R.id.nombreusuario);
            TextView tiempopregunta = (TextView) getView().findViewById(R.id.tiempopregunta);
            TextView pregunta = (TextView) getView().findViewById(R.id.textopregunta);
            ImageView imagenusuario = (ImageView) getView().findViewById(R.id.imgusuariopregunta);
            ImageView imagencategoriapregunta = (ImageView) getView().findViewById(R.id.imgcategoriapregunta);
            Button btn_responder = (Button) getView().findViewById(R.id.btn_responder);

            int resourceid = Util_Categorias.getImageCateogry(Integer.parseInt(jsonObject.getString("idCategoria")));
            Picasso.with(getActivity()).load(resourceid).centerCrop().fit().transform(new RoundedTransformation(65, 0)).into(imagencategoriapregunta);

            if (jsonObject.getString("FacebookId") != null) {
                String str = "https://graph.facebook.com/" + jsonObject.getString("FacebookId") + "/picture";
                Picasso.with(getActivity()).load(str).placeholder(R.drawable.placeholder_usuario).centerCrop().fit().transform(new RoundedTransformation(75, 0)).into(imagenusuario);
            }

            /*
            SET FONTS
             */
            nombreusuario.setTypeface(Util_Fonts.setPNALight(getActivity()));
            tiempopregunta.setTypeface(Util_Fonts.setPNACursivaLight(getActivity()));
            pregunta.setTypeface(Util_Fonts.setPNALight(getActivity()));
            btn_responder.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));

            nombreusuario.setText(jsonObject.getString("NombreUsuario"));
            pregunta.setText(jsonObject.getString("pregunta"));
            Date localDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(jsonObject.getString("fecha_creacion"));
            tiempopregunta.setText(new PrettyTime(getActivity()).getTimeAgo(localDate));

            btn_responder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Session_Manager session_manager = new Session_Manager(getActivity());
                    if(session_manager.isLogin()){

                        final Dialog_Local_Pregunta_v2 dialog_local_pregunta_v2 = new Dialog_Local_Pregunta_v2(getActivity(),(Bederr) getActivity());
                        dialog_local_pregunta_v2.getWindow().setWindowAnimations(R.style.Dialog_Animation_DOWN_UP);
                        dialog_local_pregunta_v2.show();

                        /*
                        final Dialog_Local_Pregunta dialog_local_pregunta = new Dialog_Local_Pregunta(getActivity(), (Bederr) getActivity());
                        dialog_local_pregunta.getWindow().setWindowAnimations(R.style.Dialog_Animation_DOWN_UP);
                        dialog_local_pregunta.show();
                        */
                    }else{
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.abajo_arriba_b,R.animator.arriba_abajo_b).add(R.id.container, Fragment_Entrar.newInstance("1"),Fragment_Entrar.class.getName()).addToBackStack(null).commit();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Detalle_Pregunta.this.getView().setFocusableInTouchMode(true);
        Fragment_Detalle_Pregunta.this.getView().requestFocus();
        Fragment_Detalle_Pregunta.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Fragment_Detalle_Pregunta.this);
                        trans.commit();
                        manager.popBackStack();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}