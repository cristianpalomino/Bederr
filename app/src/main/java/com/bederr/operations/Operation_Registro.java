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
import com.bederr.beans.Usuario_DTO;
import com.bederr.ws.WS_Maven;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gantz on 11/07/14.
 */
public class Operation_Registro {

    private Context context;
    private JSONObject jsonObjectUsuario;
    private Interface_Operation_Registro interface_operation_registro;

    public Operation_Registro(Context context) {
        this.context = context;
    }

    public void registrarUsuario() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                WS_Maven.WS_REGISTRAR_USUARIO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJsonObject = new JSONObject(response);

                            int resultado = responseJsonObject.getInt("resultado");
                            if(resultado == 1){
                                Usuario_DTO usuario_dto = new Usuario_DTO();
                                usuario_dto.setJsonObject(responseJsonObject.getJSONObject("datos_usuario"));
                                interface_operation_registro.registrarUsuario(true,usuario_dto,"Registro Exitoso");
                            }else{
                                interface_operation_registro.registrarUsuario(false,null,responseJsonObject.getString("mensaje"));
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
                try {
                    params.put("nombre", jsonObjectUsuario.getString("nombre"));
                    params.put("apellido", jsonObjectUsuario.getString("apellido"));
                    params.put("sexo", jsonObjectUsuario.getString("sexo"));
                    params.put("nacimiento", jsonObjectUsuario.getString("nacimiento"));
                    params.put("email", jsonObjectUsuario.getString("email"));
                    params.put("password", jsonObjectUsuario.getString("password"));
                    params.put("nick", jsonObjectUsuario.getString("nick"));
                    params.put("facebook_id", jsonObjectUsuario.getString("facebook_id"));
                    params.put("tipo_registro", jsonObjectUsuario.getString("tipo_registro"));
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void setJsonObjectUsuario(JSONObject jsonObjectUsuario) {
        this.jsonObjectUsuario = jsonObjectUsuario;
    }

    public void setInterface_operation_registro(Interface_Operation_Registro interface_operation_registro) {
        this.interface_operation_registro = interface_operation_registro;
    }

    public interface Interface_Operation_Registro {
        void registrarUsuario(boolean state, Usuario_DTO usuario_dto, String mensaje);
    }
}
