package com.bederr.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bederr.main.Bederr;
import com.bederr.dialog.Dialog_Contrasenia;
import com.bederr.beans.Usuario_DTO;

import pe.bederr.com.R;

import com.bederr.dialog.Dialog_Editar_Perfil;
import com.bederr.session.Session_Manager;
import com.bederr.utils.RoundedTransformation;
import com.bederr.utils.Util_Fonts;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Perfil extends Fragment_Master {

    private ImageView action_right;

    public Fragment_Perfil() {
        setId_layout(R.layout.fragment_perfil);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Perfil newInstance() {
        return new Fragment_Perfil();
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
        Session_Manager session_manager = new Session_Manager(getActivity());
        try {
            Usuario_DTO usuario_dto = new Usuario_DTO();
            usuario_dto.setJsonObject(session_manager.getSession().getJsonObject());
            ((TextView) getView().findViewById(R.id.txtnombreusuario)).setText(usuario_dto.getJsonObject().getString("nombre"));
            ((TextView) getView().findViewById(R.id.txtsexousuario)).setText(usuario_dto.getJsonObject().getString("sexo"));
            ((TextView) getView().findViewById(R.id.txtcorreousuario)).setText(usuario_dto.getJsonObject().getString("Email"));
            ((TextView) getView().findViewById(R.id.txtcumpleaniosusuario)).setText(usuario_dto.getJsonObject().getString("nacimiento"));

            if (usuario_dto.getJsonObject().getString("FacebookId") != null) {
                String str = "https://graph.facebook.com/" + usuario_dto.getJsonObject().getString("FacebookId") + "/picture";
                Picasso.with(getActivity()).load(str).placeholder(R.drawable.placeholder_usuario).transform(new RoundedTransformation(75, 0)).into(((ImageView) getView().findViewById(R.id.img_perfil_usuario)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


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

        Fragment_Perfil.this.onFinishLoad(getView());
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

        Session_Manager session_manager = new Session_Manager(getBederr());
        if (session_manager.getSessionType() == 0) {
            ImageView action_right = (ImageView) getView().findViewById(R.id.action_right);
            action_right.setVisibility(View.VISIBLE);
            action_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog_Editar_Perfil dialog_editar_perfil = new Dialog_Editar_Perfil(getActivity());
                    dialog_editar_perfil.getWindow().setWindowAnimations(R.style.Dialog_Animation_UP_DOWN);
                    dialog_editar_perfil.show();
                }
            });
        }

        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
    }
}
