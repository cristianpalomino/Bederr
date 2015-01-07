package com.bederr.benefits_v2.services;

import android.content.Context;

import com.bederr.beans_v2.Qr_DTO;
import com.bederr.benefits_v2.interfaces.OnSuccessOffer;
import com.bederr.util_v2.Bederr_WS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by Gantz on 23/12/14.
 */
public class Service_Offer {

    private Context context;
    private OnSuccessOffer onSuccessOffer;

    public Service_Offer(Context context) {
        this.context = context;
    }

    public void setOnSuccessOffer(OnSuccessOffer onSuccessOffer) {
        this.onSuccessOffer = onSuccessOffer;
    }

    public void sendRequest(String token,String in_place_offer,String purchase_corporate_offer) {
        RequestParams params = new RequestParams();
        params.put("in_place_offer", in_place_offer);
        params.put("purchase_corporate_offer", purchase_corporate_offer);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Authorization", "Token " + token);
        httpClient.post(context, Bederr_WS.BEDERR_OFFER, params , new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Qr_DTO qr_dto = new Qr_DTO();
                qr_dto.setDataSource(response);
                onSuccessOffer.onSuccessOffer(true,qr_dto);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessOffer.onSuccessOffer(false,null);
            }
        });
    }
}
