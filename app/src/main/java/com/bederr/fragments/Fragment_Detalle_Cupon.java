package com.bederr.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bederr.beans.Cupon_DTO;

import pe.bederr.com.R;
import com.bederr.dialog.Dialog_Cupon;
import com.bederr.utils.Util_Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gantz on 16/07/14.
 */
public class Fragment_Detalle_Cupon extends Fragment_Master implements Dialog_Cupon.Interface_Dialog_Cupon {

    private Cupon_DTO cupon_dto;
    private String nombre_local;

    public Fragment_Detalle_Cupon() {
        setId_layout(R.layout.fragment_detalle_cupon);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Detalle_Cupon newInstance() {
        return new Fragment_Detalle_Cupon();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cupon_dto = getBederr().getCupon_dto();
        try {
            nombre_local = getBederr().getLocal_dto().getJsonObject().getString("NombreLocal");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(inflater, viewGroup, bundle);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override
    protected void initView() {
        super.initView();

        Fragment_Detalle_Cupon.this.onFinishLoad(getView());
        try {
            TextView txt_tipo_beneficio = (TextView) getView().findViewById(R.id.txt_tipo_beneficio);
            TextView txtnombrecupon = (TextView) getView().findViewById(R.id.txt_nombre_cupon);
            TextView txtdescripcioncupon = (TextView) getView().findViewById(R.id.txt_detalle_cupon);
            TextView txtdetallecupon = (TextView) getView().findViewById(R.id.txt_descripcion_cupon);
            TextView txtcondiciones = (TextView) getView().findViewById(R.id.txt_terminos_y_condiciones);

            JSONObject jsonObject = cupon_dto.getJsonObject();

            String tipoBeneficio = jsonObject.getString("Beneficio");
            if (tipoBeneficio.equals("1")) {
                txt_tipo_beneficio.setText("Oferta Especial");
                txtdetallecupon.setText(jsonObject.getString("Nombre"));
                getView().findViewById(R.id.card_tipo_cupon).setBackgroundResource(R.drawable.holo_flat_button_verde_claro);
                getView().findViewById(R.id.btnusar).setVisibility(View.VISIBLE);
            } else if (tipoBeneficio.equals("0")) {
                txt_tipo_beneficio.setText("Oferta en Tienda");
                txtdetallecupon.setText(jsonObject.getString("Nombre"));
                getView().findViewById(R.id.card_tipo_cupon).setBackgroundResource(R.drawable.holo_flat_button_azul_claro);
                getView().findViewById(R.id.btnusar).setVisibility(View.GONE);

            }

            txtnombrecupon.setText(jsonObject.getString("Nombre"));
            txtdescripcioncupon.setText(jsonObject.getString("Descripcion"));

            JSONArray jsonArray = jsonObject.getJSONArray("Condiciones");
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < jsonArray.length(); i++) {
                stringBuffer.append(jsonArray.getString(i) + "\n \n");
            }
            txtcondiciones.setText(stringBuffer);

            /*
            SET FONTS
             */
            txt_tipo_beneficio.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
            txtnombrecupon.setTypeface(Util_Fonts.setPNALight(getActivity()));
            txtdescripcioncupon.setTypeface(Util_Fonts.setPNALight(getActivity()));
            txtdetallecupon.setTypeface(Util_Fonts.setPNALight(getActivity()));
            ((TextView) getView().findViewById(R.id.txt_terminos_y_condiciones_text)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
            ((Button) getView().findViewById(R.id.btnusar)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
            txtcondiciones.setTypeface(Util_Fonts.setPNALight(getActivity()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getView().findViewById(R.id.btnusar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Cupon dialog_cupon = new Dialog_Cupon(getActivity());
                dialog_cupon.getWindow().setWindowAnimations(R.style.Dialog_Animation_UP_DOWN);
                dialog_cupon.setInterface_dialog_cupon(Fragment_Detalle_Cupon.this);
                dialog_cupon.show();
            }
        });
    }

    @Override
    public void onAcepted(Dialog_Cupon dialog_cupon) {
        dialog_cupon.hide();

        getView().findViewById(R.id.txt_terminos_y_condiciones_text).setVisibility(View.GONE);
        getView().findViewById(R.id.txt_terminos_y_condiciones).setVisibility(View.GONE);

        ((TextView) getView().findViewById(R.id.txtcontador)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        getView().findViewById(R.id.txtcontador).setVisibility(View.VISIBLE);

        getView().findViewById(R.id.imgqr).startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.abajo_arriba));
        getView().findViewById(R.id.txtcontador).startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.abajo_arriba));

        getView().findViewById(R.id.imgqr).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.btnusar).setVisibility(View.GONE);
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();

        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        action_middle.setText(nombre_local);

        ImageView action_left = (ImageView) getView().findViewById(R.id.action_right);
        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b);
                trans.remove(Fragment_Detalle_Cupon.this);
                trans.commit();
                manager.popBackStack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Detalle_Cupon.this.getView().setFocusableInTouchMode(true);
        Fragment_Detalle_Cupon.this.getView().requestFocus();
        Fragment_Detalle_Cupon.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b);
                        trans.remove(Fragment_Detalle_Cupon.this);
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
