package com.bederr.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bederr.adapter.Adapter_Locales;
import com.bederr.beans.Categoria_DTO;
import com.bederr.beans.Local_DTO;
import com.bederr.main.Bederr;
import com.bederr.operations.Operation_Busquedas;
import com.bederr.operations.Operation_Locales_Cercanos;

import pe.bederr.com.R;

import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;

/**
 * Created by Gantz on 5/07/14.
 */
public class Fragment_Resultado_Busquedas extends Fragment_Master implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private String s;
    private String distrito;
    private boolean isLoading = false;
    private int page = 1;

    private ArrayList<Local_DTO> local_dtos;
    protected Adapter_Locales adapter_locales;
    protected ListView lista_locales_busquedas;

    public Fragment_Resultado_Busquedas() {
        setId_layout(R.layout.fragment_resultado_busquedas);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Resultado_Busquedas newInstance(String s, String distrito) {
        Fragment_Resultado_Busquedas fragment_resultado_busquedas = new Fragment_Resultado_Busquedas();
        Bundle bundle = new Bundle();
        bundle.putString("s", s);
        bundle.putString("distrito", distrito);
        fragment_resultado_busquedas.setArguments(bundle);
        return fragment_resultado_busquedas;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        s = getArguments().getString("s");
        distrito = getArguments().getString("distrito");
        hideSoftKeyboard();
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

        TextView txt_criterio = (TextView) getView().findViewById(R.id.txtcriterio);
        txt_criterio.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));

        if (s.equals("") || distrito.equals("")) {
            txt_criterio.setText(s + distrito);
        } else {
            txt_criterio.setText(s + " - " + distrito);
        }

        txt_criterio.setVisibility(View.VISIBLE);
        txt_criterio.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.abajo_arriba_b));

        lista_locales_busquedas = (ListView) getView().findViewById(R.id.lista_locales_busquedas);
        lista_locales_busquedas.setOnItemClickListener(this);
        lista_locales_busquedas.setOnScrollListener(this);
        lista_locales_busquedas.setVisibility(View.GONE);

        buscar(s, distrito);
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
                trans.remove(Fragment_Resultado_Busquedas.this);
                trans.commit();
                manager.popBackStack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Resultado_Busquedas.this.getView().setFocusableInTouchMode(true);
        Fragment_Resultado_Busquedas.this.getView().requestFocus();
        Fragment_Resultado_Busquedas.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.izquierda_derecha, R.animator.derecha_izquierda);
                        trans.remove(Fragment_Resultado_Busquedas.this);
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
        Local_DTO local_dto = (Local_DTO) parent.getItemAtPosition(position);
        ((Bederr) getActivity()).setLocal_dto(local_dto);
        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Detalle_Local.newInstance(), Fragment_Detalle_Local.class.getName()).addToBackStack(null).commit();
    }

    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Callback method to be invoked while the list view or grid view is being scrolled. If the
     * view is being scrolled, this method will be called before the next frame of the scroll is
     * rendered. In particular, it will be called before any calls to
     * {@link Adapter#getView(int, android.view.View, android.view.ViewGroup)}.
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

    private boolean isCategory(String s) {
        for (int i = 0; i < getCategorias().size(); i++) {
            if (s.equals(getCategorias().get(i).getNombrecategoria())) {
                return true;
            }
        }
        return false;
    }


    private ArrayList<Categoria_DTO> getCategorias() {
        ArrayList<Categoria_DTO> categoria_dtos = new ArrayList<Categoria_DTO>();
        categoria_dtos.add(new Categoria_DTO("Beneficio", "", R.drawable.categoria_beneficio, false, 10));
        categoria_dtos.add(new Categoria_DTO("Descuento", "", R.drawable.categoria_descuento, false, 6));
        categoria_dtos.add(new Categoria_DTO("Comida", "", R.drawable.categoria_comida, false, 2));
        categoria_dtos.add(new Categoria_DTO("Ropa", "", R.drawable.categoria_ropa, false, 4));
        categoria_dtos.add(new Categoria_DTO("Salud", "", R.drawable.categoria_salud, false, 9));
        categoria_dtos.add(new Categoria_DTO("Bar", "", R.drawable.categoria_bar, false, 3));
        categoria_dtos.add(new Categoria_DTO("Diversión", "", R.drawable.categoria_entretenimiento, false, 1));
        categoria_dtos.add(new Categoria_DTO("Markets", "", R.drawable.categoria_markets, false, 7));
        categoria_dtos.add(new Categoria_DTO("Viajes", "", R.drawable.categoria_viajes, false, 5));
        categoria_dtos.add(new Categoria_DTO("Grifos", "", R.drawable.grifos, false, 8));
        return categoria_dtos;
    }

    private boolean isDescuentoOrBeneficio(String s) {
        if (s.equals("Beneficio") || s.equals("Descuento")) {
            return true;
        }
        return false;
    }

    private void buscar(String s, String distrito) {
        if (!s.matches("") && !distrito.matches("")) {
            if (distrito.equals("Mi ubicación")) {
                if (isCategory(s)) {
                    if (isDescuentoOrBeneficio(s)) {
                        Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                        operation_busquedas.buscarLocalesCercanos("", "", s, "", page);
                        operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                            @Override
                            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                                if (status) {
                                    try {
                                        adapter_locales = new Adapter_Locales(getActivity(), local_dtos, 0);
                                        lista_locales_busquedas.setAdapter(adapter_locales);
                                        Fragment_Resultado_Busquedas.this.onFinishLoad(lista_locales_busquedas);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } else {
                        Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                        operation_busquedas.buscarLocalesCercanos("", s, "", "", page);
                        operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                            @Override
                            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                                if (status) {
                                    try {
                                        adapter_locales = new Adapter_Locales(getActivity(), local_dtos, 0);
                                        lista_locales_busquedas.setAdapter(adapter_locales);
                                        Fragment_Resultado_Busquedas.this.onFinishLoad(lista_locales_busquedas);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                } else {
                    Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                    operation_busquedas.buscarLocalesCercanos(s, "", "", "", page);
                    operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                        @Override
                        public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                            if (status) {
                                try {
                                    adapter_locales = new Adapter_Locales(getActivity(), local_dtos, 0);
                                    lista_locales_busquedas.setAdapter(adapter_locales);
                                    Fragment_Resultado_Busquedas.this.onFinishLoad(lista_locales_busquedas);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            } else {
                if (isCategory(s)) {
                    if (isDescuentoOrBeneficio(s)) {
                        Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                        operation_busquedas.buscarLocales("", "", distrito, s, page);
                        operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                            @Override
                            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                                if (status) {
                                    try {
                                        adapter_locales = new Adapter_Locales(getActivity(), local_dtos, 0);
                                        lista_locales_busquedas.setAdapter(adapter_locales);
                                        Fragment_Resultado_Busquedas.this.onFinishLoad(lista_locales_busquedas);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } else {
                        Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                        operation_busquedas.buscarLocales("", s, distrito, "", page);
                        operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                            @Override
                            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                                if (status) {
                                    try {
                                        adapter_locales = new Adapter_Locales(getActivity(), local_dtos, 0);
                                        lista_locales_busquedas.setAdapter(adapter_locales);
                                        Fragment_Resultado_Busquedas.this.onFinishLoad(lista_locales_busquedas);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                } else {
                    Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                    operation_busquedas.buscarLocales(s, "", distrito, "", page);
                    operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                        @Override
                        public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                            if (status) {
                                try {
                                    adapter_locales = new Adapter_Locales(getActivity(), local_dtos, 0);
                                    lista_locales_busquedas.setAdapter(adapter_locales);
                                    Fragment_Resultado_Busquedas.this.onFinishLoad(lista_locales_busquedas);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        } else {
            if (!s.matches("")) {
                if (isCategory(s)) {
                    if (isDescuentoOrBeneficio(s)) {
                        Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                        operation_busquedas.buscarLocales("", "", "", s, page);
                        operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                            @Override
                            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                                if (status) {
                                    try {
                                        adapter_locales = new Adapter_Locales(getActivity(), local_dtos, 0);
                                        lista_locales_busquedas.setAdapter(adapter_locales);
                                        Fragment_Resultado_Busquedas.this.onFinishLoad(lista_locales_busquedas);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } else {
                        Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                        operation_busquedas.buscarLocales("", s, "", "", page);
                        operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                            @Override
                            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                                if (status) {
                                    try {
                                        adapter_locales = new Adapter_Locales(getActivity(), local_dtos, 0);
                                        lista_locales_busquedas.setAdapter(adapter_locales);
                                        Fragment_Resultado_Busquedas.this.onFinishLoad(lista_locales_busquedas);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                } else {
                    Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                    operation_busquedas.buscarLocales(s, "", "", "", page);
                    operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                        @Override
                        public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                            if (status) {
                                try {
                                    adapter_locales = new Adapter_Locales(getActivity(), local_dtos, 0);
                                    lista_locales_busquedas.setAdapter(adapter_locales);
                                    Fragment_Resultado_Busquedas.this.onFinishLoad(lista_locales_busquedas);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            } else {
                if (distrito.equals("Mi ubicación")) {
                    Operation_Locales_Cercanos operation_locales_cercanos = new Operation_Locales_Cercanos(getActivity());
                    operation_locales_cercanos.getLocalesCercanos();
                    operation_locales_cercanos.setInterface_operation_locales_cercanos(new Operation_Locales_Cercanos.Interface_Operation_Locales_Cercanos() {
                        @Override
                        public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages) {
                            if (status) {
                                try {
                                    adapter_locales = new Adapter_Locales(getActivity(), local_dtos, 0);
                                    lista_locales_busquedas.setAdapter(adapter_locales);
                                    Fragment_Resultado_Busquedas.this.onFinishLoad(lista_locales_busquedas);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else {
                    Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                    operation_busquedas.buscarLocales("", "", distrito, "", page);
                    operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                        @Override
                        public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                            if (status) {
                                try {
                                    adapter_locales = new Adapter_Locales(getActivity(), local_dtos, 0);
                                    lista_locales_busquedas.setAdapter(adapter_locales);
                                    Fragment_Resultado_Busquedas.this.onFinishLoad(lista_locales_busquedas);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    private void loadMoreDAta(int page) {
        if (!s.matches("") && !distrito.matches("")) {
            if (distrito.equals("Mi ubicación")) {
                if (isCategory(s)) {
                    if (isDescuentoOrBeneficio(s)) {
                        Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                        operation_busquedas.buscarLocalesCercanos("", "", s, "", page);
                        operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                            @Override
                            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                                if (status) {
                                    try {
                                        for (int i = 0; i < local_dtos.size(); i++) {
                                            adapter_locales.add(local_dtos.get(i));
                                        }
                                        isLoading = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } else {
                        Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                        operation_busquedas.buscarLocalesCercanos("", s, "", "", page);
                        operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                            @Override
                            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                                if (status) {
                                    try {
                                        for (int i = 0; i < local_dtos.size(); i++) {
                                            adapter_locales.add(local_dtos.get(i));
                                        }
                                        isLoading = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                } else {
                    Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                    operation_busquedas.buscarLocalesCercanos(s, "", "", "", page);
                    operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                        @Override
                        public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                            if (status) {
                                try {
                                    for (int i = 0; i < local_dtos.size(); i++) {
                                        adapter_locales.add(local_dtos.get(i));
                                    }
                                    isLoading = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            } else {
                if (isCategory(s)) {
                    if (isDescuentoOrBeneficio(s)) {
                        Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                        operation_busquedas.buscarLocales("", "", distrito, s, page);
                        operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                            @Override
                            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                                if (status) {
                                    try {
                                        for (int i = 0; i < local_dtos.size(); i++) {
                                            adapter_locales.add(local_dtos.get(i));
                                        }
                                        isLoading = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } else {
                        Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                        operation_busquedas.buscarLocales("", s, distrito, "", page);
                        operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                            @Override
                            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                                if (status) {
                                    try {
                                        for (int i = 0; i < local_dtos.size(); i++) {
                                            adapter_locales.add(local_dtos.get(i));
                                        }
                                        isLoading = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                } else {
                    Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                    operation_busquedas.buscarLocales(s, "", distrito, "", page);
                    operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                        @Override
                        public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                            if (status) {
                                try {
                                    for (int i = 0; i < local_dtos.size(); i++) {
                                        adapter_locales.add(local_dtos.get(i));
                                    }
                                    isLoading = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        } else {
            if (!s.matches("")) {
                if (isCategory(s)) {
                    if (isDescuentoOrBeneficio(s)) {
                        Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                        operation_busquedas.buscarLocales("", "", "", s, page);
                        operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                            @Override
                            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                                if (status) {
                                    try {
                                        for (int i = 0; i < local_dtos.size(); i++) {
                                            adapter_locales.add(local_dtos.get(i));
                                        }
                                        isLoading = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    } else {
                        Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                        operation_busquedas.buscarLocales("", s, "", "", page);
                        operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                            @Override
                            public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                                if (status) {
                                    try {
                                        for (int i = 0; i < local_dtos.size(); i++) {
                                            adapter_locales.add(local_dtos.get(i));
                                        }
                                        isLoading = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                } else {
                    Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                    operation_busquedas.buscarLocales(s, "", "", "", page);
                    operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                        @Override
                        public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                            if (status) {
                                try {
                                    for (int i = 0; i < local_dtos.size(); i++) {
                                        adapter_locales.add(local_dtos.get(i));
                                    }
                                    isLoading = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            } else {
                if (distrito.equals("Mi ubicación")) {
                    Operation_Locales_Cercanos operation_locales_cercanos = new Operation_Locales_Cercanos(getActivity());
                    operation_locales_cercanos.getLocalesCercanos();
                    operation_locales_cercanos.setInterface_operation_locales_cercanos(new Operation_Locales_Cercanos.Interface_Operation_Locales_Cercanos() {
                        @Override
                        public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages) {
                            if (status) {
                                try {
                                    for (int i = 0; i < local_dtos.size(); i++) {
                                        adapter_locales.add(local_dtos.get(i));
                                    }
                                    isLoading = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else {
                    Operation_Busquedas operation_busquedas = new Operation_Busquedas(getActivity());
                    operation_busquedas.buscarLocales("", "", distrito, "", page);
                    operation_busquedas.setInterface_operation_busquedas(new Operation_Busquedas.Interface_Operation_Busquedas() {
                        @Override
                        public void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje) {
                            if (status) {
                                try {
                                    for (int i = 0; i < local_dtos.size(); i++) {
                                        adapter_locales.add(local_dtos.get(i));
                                    }
                                    isLoading = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        }
    }
}