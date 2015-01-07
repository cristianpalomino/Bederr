package com.bederr.retail_v2.interfaces;

import com.bederr.beans_v2.Locality_DTO;

import java.util.ArrayList;

/**
 * Created by Gantz on 23/12/14.
 */
public interface OnSuccessLocality {
    void onSuccessLocality(boolean success, ArrayList<Locality_DTO> locality_dtos);
}
