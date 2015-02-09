package com.bederr.benefits_v2.services;

import android.content.Context;

import com.bederr.beans_v2.Bederr_DTO;
import com.bederr.benefits_v2.interfaces.OnSuccessAdd;
import com.bederr.benefits_v2.interfaces.OnSuccessDelete;
import com.bederr.benefits_v2.interfaces.OnSuccessPrograms;
import com.bederr.util_v2.Bederr_WS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by Gantz on 23/12/14.
 */
public class Service_Programs {

    private Context context;
    private OnSuccessPrograms onSuccessPrograms;
    private OnSuccessDelete onSuccessDelete;
    private OnSuccessAdd onSuccessAdd;

    public Service_Programs(Context context) {
        this.context = context;
    }

    public void setOnSuccessPrograms(OnSuccessPrograms onSuccessPrograms) {
        this.onSuccessPrograms = onSuccessPrograms;
    }

    public void setOnSuccessDelete(OnSuccessDelete onSuccessDelete) {
        this.onSuccessDelete = onSuccessDelete;
    }

    public void setOnSuccessAdd(OnSuccessAdd onSuccessAdd) {
        this.onSuccessAdd = onSuccessAdd;
    }

    public void sendRequest(String token,String area) {
        RequestParams params = new RequestParams();
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Authorization", "Token " + token);

        String URL = Bederr_WS.BEDERR_PROGRAMS.replace("*",area);
        httpClient.get(context, URL , params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Bederr_DTO bederr_dto = new Bederr_DTO();
                bederr_dto.setDataSource(response);

                String count = String.valueOf(bederr_dto.parseInt("count", response));
                String next = bederr_dto.parseString("next", response);
                String previous = bederr_dto.parseString("previous", response);

                onSuccessPrograms.onSuccessPrograms(true, bederr_dto.parseBenefitProgramDTOs(), count, next, previous);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessPrograms.onSuccessPrograms(false, null, null, null, null);
            }
        });
    }

    public void sendRequestMe(String token) {
        RequestParams params = new RequestParams();
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Authorization", "Token " + token);
        httpClient.get(context, Bederr_WS.BEDERR_ME_PROGRAMS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Bederr_DTO bederr_dto = new Bederr_DTO();
                bederr_dto.setDataSource(response);

                String count = String.valueOf(bederr_dto.parseInt("count", response));
                String next = bederr_dto.parseString("next", response);
                String previous = bederr_dto.parseString("previous", response);

                onSuccessPrograms.onSuccessPrograms(true, bederr_dto.parseBenefitProgramDTOs(), count, next, previous);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessPrograms.onSuccessPrograms(false, null, null, null, null);
            }
        });
    }

    public void sendRequestDelete(String token, String id) {
        String URL = Bederr_WS.BEDERR_ME_REMOVE_PROGRAMS.replace("#", id);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Authorization", "Token " + token);
        httpClient.delete(context, URL, null, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                onSuccessDelete.onSuccessDelete(true, "Correcto");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                onSuccessDelete.onSuccessDelete(false, "Error");
            }
        });
    }

    public void sendRequestAdd(String token, String id) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Authorization", "Token " + token);
        httpClient.post(context, Bederr_WS.BEDERR_ME_PROGRAMS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                onSuccessAdd.onSuccessAdd(true, "Correcto");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                onSuccessAdd.onSuccessAdd(false, "Necesitas actualizar tu perfil.\nPor favor ingresa tu número de DNI.");
            }
        });
    }
}
