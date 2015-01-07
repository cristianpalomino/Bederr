package com.bederr.benefits_v2.interfaces;

import com.bederr.beans_v2.Benefit_Program_DTO;

import java.util.ArrayList;

/**
 * Created by Gantz on 23/12/14.
 */
public interface OnSuccessPrograms {
    void onSuccessPrograms(boolean success, ArrayList<Benefit_Program_DTO> benefit_program_dtos, String count, String next, String previous);
}
