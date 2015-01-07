package com.bederr.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bederr.adapter.Adapter_Listas;
import com.bederr.beans.Lista_DTO;
import com.bederr.main.Bederr;

import pe.bederr.com.R;
import com.bederr.operations.Operation_Listas;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 30/09/14.
 */
public class Fragment_Listas extends Fragment_Master implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    protected ListView lista_listas;
    protected Adapter_Listas adapter_listas;
    
    protected int page = 1;
    protected boolean isLoading = false;

    public Fragment_Listas() {
        setId_layout(R.layout.fragment_listas);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Listas newInstance() {
        return new Fragment_Listas();
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

        lista_listas = (ListView) getView().findViewById(R.id.lista_listas);
        lista_listas.setOnItemClickListener(this);
        lista_listas.setOnScrollListener(this);
        lista_listas.setVisibility(View.GONE);


        Operation_Listas operation_listas = new Operation_Listas(getActivity());
        operation_listas.getListas(page);
        operation_listas.setInterface_operation_listas(new Operation_Listas.Interface_Operation_listas() {
            @Override
            public void getlistas(boolean status, ArrayList<Lista_DTO> lista_dtos) {
                if (status) {
                    try {
                        adapter_listas = new Adapter_Listas(getActivity(), lista_dtos);
                        lista_listas.setAdapter(adapter_listas);
                        Fragment_Listas.this.onFinishLoad(lista_listas);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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

        TextView action_middle = (TextView) getView().findViewById(R.id.action_middle);
        action_middle.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
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
        Lista_DTO lista_dto = (Lista_DTO) parent.getItemAtPosition(position);

        final View mview = lista_listas.getAdapter().getView(position,null,lista_listas);
        lista_dto.setView_lista(mview);

        getBederr().setLista_dto(lista_dto);
        getBederr().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b,R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Detalle_Listas.newInstance(),Fragment_Detalle_Listas.class.getName()).addToBackStack(null).commit();
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
        Operation_Listas operation_listas = new Operation_Listas(getActivity());
        operation_listas.getListas(page);
        operation_listas.setInterface_operation_listas(new Operation_Listas.Interface_Operation_listas() {
            @Override
            public void getlistas(boolean status, ArrayList<Lista_DTO> lista_dtos) {
                if (status) {
                    try {
                        for (int i = 0; i < lista_dtos.size(); i++) {
                            adapter_listas.add(lista_dtos.get(i));
                        }
                        isLoading = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
