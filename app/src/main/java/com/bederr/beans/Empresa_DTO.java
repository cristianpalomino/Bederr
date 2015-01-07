package com.bederr.beans;

import android.view.View;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Gantz on 5/07/14.
 */
public class Empresa_DTO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private transient JSONObject jsonObject;

    private int _id;
    private String empresaId;
    private View view;

    public Empresa_DTO() {
    }

    public Empresa_DTO(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(String empresaId) {
        this.empresaId = empresaId;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setView(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }
}
