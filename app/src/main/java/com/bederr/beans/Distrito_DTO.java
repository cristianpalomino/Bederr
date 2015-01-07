package com.bederr.beans;

/**
 * Created by Gantz on 5/07/14.
 */
public class Distrito_DTO {

    private String nombredistrito;
    private String iddistrito;

    public Distrito_DTO(String nombredistrito, String iddistrito) {
        this.nombredistrito = nombredistrito;
        this.iddistrito = iddistrito;
    }

    public String getNombredistrito() {
        return nombredistrito;
    }

    public void setNombredistrito(String nombredistrito) {
        this.nombredistrito = nombredistrito;
    }

    public String getIddistrito() {
        return iddistrito;
    }

    public void setIddistrito(String iddistrito) {
        this.iddistrito = iddistrito;
    }
}
