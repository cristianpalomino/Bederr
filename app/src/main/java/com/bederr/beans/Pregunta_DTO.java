package com.bederr.beans;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Gantz on 5/07/14.
 */
public class Pregunta_DTO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private transient JSONObject jsonObject;

    public Pregunta_DTO() {
    }

    public Pregunta_DTO(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
