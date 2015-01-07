package com.bederr.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bederr.adapter.Adapter_Respuestas;
import com.bederr.beans.Local_DTO;
import com.bederr.beans.Pregunta_DTO;
import com.bederr.beans.Respuesta_DTO;
import com.bederr.main.Bederr;
import com.bederr.dialog.Dialog_Local_Pregunta_v2;
import com.bederr.session.Session_Manager;
import com.bederr.utils.PrettyTime;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Categorias;
import com.bederr.utils.Util_Fonts;

import pe.bederr.com.R;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Detalle_Pregunta_v2 extends Fragment_Master implements AdapterView.OnItemClickListener {

    protected Pregunta_DTO pregunta_dto;

    protected Adapter_Respuestas adapter_respuestas;
    protected ArrayList<Respuesta_DTO> respuesta_dtos = new ArrayList<Respuesta_DTO>();
    protected ListView lista_respuestas;

    public Fragment_Detalle_Pregunta_v2() {
        setId_layout(R.layout.fragment_detalle_pregunta_v2);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Detalle_Pregunta_v2 newInstance() {
        return new Fragment_Detalle_Pregunta_v2();
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

        lista_respuestas = (ListView)getView().findViewById(R.id.lista_respuestas);
        lista_respuestas.setOnItemClickListener(this);
        try {
            JSONArray jsonArray = pregunta_dto.getJsonObject().getJSONArray("respuestas");
            for (int i = 0; i < jsonArray.length(); i++) {
                respuesta_dtos.add(new Respuesta_DTO(jsonArray.getJSONObject(i)));
            }
            adapter_respuestas = new Adapter_Respuestas(getActivity(),respuesta_dtos);
            lista_respuestas.setAdapter(adapter_respuestas);
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
                trans.remove(Fragment_Detalle_Pregunta_v2.this);
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

            nombreusuario.setText(jsonObject.getString("NombreUsuario") + " preguntó:");
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
        Fragment_Detalle_Pregunta_v2.this.getView().setFocusableInTouchMode(true);
        Fragment_Detalle_Pregunta_v2.this.getView().requestFocus();
        Fragment_Detalle_Pregunta_v2.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Fragment_Detalle_Pregunta_v2.this);
                        trans.commit();
                        manager.popBackStack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Local_DTO local_dto = new Local_DTO(((Respuesta_DTO)parent.getItemAtPosition(position)).getJsonObject().getJSONObject("local"));
            getBederr().setLocal_dto(local_dto);
            getBederr().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b,R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Detalle_Local.newInstance(),Fragment_Detalle_Local.class.getName()).addToBackStack(null).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}