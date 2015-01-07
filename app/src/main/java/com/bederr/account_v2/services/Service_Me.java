package com.bederr.account_v2.services;

import android.content.Context;

import com.bederr.account_v2.interfaces.OnSuccessMe;
import com.bederr.beans_v2.User_DTO;
import com.bederr.util_v2.Bederr_WS;

import pe.bederr.com.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by Gantz on 23/12/14.
 */
public class Service_Me {

    private Context context;
    private OnSuccessMe onSuccessMe;

    public Service_Me(Context context) {
        this.context = context;
    }

    public void setOnSuccessMe(OnSuccessMe onSuccessMe) {
        this.onSuccessMe = onSuccessMe;
    }

    public void sendRequest(String token) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Authorization", "Token " + token);
        httpClient.get(context, Bederr_WS.BEDERR_USUARIO, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                User_DTO user_dto = new User_DTO();
                user_dto.setDataSource(response);
                onSuccessMe.onSuccessMe(true, user_dto);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessMe.onFailRequest(false, context.getString(R.string.error_api_message));
            }
        });
    }
}
