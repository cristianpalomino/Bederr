package com.bederr.account_v2.services;

import android.content.Context;

import com.bederr.account_v2.interfaces.OnSuccessCounty;
import com.bederr.account_v2.interfaces.OnSuccessLogin;
import com.bederr.beans_v2.Bederr_DTO;
import com.bederr.beans_v2.Country_DTO;
import com.bederr.util_v2.Bederr_WS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pe.bederr.com.R;

/**
 * Created by Gantz on 23/12/14.
 */
public class Service_Country {

    private Context context;
    private OnSuccessCounty onSuccessCounty;

    public Service_Country(Context context) {
        this.context = context;
    }

    public void setOnSuccessCounty(OnSuccessCounty onSuccessCounty) {
        this.onSuccessCounty = onSuccessCounty;
    }

    public void sendRequest() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(context, Bederr_WS.BEDERR_COUNTRY, null , new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Bederr_DTO bederr_dto = new Bederr_DTO();
                bederr_dto.setDatasourcearray(response);
                onSuccessCounty.onSuccessCountry(true,bederr_dto.parseCountrys(),"Correcto");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessCounty.onSuccessCountry(false,null,"Error");
            }
        });
    }
}
