package com.bederr.beans;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Gantz on 5/07/14.
 */
public class Respuesta_DTO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private transient JSONObject jsonObject;

    public Respuesta_DTO() {
    }

    public Respuesta_DTO(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
