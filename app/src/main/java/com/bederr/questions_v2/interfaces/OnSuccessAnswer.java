package com.bederr.questions_v2.interfaces;

import com.bederr.beans_v2.Answer_DTO;
import com.bederr.beans_v2.Question_DTO;

import java.util.ArrayList;

/**
 * Created by Gantz on 23/12/14.
 */
public interface OnSuccessAnswer {
    void onSuccessAnswer(boolean success, ArrayList<Answer_DTO> answer_dtos, String count, String next, String previous);
}
