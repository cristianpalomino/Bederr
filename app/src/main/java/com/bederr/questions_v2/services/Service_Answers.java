package com.bederr.questions_v2.services;

import android.content.Context;

import com.bederr.beans_v2.Bederr_DTO;
import com.bederr.questions_v2.interfaces.OnSuccessAnswer;
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
public class Service_Answers {

    private Context context;
    private OnSuccessAnswer onSuccessAnswer;

    public Service_Answers(Context context) {
        this.context = context;
    }

    public void setOnSuccessAnswer(OnSuccessAnswer onSuccessAnswer) {
        this.onSuccessAnswer = onSuccessAnswer;
    }

    public void sendRequest(String id) {
        String URL = Bederr_WS.BEDERR_DETAIL_CREATE_QUESTION.replace("#",id);
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

                onSuccessAnswer.onSuccessAnswer(true,bederr_dto.parseAnswerDTOs(),count,next,previous);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                onSuccessAnswer.onSuccessAnswer(false,null,null,null,null);
            }
        });
    }
}
