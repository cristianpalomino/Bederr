package com.bederr.account_v2.services;

import android.content.Context;

import com.bederr.account_v2.interfaces.OnSuccessPassword;
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
public class Service_Password {

    private Context context;
    private OnSuccessPassword onSuccessPassword;

    public Service_Password(Context context) {
        this.context = context;
    }

    public void setOnSuccessPassword(OnSuccessPassword onSuccessPassword) {
        this.onSuccessPassword = onSuccessPassword;
    }

    public void sendRequest(String email,String new_password,String old_password,String token) {
        RequestParams params = new RequestParams();
        params.put("old_password", old_password);
        params.put("new_password", new_password);
        params.put("email", email);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Authorization", "Token " + token);
        httpClient.put(context,Bederr_WS.BEDERR_CONTRASENIA, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    onSuccessPassword.onSuccessPassword(true,response.getString("detail"));
                }catch (JSONException e){
                    e.printStackTrace();
                    onSuccessPassword.onSuccessPassword(false,context.getString(R.string.error_api_message));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessPassword.onSuccessPassword(false,context.getString(R.string.error_api_message));
            }
        });
    }
}
