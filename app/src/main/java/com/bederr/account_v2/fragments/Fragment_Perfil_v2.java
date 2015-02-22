package com.bederr.account_v2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.account_v2.dialogs.Update_Profile_Dialog;
import com.bederr.account_v2.interfaces.OnSuccessCounty;
import com.bederr.account_v2.interfaces.OnSuccessMe;
import com.bederr.account_v2.services.Service_Country;
import com.bederr.account_v2.views.Area_V;
import com.bederr.account_v2.views.Country_V;
import com.bederr.application.Maven_Application;
import com.bederr.beans_v2.Country_DTO;
import com.bederr.beans_v2.Location_DTO;
import com.bederr.beans_v2.Ubication_DTO;
import com.bederr.fragments.Fragment_Perfil;
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

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Perfil_v2 extends Fragment_Master implements Country_V.OnChecked, View.OnClickListener {

    private boolean flag = true;

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

                            /**
                             * Ciudad - Pais , Usuario
                             */
                            Location_DTO location = ((Maven_Application) getBederr().getApplication()).getLocation_dto();
                            if (location != null) {
                                ((TextView) getView().findViewById(R.id.txtciudadusuario))
                                        .setText(location.getPais() + " , " + location.getCiudad());
                                Picasso.with(getActivity()).load(location.getFlag()).into(((ImageView) getView().findViewById(R.id.imagen_pais)));
                            }
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


        boolean isLatLng = !getUbication().getLatitude().equals("0.0") && !getUbication().getLongitude().equals("0.0");
        if (!isLatLng) {
            getView().findViewById(R.id.txtciudadusuario).setOnClickListener(this);
        }

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
                update_profile_dialog.setActivity(getBederr());
                update_profile_dialog.show();
            }
        });

        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
    }

    @Override
    public void onChecked(boolean checked, Object tag, Country_V country_v, Area_V area_v) {
        Location_DTO location = new Location_DTO();
        location.setPais(area_v.getCountry_v().getCountry_dto().getName());
        location.setCiudad(area_v.getArea_dto().getName());
        location.setFlag(area_v.getCountry_v().getCountry_dto().getFlag_image());
        ((Maven_Application) getBederr().getApplication()).setLocation_dto(location);

        Ubication_DTO dto = new Ubication_DTO(null, area_v.getArea_dto().getId());
        ((Maven_Application) getBederr().getApplication()).setUbication(dto);

        getBederr().restart();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(final View view) {
        final LinearLayout ciudades = (LinearLayout) getView().findViewById(R.id.containerciudad);
        if (flag) {
            view.setOnClickListener(null);
            view.setClickable(false);

            Service_Country service_country = new Service_Country(getBederr());
            service_country.sendRequest();
            service_country.setOnSuccessCounty(new OnSuccessCounty() {
                @Override
                public void onSuccessCountry(boolean success,
                                             ArrayList<Country_DTO> country_dtos,
                                             String message) {
                    try {
                        if (success) {
                            for (int i = 0; i < country_dtos.size(); i++) {
                                Country_V country_v = new Country_V(getBederr(), country_dtos.get(i));
                                country_v.setOnChecked(Fragment_Perfil_v2.this);

                                if(country_v.getCountry_dto().getName().
                                        equals(((Maven_Application) getBederr().getApplication()).getLocation_dto().getPais())){
                                    country_v.setVisibility(View.GONE);
                                }

                                ciudades.addView(country_v);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    view.setOnClickListener(Fragment_Perfil_v2.this);
                    view.setClickable(true);
                }
            });
            ciudades.setVisibility(View.VISIBLE);
            flag = false;
        } else {
            ciudades.setVisibility(View.GONE);
            ciudades.removeAllViews();
            flag = true;
        }
    }
}
