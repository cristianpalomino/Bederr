package com.bederr.beans_v2;

/**
 * Created by Gantz on 3/01/15.
 */
public class Locality_DTO extends Bederr_DTO {

    private String id = "id";
    private String name = "name";

    public int getId() {
        return parseInt(id,getDataSource());
    }

    public String getName() {
        return parseString(name,getDataSource());
    }
}
