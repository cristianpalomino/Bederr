package com.bederr.questions.interfaces;

import com.bederr.beans_v2.Place_DTO;
import com.bederr.beans_v2.Question_DTO;

import java.util.ArrayList;

/**
 * Created by Gantz on 23/12/14.
 */
public interface OnSuccessQuestion {
    void onSuccessQuestion(boolean success, ArrayList<Question_DTO> question_dtos, String count, String next, String previous);
}
