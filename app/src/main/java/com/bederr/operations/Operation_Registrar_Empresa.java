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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gantz on 11/07/14.
 */
public class Operation_Registrar_Empresa {

    private Context context;
    private Interface_Operation_Registrar_Empresa interface_operation_registrar_empresa;

    public Operation_Registrar_Empresa(Context context) {
        this.context = context;
    }

    public void registrarEmpresaLife(final String idUsuario, final String idEmpresaLife, final String dniUsuario, final String validacion) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                WS_Maven.WS_REGISTRAR_EMPRESA_LIFE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJsonObject = new JSONObject(response);

                            int resultado = Integer.parseInt(responseJsonObject.getString("resultado"));
                            if (resultado == 1) {
                                interface_operation_registrar_empresa.registarEmpresaLife(true,responseJsonObject,responseJsonObject.getString("mensaje"));
                            } else {
                                interface_operation_registrar_empresa.registarEmpresaLife(false,null,"No se pudo agregar la empresa");
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
                params.put("idUsuario", idUsuario);
                params.put("idEmpresaLife", idEmpresaLife);
                params.put("dniUsuario", dniUsuario);
                params.put("validacion", validacion);

                Log.e("PARAMS", params.toString());

                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void setInterface_operation_registrar_empresa(Interface_Operation_Registrar_Empresa interface_operation_registrar_empresa) {
        this.interface_operation_registrar_empresa = interface_operation_registrar_empresa;
    }

    public interface Interface_Operation_Registrar_Empresa {
        void registarEmpresaLife(boolean status, JSONObject response, String mensaje);
    }
}
