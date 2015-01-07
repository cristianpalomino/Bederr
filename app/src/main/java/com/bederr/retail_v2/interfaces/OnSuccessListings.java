package com.bederr.retail_v2.interfaces;

import com.bederr.beans_v2.Listing_DTO;

import java.util.ArrayList;

/**
 * Created by Gantz on 23/12/14.
 */
public interface OnSuccessListings {
    void onSuccessListings(boolean success, ArrayList<Listing_DTO> listing_dtos, String count, String next, String previous);
}
