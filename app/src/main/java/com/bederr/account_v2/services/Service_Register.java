package com.bederr.account_v2.services;

import android.content.Context;

import com.bederr.util_v2.Bederr_WS;
import com.bederr.account_v2.interfaces.OnSuccessRegister;

import pe.bederr.com.R;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gantz on 23/12/14.
 */
public class Service_Register {

    private Context context;
    private OnSuccessRegister onSuccessRegister;

    public Service_Register(Context context) {
        this.context = context;
    }

    public void setOnSuccessRegister(OnSuccessRegister onSuccessRegister) {
        this.onSuccessRegister = onSuccessRegister;
    }

    public void sendRequest(String first_name,String last_name,String email,String birthday,String gender,String password) {
        RequestParams params = new RequestParams();
        params.put("first_name",first_name);
        params.put("last_name", last_name);
        params.put("email", email);
        params.put("birthday", birthday);
        params.put("gender", gender);
        params.put("password", password);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, Bederr_WS.BEDERR_REGISTRO, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    onSuccessRegister.onSuccessRegister(true, response.getString("token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    onSuccessRegister.onSuccessRegister(false, context.getString(R.string.error_api_message));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessRegister.onSuccessRegister(false, context.getString(R.string.error_api_message));
            }
        });
    }
}
