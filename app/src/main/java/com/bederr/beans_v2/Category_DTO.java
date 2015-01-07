package com.bederr.beans_v2;

/**
 * Created by Gantz on 2/01/15.
 */
public class Category_DTO extends Bederr_DTO {

    private String id = "id";
    private String name = "name";
    private String image = "image";

    public String getId() {
        return parseString(id,getDataSource());
    }

    public String getName() {
        return parseString(name,getDataSource());
    }

    public String getImage() {
        return parseString(image,getDataSource());
    }
}
