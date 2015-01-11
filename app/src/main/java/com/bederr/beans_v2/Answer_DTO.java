package com.bederr.beans_v2;

import org.json.JSONException;

/**
 * Created by Gantz on 22/12/14.
 */
public class Answer_DTO extends Bederr_DTO {

    private String content = "content";
    private String id = "id";
    private String created_at = "created_at";
    private String owner_photo = "owner_photo";
    private String owner_fullname = "owner_fullname";

    public String getContent() {
        return parseString(content, getDataSource());
    }

    public int getId() {
        return parseInt(id, getDataSource());
    }

    public String getCreated_at() {
        return parseString(created_at, getDataSource());
    }

    public String getOwner_photo() {
        return parseString(owner_photo, getDataSource());
    }

    public String getOwner_fullname() {
        return parseString(owner_fullname, getDataSource());
    }

    public Place_DTO getPlace_dto() {
        try {
            if (!getDataSource().isNull("place")) {
                Place_DTO place_dto = new Place_DTO();
                place_dto.setDataSource(getDataSource().getJSONObject("place"));
                return place_dto;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
