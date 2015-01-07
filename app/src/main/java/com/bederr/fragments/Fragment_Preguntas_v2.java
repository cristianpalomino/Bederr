package com.bederr.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bederr.adapter.Adapter_Preguntas;
import com.bederr.main.Bederr;
import com.bederr.dialog.Dialog_Categoria;
import com.bederr.beans.Pregunta_DTO;

import pe.bederr.com.R;

import com.bederr.operations.Operation_Preguntas;
import com.bederr.session.Session_Manager;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Preguntas_v2 extends Fragment_Master implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private ListView lista_preguntas;
    protected Adapter_Preguntas adapter_preguntas;

    protected int page = 1;
    private boolean isLoading = false;

    public Fragment_Preguntas_v2() {
        setId_layout(R.layout.fragment_preguntas_v2);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Preguntas_v2 newInstance() {
        return new Fragment_Preguntas_v2();
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

        lista_preguntas = (ListView) getView().findViewById(R.id.lista_preguntas);
        lista_preguntas.setOnItemClickListener(this);
        lista_preguntas.setOnScrollListener(this);
        lista_preguntas.setVisibility(View.GONE);

        Operation_Preguntas operation_preguntas = new Operation_Preguntas(getActivity());
        operation_preguntas.getPreguntas(page);
        operation_preguntas.setInterface_obtener_preguntas(new Operation_Preguntas.Interface_Obtener_Preguntas() {
            @Override
            public void getPreguntas(boolean status, final ArrayList<Pregunta_DTO> pregunta_dtos, String mensaje) {
                try {
                    if (status) {
                        adapter_preguntas = new Adapter_Preguntas(getActivity(), pregunta_dtos);
                        lista_preguntas.setAdapter(adapter_preguntas);
                        Fragment_Preguntas_v2.this.onFinishLoad(lista_preguntas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
        /*
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
        */
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
        Pregunta_DTO pregunta_dto = (Pregunta_DTO) parent.getItemAtPosition(position);
        ((Bederr) getActivity()).setPregunta_dto(pregunta_dto);
        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Detalle_Pregunta_v2.newInstance(), Fragment_Detalle_Pregunta.class.getName()).addToBackStack(null).commit();
    }

    /**
     * Callback method to be invoked while the list view or grid view is being scrolled. If the
     * view is being scrolled, this method will be called before the next frame of the scroll is
     * rendered. In particular, it will be called before any calls to
     *
     * @param view        The view whose scroll state is being reported
     * @param scrollState The current scroll state. One of
     *                    {@link #SCROLL_STATE_TOUCH_SCROLL} or {@link #SCROLL_STATE_IDLE}.
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /**
     * Callback method to be invoked when the list or grid has been scrolled. This will be
     * called after the scroll has completed
     *
     * @param view             The view whose scroll state is being reported
     * @param firstVisibleItem the index of the first visible cell (ignore if
     *                         visibleItemCount == 0)
     * @param visibleItemCount the number of visible cells
     * @param totalItemCount   the number of items in the list adaptor
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
            if (!isLoading) {
                isLoading = true;
                page++;
                loadMoreDAta(page);
            }
        }
    }

    private void loadMoreDAta(int page) {
        Operation_Preguntas operation_preguntas = new Operation_Preguntas(getActivity());
        operation_preguntas.getPreguntas(page);
        operation_preguntas.setInterface_obtener_preguntas(new Operation_Preguntas.Interface_Obtener_Preguntas() {
            @Override
            public void getPreguntas(boolean status, final ArrayList<Pregunta_DTO> pregunta_dtos, String mensaje) {
                try {
                    for (int i = 0; i < pregunta_dtos.size(); i++) {
                        adapter_preguntas.add(pregunta_dtos.get(i));
                    }
                    isLoading = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
