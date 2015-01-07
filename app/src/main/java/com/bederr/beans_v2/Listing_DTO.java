package com.bederr.beans_v2;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Gantz on 22/12/14.
 */
public class Listing_DTO extends Bederr_DTO {

    private String id = "id";
    private String name = "name";
    private String image = "image";

    public int getId() {
        return parseInt(id, getDataSource());
    }

    public String getName() {
        return parseString(name, getDataSource());
    }

    public ArrayList<Place_DTO> getPlace_dtos() {
        ArrayList<Place_DTO> place_dtos = new ArrayList<Place_DTO>();
        try {
            JSONArray array = getDataSource().getJSONArray("places");
            for (int i = 0; i < array.length(); i++) {
                Place_DTO place_dto = new Place_DTO();
                place_dto.setDataSource(array.getJSONObject(i));
                place_dtos.add(place_dto);
            }
            return place_dtos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getImage() {
        return parseString(image, getDataSource());
    }
}
