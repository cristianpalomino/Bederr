package com.bederr.operations;

import android.content.Context;

import com.bederr.beans.Local_DTO;
import com.bederr.ws.WS_Maven;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Operation_Busquedas extends Operation_Master {
    private Context context;
    private Interface_Operation_Busquedas interface_operation_busquedas;
    private int page;

    public Operation_Busquedas(Context context) {
        super(context);
        this.context = context;
    }

    public void buscarLocalesPorNombre(final String s,int page) {
        RequestParams params = new RequestParams();
        params.put("token", WS_Maven.TOKEN_MAVEN);
        params.put("s", s);
        params.put("category", "");
        params.put("city", "");
        params.put("page",String.valueOf(page));
        params.put("empresaLife", "");
        params.put("usuario", getUser_id());

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(180000);
        asyncHttpClient.post(context, WS_Maven.WS_BUSQUEDAS_LOCALES, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (Integer.parseInt(response.getString("resultado")) == 1) {
                        JSONArray jsonarray_local = response.getJSONArray("locales");
                        ArrayList localArrayList = new ArrayList();
                        for (int i = 0; i < jsonarray_local.length(); i++)
                            localArrayList.add(new Local_DTO(jsonarray_local.getJSONObject(i)));
                        interface_operation_busquedas.getLocalesCercanos(true, localArrayList, -1, "Correcto");
                    }
                    interface_operation_busquedas.getLocalesCercanos(false, null, -1, null);
                } catch (JSONException localJSONException) {
                    interface_operation_busquedas.getLocalesCercanos(false, null, -1, null);
                    localJSONException.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                interface_operation_busquedas.getLocalesCercanos(false, null, -1, null);
            }
        });
    }

    public void buscarLocales(final String s, final String category, final String city, final String filtro,int page) {
        RequestParams params = new RequestParams();
        params.put("token", WS_Maven.TOKEN_MAVEN);
        params.put("s", s);
        params.put("category", category);
        params.put("city", city);
        params.put("page",String.valueOf(page));
        params.put("filtro", filtro);
        params.put("usuario", getUser_id());

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(180000);
        asyncHttpClient.post(context, WS_Maven.WS_BUSQUEDAS_LOCALES, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (Integer.parseInt(response.getString("resultado")) == 1) {
                        JSONArray jsonarray_local = response.getJSONArray("locales");
                        ArrayList localArrayList = new ArrayList();
                        for (int i = 0; i < jsonarray_local.length(); i++) {
                            localArrayList.add(new Local_DTO(jsonarray_local.getJSONObject(i)));
                        }
                        interface_operation_busquedas.getLocalesCercanos(true, localArrayList, -1, "Correcto");
                    } else {
                        interface_operation_busquedas.getLocalesCercanos(false, null, -1, null);
                    }
                } catch (JSONException e) {
                    interface_operation_busquedas.getLocalesCercanos(false, null, -1, null);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                interface_operation_busquedas.getLocalesCercanos(false, null, -1, null);
            }
        });
    }

    public void buscarLocalesCercanos(final String paramString1, final String paramString2, final String paramString3, final String paramString4,int page) {
        RequestParams params = new RequestParams();
        params.put("token", WS_Maven.TOKEN_MAVEN);
        params.put("categoria", paramString2);
        params.put("empresaLife", "");
        params.put("filtro", paramString3);
        params.put("latitud", String.valueOf(getLocation().getLatitude()));
        params.put("longitud", String.valueOf(getLocation().getLongitude()));
        params.put("nombre", "");
        params.put("pagina",String.valueOf(page));
        params.put("usuario", getUser_id());

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(180000);
        asyncHttpClient.post(context, WS_Maven.WS_OBTENER_LOCALES_CERCANOS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (Integer.parseInt(response.getString("resultado")) == 1) {
                        JSONArray jsonarray_local = response.getJSONArray("locales");
                        ArrayList localArrayList = new ArrayList();
                        for (int i = 0; i < jsonarray_local.length(); i++)
                            localArrayList.add(new Local_DTO(jsonarray_local.getJSONObject(i)));
                        interface_operation_busquedas.getLocalesCercanos(true, localArrayList, -1, "Correcto");
                    } else {
                        interface_operation_busquedas.getLocalesCercanos(false, null, -1, null);
                    }
                } catch (JSONException localJSONException) {
                    interface_operation_busquedas.getLocalesCercanos(false, null, -1, null);
                    localJSONException.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                interface_operation_busquedas.getLocalesCercanos(false, null, -1, null);
            }
        });
    }

    public void setInterface_operation_busquedas(Interface_Operation_Busquedas paramInterface_Operation_Busquedas) {
        this.interface_operation_busquedas = paramInterface_Operation_Busquedas;
    }

    public void setPage(int paramInt) {
        this.page = paramInt;
    }

    public static abstract interface Interface_Operation_Busquedas {
        public abstract void getLocalesCercanos(boolean status, ArrayList<Local_DTO> local_dtos, int pages, String mensaje);
    }
}