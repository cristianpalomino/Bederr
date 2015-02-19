package com.bederr.beans_v2;

/**
 * Created by Gantz on 17/02/15.
 */
public class Location_DTO {

    private String pais;
    private String ciudad;
    private String flag;

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
