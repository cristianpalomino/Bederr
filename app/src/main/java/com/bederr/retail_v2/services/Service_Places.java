package com.bederr.retail_v2.services;

import android.content.Context;

import com.bederr.beans_v2.Bederr_DTO;
import com.bederr.retail_v2.interfaces.OnSuccessPlaces;
import com.bederr.util_v2.Bederr_WS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by Gantz on 23/12/14.
 */
public class Service_Places {

    private Context context;
    private OnSuccessPlaces onSuccessPlaces;

    public Service_Places(Context context) {
        this.context = context;
    }

    public void setOnSuccessPlaces(OnSuccessPlaces onSuccessPlaces) {
        this.onSuccessPlaces = onSuccessPlaces;
    }

    public void sendRequestUser(String token, String lat, String lng, String name, String cat, String city) {
        RequestParams params = new RequestParams();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("name", name);
        params.put("cat", cat);
        params.put("locality", city);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Authorization", "Token " + token);
        httpClient.get(context, Bederr_WS.BEDERR_PLACES, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Bederr_DTO bederr_dto = new Bederr_DTO();
                bederr_dto.setDataSource(response);

                String count = String.valueOf(bederr_dto.parseInt("count", response));
                String next = bederr_dto.parseString("next", response);
                String previous = bederr_dto.parseString("previous", response);

                onSuccessPlaces.onSuccessPlaces(true,bederr_dto.parsePlaceDTOs(),count,next,previous);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessPlaces.onSuccessPlaces(false,null,null,null,null);
            }
        });
    }

    public void sendRequest(String lat, String lng, String name, String cat, String city) {
        RequestParams params = new RequestParams();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("name", name);
        params.put("cat", cat);
        params.put("city", city);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(context, Bederr_WS.BEDERR_PLACES, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Bederr_DTO bederr_dto = new Bederr_DTO();
                bederr_dto.setDataSource(response);
                
                String count = String.valueOf(bederr_dto.parseInt("count", response));
                String next = bederr_dto.parseString("next", response);
                String previous = bederr_dto.parseString("previous", response);

                onSuccessPlaces.onSuccessPlaces(true,bederr_dto.parsePlaceDTOs(),count,next,previous);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessPlaces.onSuccessPlaces(false,null,null,null,null);
            }
        });
    }
}
