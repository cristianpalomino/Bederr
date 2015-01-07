package com.bederr.retail_v2.services;

import android.content.Context;

import com.bederr.beans_v2.Bederr_DTO;
import com.bederr.retail_v2.interfaces.OnSuccessCategory;
import com.bederr.util_v2.Bederr_WS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

/**
 * Created by Gantz on 23/12/14.
 */
public class Service_Category {

    private Context context;
    private OnSuccessCategory onSuccessCategory;

    public Service_Category(Context context) {
        this.context = context;
    }

    public void setOnSuccessCategory(OnSuccessCategory onSuccessCategory) {
        this.onSuccessCategory = onSuccessCategory;
    }

    public void sendRequest() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(context, Bederr_WS.BEDERR_CATEGORY, null , new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Bederr_DTO bederr_dto = new Bederr_DTO();
                bederr_dto.setDatasourcearray(response);
                onSuccessCategory.onSuccessCategory(true,bederr_dto.parseCategoryDTOs());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessCategory.onSuccessCategory(false,null);
            }
        });
    }
}
