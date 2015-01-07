package com.bederr.retail_v2.interfaces;

import com.bederr.beans_v2.Category_DTO;

import java.util.ArrayList;

/**
 * Created by Gantz on 23/12/14.
 */
public interface OnSuccessCategory {
    void onSuccessCategory(boolean success, ArrayList<Category_DTO> category_dtos);
}
