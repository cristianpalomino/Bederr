package com.bederr.benefits_v2.fragments;

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

import com.bederr.adapter.Adapter_Categoria_Pregunta;
import com.bederr.beans.Categoria_DTO;
import com.bederr.beans_v2.Category_DTO;
import com.bederr.beans_v2.Locality_DTO;
import com.bederr.benefits_v2.adapter.Benefit_Category_A;
import com.bederr.fragments.Fragment_Master;
import com.bederr.retail_v2.adapters.Category_A;
import com.bederr.retail_v2.adapters.Locality_A;
import com.bederr.retail_v2.fragments.Result_Search_F;
import com.bederr.retail_v2.interfaces.OnSuccessCategory;
import com.bederr.retail_v2.interfaces.OnSuccessLocality;
import com.bederr.retail_v2.services.Service_Category;
import com.bederr.retail_v2.services.Service_Locality;
import com.bederr.utils.Util_Fonts;

import java.util.ArrayList;
import java.util.Locale;

import pe.bederr.com.R;

/**
 * Created by Gantz on 5/07/14.
 */
public class Benefits_Search_F extends Fragment_Master implements View.OnFocusChangeListener {

    private EditText edtcategorias;
    private EditText edtdistritos;

    private ListView listaCategorias;
    private ListView listaDistritos;

    private boolean flagcategoria = false;
    private boolean flagdistrito = false;

    protected String s = "";
    protected String distrito = "";
    protected String otro = "";

    private Locality_DTO distrito_dto;
    private Categoria_DTO categoria_dto;

    public Benefits_Search_F() {
        setId_layout(R.layout.fragment_busquedas);
        setId_container(R.id.frame_container);
    }

    public static final Benefits_Search_F newInstance() {
        return new Benefits_Search_F();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        closeKeyboard();

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
                distrito_dto = (Locality_DTO) listaDistritos.getAdapter().getItem(position);
                String distrito = distrito_dto.getName();
                edtdistritos.setText(distrito);
            }
        });

        listaCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoria_dto = (Categoria_DTO) listaCategorias.getAdapter().getItem(position);
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
                if (listaCategorias.getAdapter() != null) {
                    String text = edtcategorias.getText().toString().toLowerCase(Locale.getDefault());
                    ((Benefit_Category_A) listaCategorias.getAdapter()).filter(text);
                }
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
                    ((Locality_A) listaDistritos.getAdapter()).filter(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtcategorias.setOnFocusChangeListener(this);
        edtdistritos.setOnFocusChangeListener(this);

        ArrayList<Categoria_DTO> categoria_dtos = new ArrayList<Categoria_DTO>();
        categoria_dtos.add(new Categoria_DTO("Viajes", "viajes", R.drawable.categoria_viajes, false, 5));
        categoria_dtos.add(new Categoria_DTO("Tiendas y servicios", "tiendas-y-servicios", R.drawable.categoria_markets, false, 7));
        categoria_dtos.add(new Categoria_DTO("Salud y belleza", "salud-y-belleza", R.drawable.categoria_salud, false, 9));
        categoria_dtos.add(new Categoria_DTO("Ropa", "ropa", R.drawable.categoria_ropa, false, 4));
        categoria_dtos.add(new Categoria_DTO("Grifo", "grifo", R.drawable.grifos, false, 8));
        categoria_dtos.add(new Categoria_DTO("Comida", "comida", R.drawable.categoria_comida, false, 2));
        getBederr().setCategoria_dtos(categoria_dtos);
        Benefit_Category_A adapter_categoria = new Benefit_Category_A(getBederr() , categoria_dtos);
        listaCategorias.setAdapter(adapter_categoria);
        listaCategorias.setVisibility(View.VISIBLE);


        Service_Locality service_locality = new Service_Locality(getBederr());
        service_locality.sendRequest();
        service_locality.setOnSuccessLocality(new OnSuccessLocality() {
            @Override
            public void onSuccessLocality(boolean success, ArrayList<Locality_DTO> locality_dtos) {
                try {
                    if (success) {
                        getBederr().setLocality_dtos(locality_dtos);
                        Locality_A locality_a = new Locality_A(getBederr(), locality_dtos);
                        listaDistritos.setAdapter(locality_a);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        flagcategoria = true;
        flagdistrito = true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
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

                } else {
                    listaDistritos.setVisibility(View.VISIBLE);
                }
            } else {
                listaDistritos.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void initActionBar() {
        super.initActionBar();

        ImageView action_left = (ImageView) getView().findViewById(R.id.action_left);
        action_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b);
                trans.remove(Benefits_Search_F.this);
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
                    getActivity().
                            getSupportFragmentManager().
                            beginTransaction().
                            setCustomAnimations(
                                    R.animator.izquierda_derecha_b,
                                    R.animator.izquierda_derecha_b
                            )
                            .add(
                                    R.id.container,
                                    Benefits_Result_Search_F.newInstance(s, distrito),
                                    Benefits_Result_Search_F.class.getName()
                            )
                            .addToBackStack(null)
                            .commit();
                }
                hideSoftKeyboard();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeKeyboard();
        Benefits_Search_F.this.getView().setFocusableInTouchMode(true);
        Benefits_Search_F.this.getView().requestFocus();
        Benefits_Search_F.this.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.setCustomAnimations(R.animator.abajo_arriba_b, R.animator.arriba_abajo_b);
                        trans.remove(Benefits_Search_F.this);
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
