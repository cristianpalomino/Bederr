package com.bederr.operations;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bederr.ws.WS_Maven;
import com.bederr.beans.Categoria_DTO;
import pe.bederr.com.R;
import com.bederr.utils.Util_Categorias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gantz on 11/07/14.
 */
public class Operation_Categorias {

    private Context context;
    private Interface_Operation_Categorias interface_operation_categorias;

    public Operation_Categorias(Context context) {
        this.context = context;
    }

    public void getCategoryes() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                WS_Maven.WS_OBTENER_CATEGORIAS_DOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJsonObject = new JSONObject(response);
                            int success = responseJsonObject.getInt("resultado");
                            if (success == 1) {
                                JSONArray responseCategorias = responseJsonObject.getJSONArray("categoria");
                                JSONArray responseEmpresas = responseJsonObject.getJSONArray("empresa");

                                ArrayList<Categoria_DTO> categoria_dtos = new ArrayList<Categoria_DTO>();

                                for (int i = 0; i < responseCategorias.length(); i++) {
                                    JSONObject jsonObject = responseCategorias.getJSONObject(i);
                                    Categoria_DTO categoria_dto = new Categoria_DTO();
                                    categoria_dto.setCodigocategoria(Integer.parseInt(jsonObject.getString("Id")));
                                    categoria_dto.setNombrecategoria(jsonObject.getString("Nombre"));
                                    categoria_dto.setCantidadcategoria("5");
                                    categoria_dto.setImagencategoria(Util_Categorias.getImageCateogry(Integer.parseInt(jsonObject.getString("Id"))));
                                    categoria_dto.setEmpresa(false);
                                    categoria_dtos.add(categoria_dto);
                                }

                                for (int i = 0; i < responseEmpresas.length(); i++) {
                                    JSONObject jsonObject = responseEmpresas.getJSONObject(i);
                                    Categoria_DTO categoria_dto = new Categoria_DTO();
                                    categoria_dto.setCodigocategoria(Integer.parseInt(jsonObject.getString("EmpresaId")));
                                    categoria_dto.setNombrecategoria(jsonObject.getString("Nombre"));
                                    categoria_dto.setCantidadcategoria("5");
                                    categoria_dto.setImagencategoria(R.drawable.placeholder_empresa);
                                    categoria_dto.setEmpresa(true);
                                    //categoria_dtos.add(categoria_dto);
                                }
                            } else {
                                interface_operation_categorias.getCategoryes(true, null);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, "Error o sin Internet.", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", WS_Maven.TOKEN_MAVEN);
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void setInterface_operation_categorias(Interface_Operation_Categorias interface_operation_categorias) {
        this.interface_operation_categorias = interface_operation_categorias;
    }

    public interface Interface_Operation_Categorias {
        void getCategoryes(boolean status, ArrayList<Categoria_DTO> categoria_dtos);
    }
}
