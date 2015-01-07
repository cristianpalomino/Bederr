package com.bederr.operations;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bederr.ws.WS_Maven;
import com.bederr.beans.Empresa_DTO;
import com.bederr.session.Session_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gantz on 11/07/14.
 */
public class Operation_Empresas extends Operation_Master {

    private Context context;
    private Interface_Operation_Empresas interface_operation_empresas;
    private Interface_Operation_Mis_Empresas interface_operation_mis_empresas;
    private Interface_Operation_Eliminar_Empresa interface_operation_eliminar_empresa;

    public Operation_Empresas(Context context) {
        super(context);
        this.context = context;
    }

    public void  getEmpresasLife() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                WS_Maven.WS_OBTENER_EMPRESAS_LIFE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJsonObject = new JSONObject(response);
                            int resultado = Integer.parseInt(responseJsonObject.getString("resultado"));
                            if (resultado == 1) {
                                ArrayList<Empresa_DTO> empresa_dtos = new ArrayList<Empresa_DTO>();
                                JSONArray responseJsonArray = responseJsonObject.getJSONArray("empresas");

                                for (int i = 0; i < responseJsonArray.length(); i++) {
                                    JSONObject jsonObject = responseJsonArray.getJSONObject(i);
                                    Empresa_DTO empresa_dto = new Empresa_DTO(jsonObject);
                                    empresa_dtos.add(empresa_dto);
                                }
                                interface_operation_empresas.getEmpresasLife(true, empresa_dtos, "Mensaje");
                            } else {
                                interface_operation_empresas.getEmpresasLife(false, null, "Ocurrio un error.");
                            }
                        } catch (JSONException e) {
                            interface_operation_empresas.getEmpresasLife(false, null, "Ocurrio un error.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        interface_operation_empresas.getEmpresasLife(false, null, "Error o sin Internet.");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                Session_Manager session_manager = new Session_Manager(context);
                try {
                    JSONObject jsonObject = session_manager.getSession().getJsonObject();

                    params.put("idUsuario", jsonObject.getString("idUsuario"));
                    params.put("token", WS_Maven.TOKEN_MAVEN);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("PARMAS", params.toString());

                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void getMisEmpresasLife() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                WS_Maven.WS_OBTENER_MIS_EMPRESAS_LIFE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJsonObject = new JSONObject(response);
                            int resultado = Integer.parseInt(responseJsonObject.getString("resultado"));
                            if (resultado == 1) {
                                ArrayList<Empresa_DTO> empresa_dtos = new ArrayList<Empresa_DTO>();
                                JSONArray responseJsonArray = responseJsonObject.getJSONArray("empresas");

                                for (int i = 0; i < responseJsonArray.length(); i++) {
                                    JSONObject jsonObject = responseJsonArray.getJSONObject(i);
                                    Empresa_DTO empresa_dto = new Empresa_DTO(responseJsonArray.getJSONObject(i));
                                    empresa_dtos.add(empresa_dto);
                                }

                                if(empresa_dtos.isEmpty()){
                                    interface_operation_mis_empresas.getMisEmpresasLife(false, null, "Ocurrio un error.");
                                }else{
                                    interface_operation_mis_empresas.getMisEmpresasLife(true, empresa_dtos, "Mensaje");
                                }
                            } else {
                            //
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
                Session_Manager session_manager = new Session_Manager(context);
                try {
                    JSONObject jsonObject = session_manager.getSession().getJsonObject();
                    params.put("token", WS_Maven.TOKEN_MAVEN);
                    params.put("idUsuario", jsonObject.getString("idUsuario"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void removeEmpresaLife(final String idEmpresa) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                WS_Maven.WS_ELIMINAR_EMPRESA_LIFE   ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJsonObject = new JSONObject(response);
                            int resultado = responseJsonObject.getInt("resultado");
                            if (resultado == 1) {
                                interface_operation_eliminar_empresa.removeEmpresaLife(responseJsonObject.getString("mensaje"));
                            } else {
                                interface_operation_eliminar_empresa.removeEmpresaLife("Error");
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
                Session_Manager session_manager = new Session_Manager(context);
                try {
                    JSONObject jsonObject = session_manager.getSession().getJsonObject();
                    params.put("token", WS_Maven.TOKEN_MAVEN);
                    params.put("idEmpresaLife",idEmpresa);
                    params.put("idUsuario", jsonObject.getString("idUsuario"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("PARAMS", params.toString());

                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void setInterface_operation_eliminar_empresa(Interface_Operation_Eliminar_Empresa interface_operation_eliminar_empresa) {
        this.interface_operation_eliminar_empresa = interface_operation_eliminar_empresa;
    }

    public void setInterface_operation_empresas(Interface_Operation_Empresas interface_operation_empresas) {
        this.interface_operation_empresas = interface_operation_empresas;
    }

    public void setInterface_operation_mis_empresas(Interface_Operation_Mis_Empresas interface_operation_mis_empresas) {
        this.interface_operation_mis_empresas = interface_operation_mis_empresas;
    }

    public interface Interface_Operation_Empresas {
        void getEmpresasLife(boolean status, ArrayList<Empresa_DTO> empresa_dtos, String mensaje);
    }

    public interface Interface_Operation_Mis_Empresas {
        void getMisEmpresasLife(boolean status, ArrayList<Empresa_DTO> empresa_dtos, String mensaje);
    }

    public interface Interface_Operation_Eliminar_Empresa {
        void removeEmpresaLife(String mensaje);
    }
}
