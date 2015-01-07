package com.bederr.operations;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bederr.beans.Local_DTO;
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
public class Operation_Locales_Cercanos extends Operation_Master {

    private Context context;
    private Interface_Operation_Locales_Cercanos interface_operation_locales_cercanos;
    private int page;

    public Operation_Locales_Cercanos(Context context) {
        super(context);
        this.context = context;
    }

    public void getLocalesCercanos() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                WS_Maven.WS_OBTENER_LOCALES_CERCANOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJsonObject = new JSONObject(response);
                            JSONArray responseJsonArray = responseJsonObject.getJSONArray("locales");

                            int totalpages = responseJsonObject.getInt("totalPaginas");
                            ArrayList<Local_DTO> local_dtos = new ArrayList<Local_DTO>();
                            boolean status = true;

                            for (int i = 0; i < responseJsonArray.length(); i++) {
                                Local_DTO local_dto = new Local_DTO(responseJsonArray.getJSONObject(i));
                                local_dtos.add(local_dto);
                            }

                            interface_operation_locales_cercanos.getLocalesCercanos(true, local_dtos, totalpages);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                final HashMap params = new HashMap();
                /*
                params.put("token", "0e9347897aa24ae0a2c439801cb2bc27");
                params.put("pagina", String.valueOf(page));
                params.put("latitud", String.valueOf(getLocation().getLatitude()));
                params.put("longitud", String.valueOf(getLocation().getLongitude()));
                params.put("usuario", getUser_id());
                */
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setInterface_operation_locales_cercanos(Interface_Operation_Locales_Cercanos interface_operation_locales_cercanos) {
        this.interface_operation_locales_cercanos = interface_operation_locales_cercanos;
    }

    public interface Interface_Operation_Locales_Cercanos {
        void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages);
    }
}
