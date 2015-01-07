package com.bederr.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bederr.beans.Categoria_DTO;
import com.bederr.adapter.Adapter_Categoria;
import com.bederr.adapter.Adapter_Distrito;
import com.bederr.beans.Distrito_DTO;
import pe.bederr.com.R;
import com.bederr.operations.Operation_Distritos;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Gantz on 5/07/14.
 */
public class Fragment_Busqueda_Life extends Fragment_Master implements View.OnFocusChangeListener, Operation_Distritos.Interface_Operation_Distritos {

    private EditText edtcategorias;
    private EditText edtdistritos;

    private ListView listaCategorias;
    private ListView listaDistritos;

    private Operation_Distritos operation_distritos;

    private boolean flagcategoria = false;
    private boolean flagdistrito = false;

    protected String s = "";
    protected String distrito = "";
    protected String otro = "";

    public Fragment_Busqueda_Life() {
        setId_layout(R.layout.fragment_busquedas);
        setId_container(R.id.frame_container);
    }

    public static final Fragment_Busqueda_Life newInstance() {
        return new Fragment_Busqueda_Life();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        operation_distritos = new Operation_Distritos(getActivity());
        flagcategoria = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(inflater, viewGroup, bundle);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        edtdistritos = (EditText) getView().findViewById(R.id.edt_buscar_distrito);
        edtcategorias = (EditText) getView().findViewById(R.id.edt_buscar_categorias);

        /*
        SET FONTS
         */
        edtcategorias.setTypeface(Util_Fonts.setPNALight(getActivity()));
        edtdistritos.setTypeface(Util_Fonts.setPNALight(getActivity()));

        listaCategorias = (ListView) getView().findViewById(R.id.lista_categorias);
        listaDistritos = (ListView) getView().findViewById(R.id.lista_distritos);

        listaDistritos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Distrito_DTO distrito_dto = (Distrito_DTO) listaDistritos.getAdapter().getItem(position);
                String distrito = distrito_dto.getNombredistrito();
                edtdistritos.setText(distrito);
            }
        });
        listaCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Categoria_DTO categoria_dto = (Categoria_DTO) listaCategorias.getAdapter().getItem(position);
                String categoria = categoria_dto.getNombrecategoria();
                edtcategorias.setText(categoria);
            }
        });

        edtcategorias.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = edtcategorias.getText().toString().toLowerCase(Locale.getDefault());
                ((Adapter_Categoria) listaCategorias.getAdapter()).filter(text);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtdistritos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (listaDistritos.getAdapter() != null) {
                    String text = edtdistritos.getText().toString().toLowerCase(Locale.getDefault());
                    ((Adapter_Distrito) listaDistritos.getAdapter()).filter(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtcategorias.setOnFocusChangeListener(this);
        edtdistritos.setOnFocusChangeListener(this);

        ArrayList<Categoria_DTO> categoria_dtos = new ArrayList<Categoria_DTO>();
        categoria_dtos.add(new Categoria_DTO("Comida", "", R.drawable.categoria_comida, false, 2));
        categoria_dtos.add(new Categoria_DTO("Ropa", "", R.drawable.categoria_ropa, false, 4));
        categoria_dtos.add(new Categoria_DTO("Salud", "", R.drawable.categoria_salud, false, 9));
        categoria_dtos.add(new Categoria_DTO("Bar", "", R.drawable.categoria_bar, false, 3));
        categoria_dtos.add(new Categoria_DTO("Diversión", "", R.drawable.categoria_entretenimiento, false, 1));
        categoria_dtos.add(new Categoria_DTO("Markets", "", R.drawable.categoria_markets, false, 7));
        categoria_dtos.add(new Categoria_DTO("Viajes", "", R.drawable.categoria_viajes, false, 5));
        categoria_dtos.add(new Categoria_DTO("Grifos", "", R.drawable.grifos, false, 8));

        Adapter_Categoria adapter_categoria = new Adapter_Categoria(getActivity(), categoria_dtos);
        listaCategorias.setAdapter(adapter_categoria);
        listaCategorias.setVisibility(View.VISIBLE);
        flagcategoria = true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtcategorias, InputMethodManager.SHOW_IMPLICIT);

        if (v.equals(edtcategorias)) {
            if (hasFocus) {
                if (!flagcategoria) {

                } else {
                    listaCategorias.setVisibility(View.VISIBLE);
                }
            } else {
                listaCategorias.setVisibility(View.GONE);
            }
        } else if (v.equals(edtdistritos)) {
            if (hasFocus) {
                if (!flagdistrito) {
                    operation_distritos.getDistritos();
                    operation_distritos.setInterface_operation_distritos(this);
                } else {
                    listaDistritos.setVisibility(View.VISIBLE);
                }
            } else {
                listaDistritos.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void getDistritos(boolean status, ArrayList<Distrito_DTO> distrito_dtos) {
        if (status) {
            try {
                distrito_dtos.remove(0);
                Adapter_Distrito adapter_categoria = new Adapter_Distrito(getActivity(),distrito_dtos);
                listaDistritos.setAdapter(adapter_categoria);
                listaDistritos.setVisibility(View.VISIBLE);
                flagdistrito = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isCategory(String s) {
        for (int i = 0; i < listaCategorias.getAdapter().getCount(); i++) {
            Categoria_DTO categoria_dto = (Categoria_DTO) listaCategorias.getItemAtPosition(i);
            if (s.equals(categoria_dto.getNombrecategoria())) {
                return true;
            }
        }
        return false;
    }

    private boolean isDescuentoOrBeneficio(String s) {
        if (s.equals("Beneficio") || s.equals("Descuento")) {
            return true;
        }
        return false;
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();

        ImageView action_left = (ImageView) getView().findViewById(R.id.action_left);
        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();

                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b);
                trans.remove(Fragment_Busqueda_Life.this);
                trans.commit();
                manager.popBackStack();
            }
        });

        TextView action_right = (TextView) getView().findViewById(R.id.txt_action_right);
        action_right.setTypeface(Util_Fonts.setPNASemiBold(getActivity()));
        action_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtcategorias.getText().toString().matches("") && edtdistritos.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), "Ingrese al menos un criterio de busqueda...!", Toast.LENGTH_SHORT).show();
                } else {
                    s = edtcategorias.getText().toString();
                    distrito = edtdistritos.getText().toString();
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.animator.izquierda_derecha_b, R.animator.izquierda_derecha_b).add(R.id.container, Fragment_Resultado_Busquedas_Life.newInstance(s, distrito),Fragment_Resultado_Busquedas_Life.class.getName()).addToBackStack(null).commit();
                }
                hideSoftKeyboard();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment_Busqueda_Life.this.getView().setFocusableInTouchMode(true);
        Fragment_Busqueda_Life.this.getView().requestFocus();
        Fragment_Busqueda_Life.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b);
                        trans.remove(Fragment_Busqueda_Life.this);
                        trans.commit();
                        manager.popBackStack();
                        hideSoftKeyboard();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void hideSoftKeyboard() {
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
