package com.bederr.account_v2.services;

import android.content.Context;

import com.bederr.account_v2.interfaces.OnSuccessLogin;

import pe.bederr.com.R;

import com.bederr.util_v2.Bederr_WS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gantz on 23/12/14.
 */
public class Service_Login {

    private Context context;
    private OnSuccessLogin onSuccessLogin;

    public Service_Login(Context context) {
        this.context = context;
    }

    public void setOnSuccessLogin(OnSuccessLogin onSuccessLogin) {
        this.onSuccessLogin = onSuccessLogin;
    }

    public void sendRequest(String email, String password) {
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, Bederr_WS.BEDERR_LOGIN, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    onSuccessLogin.onSuccessLogin(true, response.getString("token"));
                } catch (JSONException e) {
                    onSuccessLogin.onSuccessLogin(false, context.getString(R.string.error_login_message));
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessLogin.onSuccessLogin(false, context.getString(R.string.error_api_message));
            }
        });
    }

    public void sendRequestFacebook(String facebook_token) {
        RequestParams params = new RequestParams();
        params.put("access_token",facebook_token);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(context, Bederr_WS.BEDERR_FACEBOOK, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    onSuccessLogin.onSuccessLogin(true, response.getString("token"));
                } catch (JSONException e) {
                    onSuccessLogin.onSuccessLogin(false, context.getString(R.string.error_login_message));
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessLogin.onSuccessLogin(false, context.getString(R.string.error_api_message));
            }
        });
    }
}
