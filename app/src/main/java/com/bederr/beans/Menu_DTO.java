package com.bederr.beans;

import org.json.JSONObject;

/**
 * Created by Gantz on 5/07/14.
 */
public class Menu_DTO {

    private transient JSONObject jsonObjectMenu;

    public Menu_DTO(JSONObject jsonObjectMenu) {
        this.jsonObjectMenu = jsonObjectMenu;
    }

    public JSONObject getJsonObjectMenu() {
        return jsonObjectMenu;
    }

    public void setJsonObjectMenu(JSONObject jsonObjectMenu) {
        this.jsonObjectMenu = jsonObjectMenu;
    }
}
