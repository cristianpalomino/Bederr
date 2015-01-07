package com.bederr.retail_v2.interfaces;

import com.bederr.beans_v2.Place_DTO;

import java.util.ArrayList;

/**
 * Created by Gantz on 23/12/14.
 */
public interface OnSuccessPlaces {
    void onSuccessPlaces(boolean success, ArrayList<Place_DTO> place_dtos, String count, String next, String previous);
}
