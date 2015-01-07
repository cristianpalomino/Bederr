package com.bederr.account_v2.services;

import android.content.Context;

import com.bederr.account_v2.interfaces.OnSuccessUpdate;
import com.bederr.beans_v2.User_DTO;
import pe.bederr.com.R;
import com.bederr.util_v2.Bederr_WS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by Gantz on 23/12/14.
 */
public class Service_Update {

    private Context context;
    private OnSuccessUpdate onSuccessUpdate;

    public Service_Update(Context context) {
        this.context = context;
    }

    public void setOnSuccessUpdate(OnSuccessUpdate onSuccessUpdate) {
        this.onSuccessUpdate = onSuccessUpdate;
    }

    public void sendRequest(String token,User_DTO user_dto,String dni) {
        RequestParams params = new RequestParams();
        params.put("first_name",user_dto.getFirst_name());
        params.put("last_name",user_dto.getLast_name());
        params.put("email",user_dto.getEmail());
        params.put("birthday","");
        params.put("gender",user_dto.getGender());
        params.put("dni",dni);
        params.put("photo","");

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Authorization", "Token " + token);
        httpClient.put(context, Bederr_WS.BEDERR_USUARIO, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                onSuccessUpdate.onSuccessUpdate(true, "Correcto");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessUpdate.onSuccessUpdate(false, context.getString(R.string.error_api_message));
            }
        });
    }
}
