package com.bederr.account_v2.interfaces;

import com.bederr.beans_v2.User_DTO;

/**
 * Created by Gantz on 23/12/14.
 */
public interface OnSuccessMe {
    void onSuccessMe(boolean success, User_DTO user_dto);
    void onFailRequest(boolean success, String message);
}
