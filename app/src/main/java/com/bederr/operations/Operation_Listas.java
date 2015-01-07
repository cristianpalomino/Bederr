package com.bederr.operations;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bederr.beans.Lista_DTO;
import com.bederr.ws.WS_Maven;
import com.bederr.beans.Local_DTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gantz on 11/07/14.
 */
public class Operation_Listas {

    private Context context;
    private int page;
    private Interface_Operation_listas interface_operation_listas;
    private Interface_Operation_Locales_Por_Lista interface_operation_locales_por_lista;

    public Operation_Listas(Context context) {
        this.context = context;
    }

    public void getListas(final int page) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                WS_Maven.WS_OBTENER_LISTAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("listas",response);
                            JSONObject responseJsonObject = new JSONObject(response);
                            JSONArray responseJsonArray = responseJsonObject.getJSONArray("listas");

                            String resultado = responseJsonObject.getString("resultado");
                            ArrayList<Lista_DTO> lista_dtos = new ArrayList<Lista_DTO>();

                            if(resultado.equals("1")){
                                for (int i = 0; i < responseJsonArray.length(); i++) {
                                    Lista_DTO lista_dto = new Lista_DTO();
                                    lista_dto.setId_lista(responseJsonArray.getJSONObject(i).getString("id"));
                                    lista_dto.setTitle_lista(responseJsonArray.getJSONObject(i).getString("title"));
                                    lista_dto.setDecription_lista(responseJsonArray.getJSONObject(i).getString("description"));
                                    lista_dto.setImage_lista(responseJsonArray.getJSONObject(i).getString("image"));
                                    lista_dto.setStatus_lista(responseJsonArray.getJSONObject(i).getString("status"));
                                    lista_dto.setDate_lista(responseJsonArray.getJSONObject(i).getString("date_register"));
                                    lista_dto.setJson_lista(responseJsonArray.getJSONObject(i));
                                    lista_dtos.add(lista_dto);
                                }
                            }
                            interface_operation_listas.getlistas(true,lista_dtos);
                        } catch (JSONException e) {
                            interface_operation_listas.getlistas(false,null);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        interface_operation_listas.getlistas(false,null);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", WS_Maven.TOKEN_MAVEN);
                params.put("page",String.valueOf(page));
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void getListas(final String id) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                WS_Maven.WS_OBTENER_LISTA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("listas",response);
                            JSONObject responseJsonObject = new JSONObject(response);
                            JSONArray responseJsonArray = responseJsonObject.getJSONArray("locales");

                            String resultado = responseJsonObject.getString("resultado");
                            ArrayList<Local_DTO> local_dtos = new ArrayList<Local_DTO>();

                            if(resultado.equals("1")){
                                for (int i = 0; i < responseJsonArray.length(); i++) {
                                    Local_DTO local_dto = new Local_DTO(responseJsonArray.getJSONObject(i));
                                    local_dtos.add(local_dto);
                                }
                            }
                            interface_operation_locales_por_lista.getLocalesPorLista(true, local_dtos);
                        } catch (JSONException e) {
                            interface_operation_locales_por_lista.getLocalesPorLista(false, null);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        interface_operation_locales_por_lista.getLocalesPorLista(false, null);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", WS_Maven.TOKEN_MAVEN);
                params.put("lista",id);
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void setInterface_operation_locales_por_lista(Interface_Operation_Locales_Por_Lista interface_operation_locales_por_lista) {
        this.interface_operation_locales_por_lista = interface_operation_locales_por_lista;
    }

    public void setInterface_operation_listas(Interface_Operation_listas interface_operation_listas) {
        this.interface_operation_listas = interface_operation_listas;
    }

    public interface Interface_Operation_listas {
        void getlistas(boolean status, ArrayList<Lista_DTO> lista_dtos);
    }

    public interface Interface_Operation_Locales_Por_Lista {
        void getLocalesPorLista(boolean status, ArrayList<Local_DTO> local_dtos);
    }

    public void setPage(int page) {
        this.page = page;
    }
}
