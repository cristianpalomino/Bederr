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
public class Operation_Locales_Life extends Operation_Master{

    private Context context;
    private Interface_Operation_Locales_Life interface_operation_locales_life;
    private int page;

    public Operation_Locales_Life(Context context) {
        super(context);
        this.context = context;
    }

    public void getLocalesCercanos(final String idEmpresa) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.POST,
                WS_Maven.WS_OBTENER_LOCALES_LIFE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseJsonObject = new JSONObject(response);
                            JSONArray responseJsonArray = responseJsonObject.getJSONArray("locales");

                            int success = responseJsonObject.getInt("resultado");
                            if(success == 1){

                                ArrayList<Local_DTO> local_dtos = new ArrayList<Local_DTO>();
                                for (int i = 0; i < responseJsonArray.length(); i++) {
                                    Local_DTO local_dto = new Local_DTO(responseJsonArray.getJSONObject(i));
                                    local_dtos.add(local_dto);
                                }

                                if(local_dtos.isEmpty()){
                                    interface_operation_locales_life.getLocalesLife(false,null,-1,"Ocurrio un Error...");
                                }else{
                                    interface_operation_locales_life.getLocalesLife(true,local_dtos,-1,"Ok");
                                }
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
                params.put("empresa",idEmpresa);
                params.put("usuario",getUser_id());
                return params;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setInterface_operation_locales_life(Interface_Operation_Locales_Life interface_operation_locales_life) {
        this.interface_operation_locales_life = interface_operation_locales_life;
    }

    public interface Interface_Operation_Locales_Life {
        void getLocalesLife(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje);
    }
}
