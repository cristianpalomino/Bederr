package com.bederr.beans_v2;

/**
 * Created by Gantz on 4/01/15.
 */
public class Benefit_Program_DTO extends Bederr_DTO {

    private String id = "id";
    private String name = "name";
    private String total_users = "total_users";
    private String color = "color_hex";
    private String description = "description";
    private String validate = "validate";
    private String company_name = "company_name";
    private String company_logo = "company_logo";

    public int getId() {
        return parseInt(id,getDataSource());
    }

    public String getName() {
        return parseString(name,getDataSource());
    }

    public int getTotal_users() {
        return parseInt(total_users,getDataSource());
    }

    public String getColor() {
        return parseString(color,getDataSource());
    }

    public String getDescription() {
        return parseString(description,getDataSource());
    }

    public boolean isValidate() {
        return parseBooelan(validate,getDataSource());
    }

    public String getCompany_name() {
        return parseString(company_name,getDataSource());
    }

    public String getCompany_logo() {
        return parseString(company_logo,getDataSource());
    }
}
