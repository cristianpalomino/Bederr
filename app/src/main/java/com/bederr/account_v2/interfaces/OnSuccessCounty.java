package com.bederr.account_v2.interfaces;

import com.bederr.beans_v2.Country_DTO;

import java.util.ArrayList;

/**
 * Created by Gantz on 8/01/15.
 */
public interface OnSuccessCounty {
    void onSuccessCountry(boolean success,ArrayList<Country_DTO> country_dtos,String message);
}
