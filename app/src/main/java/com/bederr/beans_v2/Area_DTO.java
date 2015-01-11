package com.bederr.beans_v2;

/**
 * Created by Gantz on 8/01/15.
 */
public class Area_DTO extends Bederr_DTO {

    private String id = "id";
    private String name = "name";

    public String getId() {
        return parseString(id,getDataSource());
    }

    public String getName() {
        return parseString(name,getDataSource());
    }
}
