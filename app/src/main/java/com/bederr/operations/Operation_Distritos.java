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
import com.bederr.beans.Distrito_DTO;
import com.bederr.ws.WS_Maven;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gantz on 11/07/14.
 */
public class Operation_Distritos {

    private Context context;
    private Interface_Operation_Distritos interface_operation_distritos;

    public Operation_Distritos(Context context) {
        this.context = context;
    }

    public void getDistritos() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                WS_Maven.WS_OBTENER_DISTRITOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("DISTRITOS",response);
                            JSONObject responseJsonObject = new JSONObject(response);
                            JSONArray responseJsonArray = responseJsonObject.getJSONArray("city");

                            ArrayList<Distrito_DTO> distrito_dtos = new ArrayList<Distrito_DTO>();
                            distrito_dtos.add(new Distrito_DTO("Mi ubicación","-1"));
                            boolean status = true;

                            for (int i = 0; i < responseJsonArray.length(); i++) {
                                JSONObject jsonObject = responseJsonArray.getJSONObject(i);
                                Distrito_DTO categoria_dto = new Distrito_DTO(jsonObject.getString("c_name_ubigeo"),jsonObject.getString("n_id_ubigeo"));
                                distrito_dtos.add(categoria_dto);
                            }

                            interface_operation_distritos.getDistritos(status, distrito_dtos);

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

    public void setInterface_operation_distritos(Interface_Operation_Distritos interface_operation_distritos) {
        this.interface_operation_distritos = interface_operation_distritos;
    }

    public interface Interface_Operation_Distritos {
        void getDistritos(boolean status, ArrayList<Distrito_DTO> distrito_dtos);
    }
}
