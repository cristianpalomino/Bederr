package com.bederr.retail_v2.fragments;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bederr.beans_v2.Qr_DTO;
import com.bederr.fragments.Fragment_Master;
import com.bederr.beans_v2.Offer_DTO;

import pe.bederr.com.R;
import com.bederr.benefits_v2.interfaces.OnSuccessOffer;
import com.bederr.benefits_v2.services.Service_Offer;
import com.bederr.dialog.Dialog_Cupon;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

/**
 * Created by Gantz on 16/07/14.
 */
public class Offer_Detail_F extends Fragment_Master implements Dialog_Cupon.Interface_Dialog_Cupon {

    private Offer_DTO offer_dto;
    private String nombre_local;

    private TextView txt_tipo_beneficio;
    private TextView txtnombrecupon;
    private TextView txtdescripcioncupon;
    private TextView txtdetallecupon;
    private TextView txtcondiciones;
    private LinearLayout frame;
    private Button btnusar;
    private ImageView imageqr;

    private int type;

    public Offer_Detail_F() {
        setId_layout(R.layout.fragment_detalle_cupon);
        setId_container(R.id.frame_container);
    }

    public static final Offer_Detail_F newInstance(int type) {
        Offer_Detail_F offer_detail_f = new Offer_Detail_F();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        offer_detail_f.setArguments(bundle);
        return offer_detail_f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
        offer_dto = getBederr().getOffer_dto();
        nombre_local = getBederr().getPlace_dto().getName();
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
        frame = (LinearLayout)getView().findViewById(R.id.card_tipo_cupon);
        btnusar = (Button)getView().findViewById(R.id.btnusar);
        imageqr = (ImageView) getView().findViewById(R.id.imgqr);

        if (type == 0) {
            txt_tipo_beneficio.setText("Oferta en Tienda");
            txtdetallecupon.setText(offer_dto.getTitle());
            frame.setBackgroundResource(R.drawable.holo_flat_button_azul_claro);
        } else if (type == 1) {
            txt_tipo_beneficio.setText("Oferta Especial");
            txtdetallecupon.setText(offer_dto.getTitle());
            frame.setBackgroundResource(R.drawable.holo_flat_button_verde_claro);
        }


        if(offer_dto.isValidate_use()){
            btnusar.setVisibility(View.VISIBLE);
        }else{
            btnusar.setVisibility(View.VISIBLE);
        }

        txtnombrecupon.setText(offer_dto.getTitle());
        txtdescripcioncupon.setText(offer_dto.getContent());
        txtcondiciones.setText(offer_dto.getTerms());

            /*
            SET FONTS
             */
        txt_tipo_beneficio.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        txtnombrecupon.setTypeface(Util_Fonts.setPNALight(getActivity()));
        txtdescripcioncupon.setTypeface(Util_Fonts.setPNALight(getActivity()));
        txtdetallecupon.setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((TextView) getView().findViewById(R.id.txt_terminos_y_condiciones_text)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        btnusar.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        txtcondiciones.setTypeface(Util_Fonts.setPNALight(getActivity()));

        getView().findViewById(R.id.btnusar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Cupon dialog_cupon = new Dialog_Cupon(getActivity());
                dialog_cupon.getWindow().setWindowAnimations(R.style.Dialog_Animation_UP_DOWN);
                dialog_cupon.setInterface_dialog_cupon(Offer_Detail_F.this);
                dialog_cupon.show();
            }
        });

        Offer_Detail_F.this.onFinishLoad(getView());
    }

    @Override
    public void onAcepted(Dialog_Cupon dialog_cupon) {
        dialog_cupon.hide();

        Session_Manager session_manager = new Session_Manager(getBederr());
        String token = session_manager.getUserToken();
        String in_place_offer = String.valueOf(offer_dto.getId());
        String purchase_corporate_offer = "";

        Service_Offer service_offer = new Service_Offer(getBederr());
        service_offer.sendRequest(token,in_place_offer,purchase_corporate_offer);
        service_offer.setOnSuccessOffer(new OnSuccessOffer() {
            @Override
            public void onSuccessOffer(boolean success, Qr_DTO qr_dto) {
                try {
                    if(success){
                        getView().findViewById(R.id.txt_terminos_y_condiciones_text).setVisibility(View.GONE);
                        getView().findViewById(R.id.txt_terminos_y_condiciones).setVisibility(View.GONE);
                        ((TextView) getView().findViewById(R.id.txtcontador)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
                        getView().findViewById(R.id.txtcontador).setVisibility(View.VISIBLE);
                        imageqr.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.abajo_arriba));
                        getView().findViewById(R.id.txtcontador).startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.abajo_arriba));

                        String link = "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data="+qr_dto.getCode();
                        Picasso.with(getBederr()).
                                load(link).
                                into(imageqr);

                        imageqr.setVisibility(View.VISIBLE);
                        btnusar.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
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
                trans.remove(Offer_Detail_F.this);
                trans.commit();
                manager.popBackStack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Offer_Detail_F.this.getView().setFocusableInTouchMode(true);
        Offer_Detail_F.this.getView().requestFocus();
        Offer_Detail_F.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b);
                        trans.remove(Offer_Detail_F.this);
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
