package com.bederr.beans_v2;

/**
 * Created by Gantz on 22/12/14.
 */
public class Question_DTO extends Bederr_DTO {

    private String category_name = "category_name";
    private String category_code = "category";
    private String created_at = "created_at";
    private String content = "content";
    private String owner_fullname = "owner_fullname";
    private String owner_photo = "owner_photo";

    private String id = "id";
    private String num_answers = "total_answers";

    public String getCategory_name() {
        return parseString(category_name,getDataSource());
    }

    public String getCategory_code() {
        return parseString(category_code,getDataSource());
    }

    public String getCreated_at() {
        return parseString(created_at,getDataSource());
    }

    public String getContent() {
        return parseString(content,getDataSource());
    }

    public String getOwner_fullname() {
        return parseString(owner_fullname,getDataSource());
    }

    public String getOwner_photo() {
        return parseString(owner_photo,getDataSource());
    }

    public int getId() {
        return parseInt(id,getDataSource());
    }

    public int getNum_answers() {
        return parseInt(num_answers,getDataSource());
    }

    public Point_DTO getPoint_dto() {
        return parsePointDTO(KEY_POINT,getDataSource());
    }
}
