package com.bederr.beans_v2;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Gantz on 8/01/15.
 */
public class Country_DTO extends Bederr_DTO {

    private String id = "id";
    private String name = "name";
    private String flag_image = "flag_image";

    public String getId() {
        return parseString(id,getDataSource());
    }

    public String getName() {
        return parseString(name,getDataSource());
    }

    public String getFlag_image() {
        return parseString(flag_image,getDataSource());
    }

    public ArrayList<Area_DTO> getAreas(){
        try {
            ArrayList<Area_DTO> area_dtos = new ArrayList<Area_DTO>();
            JSONArray array = parseJSONArray("areas",getDataSource());
            for (int i = 0; i < array.length() ; i++) {
                Area_DTO area_dto = new Area_DTO();
                area_dto.setDataSource(array.getJSONObject(i));
                area_dtos.add(area_dto);
            }
            return area_dtos;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
