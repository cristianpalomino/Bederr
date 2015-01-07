package com.bederr.retail_v2.services;

import android.content.Context;

import com.bederr.beans_v2.Bederr_DTO;
import com.bederr.retail_v2.interfaces.OnSuccessListings;
import com.bederr.util_v2.Bederr_WS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by Gantz on 23/12/14.
 */
public class Service_Listings {

    private Context context;
    private OnSuccessListings onSuccessListings;

    public Service_Listings(Context context) {
        this.context = context;
    }

    public void setOnSuccessListings(OnSuccessListings onSuccessListings) {
        this.onSuccessListings = onSuccessListings;
    }

    public void sendRequestUser(String token,String area) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Authorization", "Token " + token);
        httpClient.get(context, Bederr_WS.BEDERR_LISTINGS, null , new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Bederr_DTO bederr_dto = new Bederr_DTO();
                bederr_dto.setDataSource(response);

                String count = String.valueOf(bederr_dto.parseInt("count", response));
                String next = bederr_dto.parseString("next", response);
                String previous = bederr_dto.parseString("previous", response);

                onSuccessListings.onSuccessListings(true, bederr_dto.parseListingDTOs(), count, next, previous);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessListings.onSuccessListings(false, null, null, null, null);
            }
        });
    }

    public void sendRequest(String area) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(context, Bederr_WS.BEDERR_LISTINGS, null , new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Bederr_DTO bederr_dto = new Bederr_DTO();
                bederr_dto.setDataSource(response);
                
                String count = String.valueOf(bederr_dto.parseInt("count", response));
                String next = bederr_dto.parseString("next", response);
                String previous = bederr_dto.parseString("previous", response);

                onSuccessListings.onSuccessListings(true, bederr_dto.parseListingDTOs(), count, next, previous);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessListings.onSuccessListings(false, null, null, null, null);
            }
        });
    }
}
