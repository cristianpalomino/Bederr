package com.bederr.retail_v2.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bederr.beans_v2.CorporateOffer_DTO;
import com.bederr.dialog.Dialog_Cupon;
import com.bederr.fragments.Fragment_Master;
import com.bederr.utils.Util_Fonts;

import pe.bederr.com.R;

/**
 * Created by Gantz on 16/07/14.
 */
public class Offer_Corporate_Detail_F extends Fragment_Master implements Dialog_Cupon.Interface_Dialog_Cupon {

    private CorporateOffer_DTO corporateOffer_dto;
    private String nombre_local;
    private TextView txt_tipo_beneficio;
    private TextView txtnombrecupon;
    private TextView txtdescripcioncupon;
    private TextView txtdetallecupon;
    private TextView txtcondiciones;
    private LinearLayout frame;
    private Button btnusar;
    private int type;


    public Offer_Corporate_Detail_F() {
        setId_layout(R.layout.fragment_detalle_cupon_life);
        setId_container(R.id.frame_container);
    }

    public static final Offer_Corporate_Detail_F newInstance(int type) {
        Offer_Corporate_Detail_F offer_corporate_detail_f = new Offer_Corporate_Detail_F();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        offer_corporate_detail_f.setArguments(bundle);
        return offer_corporate_detail_f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        corporateOffer_dto = getBederr().getCorporateOffer_dto();
        nombre_local = getBederr().getPlace_dto().getName();
        type = getArguments().getInt("type");
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

        txt_tipo_beneficio = (TextView) getView().findViewById(R.id.txt_tipo_beneficio);
        txtnombrecupon = (TextView) getView().findViewById(R.id.txt_nombre_cupon);
        txtdescripcioncupon = (TextView) getView().findViewById(R.id.txt_detalle_cupon);
        txtdetallecupon = (TextView) getView().findViewById(R.id.txt_descripcion_cupon);
        txtcondiciones = (TextView) getView().findViewById(R.id.txt_terminos_y_condiciones);
        frame = (LinearLayout) getView().findViewById(R.id.card_tipo_cupon);
        btnusar = (Button) getView().findViewById(R.id.btnusar);

        txt_tipo_beneficio.setText("Beneficio Corporativo");
        txtdetallecupon.setText(corporateOffer_dto.getTitle());
        txtcondiciones.setText(corporateOffer_dto.getTerms());

        frame.setBackgroundResource(R.drawable.holo_flat_button_plomo_claro);

        txt_tipo_beneficio.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        txtnombrecupon.setTypeface(Util_Fonts.setPNALight(getActivity()));
        txtdescripcioncupon.setTypeface(Util_Fonts.setPNALight(getActivity()));
        txtdetallecupon.setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((TextView) getView().findViewById(R.id.txt_terminos_y_condiciones_text)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        btnusar.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        txtcondiciones.setTypeface(Util_Fonts.setPNALight(getActivity()));

        if(corporateOffer_dto.isValidate_use()){
            btnusar.setVisibility(View.VISIBLE);
        }else{
            btnusar.setVisibility(View.GONE);
        }

        Offer_Corporate_Detail_F.this.onFinishLoad(getView());
    }

    @Override
    public void onAcepted(Dialog_Cupon dialog_cupon) {
        dialog_cupon.hide();

        getView().findViewById(R.id.txt_terminos_y_condiciones_text).setVisibility(View.GONE);
        getView().findViewById(R.id.txt_terminos_y_condiciones).setVisibility(View.GONE);

        ((TextView) getView().findViewById(R.id.txtcontador)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        getView().findViewById(R.id.txtcontador).setVisibility(View.VISIBLE);
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
                trans.remove(Offer_Corporate_Detail_F.this);
                trans.commit();
                manager.popBackStack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Offer_Corporate_Detail_F.this.getView().setFocusableInTouchMode(true);
        Offer_Corporate_Detail_F.this.getView().requestFocus();
        Offer_Corporate_Detail_F.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b);
                        trans.remove(Offer_Corporate_Detail_F.this);
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
