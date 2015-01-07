package com.bederr.beans;

import android.view.View;

import org.json.JSONObject;

/**
 * Created by Gantz on 5/10/14.
 */
public class Lista_DTO {

    private String id_lista;
    private String title_lista;
    private String decription_lista;
    private String image_lista;
    private String status_lista;
    private String date_lista;

    private JSONObject json_lista;
    private View view_lista;

    public Lista_DTO() {
    }

    public Lista_DTO(String id_lista, String title_lista, String decription_lista, String image_lista, String status_lista, String date_lista) {
        this.id_lista = id_lista;
        this.title_lista = title_lista;
        this.decription_lista = decription_lista;
        this.image_lista = image_lista;
        this.status_lista = status_lista;
        this.date_lista = date_lista;
    }

    public String getId_lista() {
        return id_lista;
    }

    public void setId_lista(String id_lista) {
        this.id_lista = id_lista;
    }

    public String getDate_lista() {
        return date_lista;
    }

    public void setDate_lista(String date_lista) {
        this.date_lista = date_lista;
    }

    public String getStatus_lista() {
        return status_lista;
    }

    public void setStatus_lista(String status_lista) {
        this.status_lista = status_lista;
    }

    public String getImage_lista() {
        return image_lista;
    }

    public void setImage_lista(String image_lista) {
        this.image_lista = image_lista;
    }

    public String getDecription_lista() {
        return decription_lista;
    }

    public void setDecription_lista(String decription_lista) {
        this.decription_lista = decription_lista;
    }

    public String getTitle_lista() {
        return title_lista;
    }

    public void setTitle_lista(String title_lista) {
        this.title_lista = title_lista;
    }

    public void setJson_lista(JSONObject json_lista) {
        this.json_lista = json_lista;
    }

    public void setView_lista(View view_lista) {
        this.view_lista = view_lista;
    }

    public View getView_lista() {
        return view_lista;
    }

    public JSONObject getJson_lista() {
        return json_lista;
    }
}
