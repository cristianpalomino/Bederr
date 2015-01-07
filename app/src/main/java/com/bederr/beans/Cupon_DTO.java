package com.bederr.beans;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Gantz on 5/07/14.
 */
public class Cupon_DTO implements Serializable {
    private transient JSONObject jsonObject;

    public Cupon_DTO(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
