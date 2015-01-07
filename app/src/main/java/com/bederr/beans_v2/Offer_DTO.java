package com.bederr.beans_v2;

/**
 * Created by Gantz on 22/12/14.
 */
public class Offer_DTO extends Bederr_DTO {

    private String id = "id";
    private String created_at = "created_at";
    private String title = "title";
    private String content = "content";
    private String from_date = "from_date";
    private String to_date = "to_date";
    private String terms = "terms";
    private String validate_use = "validate_use";

    public int getId() {
        return parseInt(id,getDataSource());
    }

    public String getCreated_at() {
        return parseString(created_at,getDataSource());
    }

    public String getTitle() {
        return parseString(title,getDataSource());
    }

    public String getContent() {
        return parseString(content,getDataSource());
    }

    public String getFrom_date() {
        return parseString(from_date,getDataSource());
    }

    public String getTo_date() {
        return parseString(to_date,getDataSource());
    }

    public String getTerms() {
        return parseString(terms,getDataSource());
    }

    public boolean isValidate_use() {
        return parseBooelan(validate_use,getDataSource());
    }
}
