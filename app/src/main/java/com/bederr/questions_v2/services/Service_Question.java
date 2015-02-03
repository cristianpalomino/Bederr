package com.bederr.questions_v2.services;

import android.content.Context;

import com.bederr.beans_v2.Bederr_DTO;
import com.bederr.questions_v2.interfaces.OnSuccessQuestion;
import com.bederr.util_v2.Bederr_WS;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by Gantz on 23/12/14.
 */
public class Service_Question {

    private Context context;
    private OnSuccessQuestion onSuccessQuestion;

    public Service_Question(Context context) {
        this.context = context;
    }

    public void setOnSuccessQuestion(OnSuccessQuestion onSuccessQuestion) {
        this.onSuccessQuestion = onSuccessQuestion;
    }


    public void sendRequestUser(String token, String lat, String lng,String area) {
        RequestParams params = new RequestParams();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("area", area);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Authorization", "Token " + token);
        httpClient.get(context, Bederr_WS.BEDERR_QUESTIONS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Bederr_DTO bederr_dto = new Bederr_DTO();
                bederr_dto.setDataSource(response);

                String count = String.valueOf(bederr_dto.parseInt("count", response));
                String next = bederr_dto.parseString("next", response);
                String previous = bederr_dto.parseString("previous", response);

                onSuccessQuestion.onSuccessQuestion(true, bederr_dto.parseQuestionDTOs(), count, next, previous);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessQuestion.onSuccessQuestion(false, null, null, null, null);
            }
        });
    }

    public void sendRequest(String lat, String lng, String area) {
        RequestParams params = new RequestParams();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("area", area);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(context, Bederr_WS.BEDERR_QUESTIONS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Bederr_DTO bederr_dto = new Bederr_DTO();
                bederr_dto.setDataSource(response);

                String count = String.valueOf(bederr_dto.parseInt("count", response));
                String next = bederr_dto.parseString("next", response);
                String previous = bederr_dto.parseString("previous", response);

                onSuccessQuestion.onSuccessQuestion(true, bederr_dto.parseQuestionDTOs(), count, next, previous);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessQuestion.onSuccessQuestion(false, null, null, null, null);
            }
        });
    }

    public void loadMore(String URL) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(context, URL , null , new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Bederr_DTO bederr_dto = new Bederr_DTO();
                bederr_dto.setDataSource(response);

                String count = String.valueOf(bederr_dto.parseInt("count", response));
                String next = bederr_dto.parseString("next", response);
                String previous = bederr_dto.parseString("previous", response);

                onSuccessQuestion.onSuccessQuestion(true, bederr_dto.parseQuestionDTOs(), count, next, previous);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessQuestion.onSuccessQuestion(false, null, null, null, null);
            }
        });
    }

    public void sendQuestion(String token, String category, String content) {
        RequestParams params = new RequestParams();
        params.put("category", category);
        params.put("content", content);

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.addHeader("Authorization", "Token " + token);
        httpClient.post(context, Bederr_WS.BEDERR_QUESTIONS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                onSuccessQuestion.onSuccessQuestion(true, null, null, null, null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessQuestion.onSuccessQuestion(false, null, null, null, null);
            }
        });
    }
}
