package com.bederr.beans;

/**
 * Created by Gantz on 5/07/14.
 */
public class Categoria_DTO {

    private String nombrecategoria;
    private int imagencategoria;
    private String cantidadcategoria;
    private int codigocategoria;
    private boolean isEmpresa;

    public Categoria_DTO() {
    }

    public Categoria_DTO(String nombrecategoria, String cantidadcategoria, int imagencategoria, boolean isEmpresa, int codigocategoria) {
        this.nombrecategoria = nombrecategoria;
        this.cantidadcategoria = cantidadcategoria;
        this.imagencategoria = imagencategoria;
        this.isEmpresa = isEmpresa;
        this.codigocategoria = codigocategoria;
    }

    public String getNombrecategoria() {
        return nombrecategoria;
    }

    public void setNombrecategoria(String nombrecategoria) {
        this.nombrecategoria = nombrecategoria;
    }

    public String getCantidadcategoria() {
        return cantidadcategoria;
    }

    public void setCantidadcategoria(String cantidadcategoria) {
        this.cantidadcategoria = cantidadcategoria;
    }

    public int getImagencategoria() {
        return imagencategoria;
    }

    public void setImagencategoria(int imagencategoria) {
        this.imagencategoria = imagencategoria;
    }

    public int getCodigocategoria() {
        return codigocategoria;
    }

    public void setCodigocategoria(int codigocategoria) {
        this.codigocategoria = codigocategoria;
    }

    public boolean isEmpresa() {
        return isEmpresa;
    }

    public void setEmpresa(boolean isEmpresa) {
        this.isEmpresa = isEmpresa;
    }
}
