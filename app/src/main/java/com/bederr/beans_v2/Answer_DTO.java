package com.bederr.beans_v2;

/**
 * Created by Gantz on 22/12/14.
 */
public class Answer_DTO extends Bederr_DTO {

    private String id= "id";

    private String retail_name= "retail_name";
    private String category_name= "category_name";
    private String category_code= "category_code";
    private String address= "address";

    public int getId() {
        return parseInt(id,getDataSource());
    }

    public String getRetail_name() {
        return parseString(retail_name,getDataSource());
    }

    public String getCategory_name() {
        return parseString(category_name,getDataSource());
    }

    public String getCategory_code() {
        return parseString(category_code,getDataSource());
    }

    public String getAddress() {
        return parseString(address,getDataSource());
    }

    public Point_DTO getPoint_dto() {
        return parsePointDTO(KEY_POINT,getDataSource());
    }
}
