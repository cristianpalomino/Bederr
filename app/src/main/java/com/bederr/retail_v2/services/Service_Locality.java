package com.bederr.retail_v2.services;

import android.content.Context;

import com.bederr.beans_v2.Bederr_DTO;
import com.bederr.util_v2.Bederr_WS;
import com.bederr.retail_v2.interfaces.OnSuccessLocality;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

/**
 * Created by Gantz on 23/12/14.
 */
public class Service_Locality {

    private Context context;
    private OnSuccessLocality onSuccessLocality;

    public Service_Locality(Context context) {
        this.context = context;
    }

    public void setOnSuccessLocality(OnSuccessLocality onSuccessLocality) {
        this.onSuccessLocality = onSuccessLocality;
    }

    public void sendRequest(String area) {
        AsyncHttpClient httpClient = new AsyncHttpClient();

        String URL = Bederr_WS.BEDERR_LOCALITY.replace("*",area);

        httpClient.get(context, URL, null , new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Bederr_DTO bederr_dto = new Bederr_DTO();
                bederr_dto.setDatasourcearray(response);
                onSuccessLocality.onSuccessLocality(true,bederr_dto.parseLocalityDTOs());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessLocality.onSuccessLocality(false,null);
            }
        });
    }
}
