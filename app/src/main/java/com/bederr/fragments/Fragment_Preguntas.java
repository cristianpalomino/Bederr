package com.bederr.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bederr.beans.Local_DTO;
import com.bederr.beans.Pregunta_DTO;
import com.bederr.main.Bederr;
import com.bederr.dialog.Dialog_Categoria;
import com.bederr.operations.Operation_Locales_Cercanos;
import com.bederr.operations.Operation_Preguntas;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;
import com.bederr.views.View_Pregunta;

import pe.bederr.com.R;

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Preguntas extends Fragment_Master {

    private int page = 1;

    public Fragment_Preguntas() {
        setId_layout(R.layout.fragment_preguntas);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Preguntas newInstance() {
        return new Fragment_Preguntas();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        getBederr().setActive_fragment(Fragment_Beneficios.newInstance());
        return super.onCreateView(inflater, viewGroup, bundle);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override
    protected void initView() {
        super.initView();
        Operation_Preguntas operation_preguntas = new Operation_Preguntas(getActivity());
        operation_preguntas.getPreguntas(page);
        operation_preguntas.setInterface_obtener_preguntas(new Operation_Preguntas.Interface_Obtener_Preguntas() {
            @Override
            public void getPreguntas(boolean status, final ArrayList<Pregunta_DTO> pregunta_dtos, String mensaje) {
                try {
                    if (status) {
                        try {
                            Runnable r = new Runnable() {
                                public void run() {
                                    for (int i = 0; i < pregunta_dtos.size(); i++) {
                                        View_Pregunta view_pregunta = new View_Pregunta(getActivity(), pregunta_dtos.get(i));
                                        view_pregunta.setTag(pregunta_dtos.get(i));
                                        getLayout().addView(view_pregunta);
                                    }
                                }
                            };
                            getLayout().post(r);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        /*
        ScrollViewExt scrollViewExt = (ScrollViewExt) getView().findViewById(R.id.scrollview);
        scrollViewExt.setScrollViewListener(new ScrollViewExt.ScrollViewListener() {
            @Override
            public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy) {
                View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
                Log.e("SCROLL","" + diff);
                if (diff == 0) {
                    page++;
                    Log.e("SCROLL","ESTOY EN EL FINAL " + diff);
                }
            }
        });
        */
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();
        ImageView action_left = (ImageView) getView().findViewById(R.id.action_left);
        Button action_middle = (Button) getView().findViewById(R.id.btn_action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));

        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Bederr) getActivity()).sm_menu.toggle();
            }
        });

        action_middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session_Manager session_manager = new Session_Manager(getActivity());
                if (session_manager.isLogin()) {
                    Dialog_Categoria dialog_categoria = new Dialog_Categoria(getActivity(), (Bederr) getActivity());
                    dialog_categoria.getWindow().setWindowAnimations(R.style.Dialog_Animation_UP_DOWN);
                    dialog_categoria.show();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b).add(R.id.container, Fragment_Entrar.newInstance("1"), Fragment_Entrar.class.getName()).addToBackStack(null).commit();
                }
            }
        });
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        Operation_Locales_Cercanos operation_locales_cercanos = new Operation_Locales_Cercanos(getActivity());
        operation_locales_cercanos.setPage(1);
        operation_locales_cercanos.getLocalesCercanos();
        operation_locales_cercanos.setInterface_operation_locales_cercanos(new Operation_Locales_Cercanos.Interface_Operation_Locales_Cercanos() {
            @Override
            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages) {
                try {
                    ((Bederr)activity).setLocal_dtos(local_dtos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
