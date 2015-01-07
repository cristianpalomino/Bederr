package com.bederr.account_v2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.account_v2.dialogs.Update_Profile_Dialog;
import com.bederr.account_v2.interfaces.OnSuccessMe;
import com.bederr.main.Bederr;
import com.bederr.dialog.Dialog_Contrasenia;
import com.bederr.fragments.Fragment_Master;
import com.bederr.account_v2.services.Service_Me;
import com.bederr.beans_v2.User_DTO;

import pe.bederr.com.R;

import com.bederr.session.Session_Manager;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Perfil_v2 extends Fragment_Master {

    private ImageView action_right;

    public Fragment_Perfil_v2() {
        setId_layout(R.layout.fragment_perfil);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Perfil_v2 newInstance() {
        return new Fragment_Perfil_v2();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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
        initStyles();
        getView().findViewById(R.id.cerrarsessionusuario).setVisibility(View.GONE);

        final Session_Manager session_manager = new Session_Manager(getActivity());
        Service_Me service_me = new Service_Me(getBederr());
        service_me.sendRequest(session_manager.getUserToken());
        service_me.setOnSuccessMe(new OnSuccessMe() {
            @Override
            public void onSuccessMe(boolean success, User_DTO user_dto) {
                try {
                    if (success) {
                        session_manager.setUserJson(user_dto.getDataSource().toString());
                        getView().findViewById(R.id.cerrarsessionusuario).setVisibility(View.VISIBLE);
                        if (success) {
                            ((TextView) getView().findViewById(R.id.txtnombreusuario)).setText(user_dto.getFirst_name());
                            ((TextView) getView().findViewById(R.id.txtsexousuario)).setText(user_dto.getLast_name());
                            ((TextView) getView().findViewById(R.id.txtcorreousuario)).setText(user_dto.getEmail());
                            ((TextView) getView().findViewById(R.id.txtcumpleaniosusuario)).setText(user_dto.getBirthday());
                            Picasso.with(getActivity()).load(user_dto.getPhoto()).placeholder(R.drawable.placeholder_usuario).transform(new RoundedTransformation(75, 0)).into(((ImageView) getView().findViewById(R.id.img_perfil_usuario)));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailRequest(boolean success, String message) {
                Toast.makeText(getBederr(), message, Toast.LENGTH_SHORT).show();
            }
        });

        if (session_manager.getSessionType() == 0) {
            getView().findViewById(R.id.cambiar_contrasenia_password).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.txtlogeofacebookusuario).setVisibility(View.GONE);
            getView().findViewById(R.id.txtlogeonosotrosususario).setVisibility(View.VISIBLE);
        } else if (session_manager.getSessionType() == 1) {
            getView().findViewById(R.id.cambiar_contrasenia_password).setVisibility(View.GONE);
            getView().findViewById(R.id.txtlogeofacebookusuario).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.txtlogeonosotrosususario).setVisibility(View.GONE);
        }

        getView().findViewById(R.id.cerrarsessionusuario).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Session_Manager(getActivity()).cerrarSession();
            }
        });

        getView().findViewById(R.id.cambiar_contrasenia_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Contrasenia dialog_contrasenia = new Dialog_Contrasenia(getActivity());
                dialog_contrasenia.getWindow().setWindowAnimations(R.style.Dialog_Animation_UP_DOWN);
                dialog_contrasenia.show();
            }
        });

        Fragment_Perfil_v2.this.onFinishLoad(getView());
    }

    private void initStyles() {
        ((TextView) getView().findViewById(R.id.txtnombreusuario)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((TextView) getView().findViewById(R.id.txtsexousuario)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((TextView) getView().findViewById(R.id.txtcorreousuario)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((TextView) getView().findViewById(R.id.txtcumpleaniosusuario)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((TextView) getView().findViewById(R.id.txtgeneralusuario)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        ((TextView) getView().findViewById(R.id.txtciudadusuario)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((TextView) getView().findViewById(R.id.txtlogeofacebookusuario)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((TextView) getView().findViewById(R.id.txtlogeonosotrosususario)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((TextView) getView().findViewById(R.id.txtcompartirusuario)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        ((TextView) getView().findViewById(R.id.txtconfacebookusuario)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((TextView) getView().findViewById(R.id.contwitterusuario)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((TextView) getView().findViewById(R.id.txtseguridadusuario)).setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        ((TextView) getView().findViewById(R.id.cambiar_contrasenia_password)).setTypeface(Util_Fonts.setPNALight(getActivity()));
        ((TextView) getView().findViewById(R.id.cerrarsessionusuario)).setTypeface(Util_Fonts.setPNALight(getActivity()));
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        ImageView action_left = (ImageView) getView().findViewById(R.id.action_left);
        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Bederr) getActivity()).sm_menu.toggle();
            }
        });

        ImageView action_right = (ImageView) getView().findViewById(R.id.action_right);
        action_right.setVisibility(View.VISIBLE);
        action_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update_Profile_Dialog update_profile_dialog = new Update_Profile_Dialog(getActivity());
                update_profile_dialog.getWindow().setWindowAnimations(R.style.Dialog_Animation_UP_DOWN);
                update_profile_dialog.show();
            }
        });

        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
    }
}
