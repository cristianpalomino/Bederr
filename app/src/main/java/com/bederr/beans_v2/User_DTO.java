package com.bederr.beans_v2;

/**
 * Created by Gantz on 23/12/14.
 */
public class User_DTO extends Bederr_DTO {

    private String access_token;

    private String first_name = "first_name";
    private String last_name = "last_name";
    private String email = "email";
    private String birthday = "birthday";
    private String gender = "gender";
    private String photo = "photo";

    private String password;
    private String old_password;
    private String new_password;

    public String getFirst_name() {
        return parseString(first_name,getDataSource());
    }

    public String getLast_name() {
        return parseString(last_name,getDataSource());
    }

    public String getEmail() {
        return parseString(email,getDataSource());
    }

    public String getBirthday() {
        return parseString(birthday,getDataSource());
    }

    public String getGender() {
        return parseString(gender,getDataSource());
    }

    public String getPhoto() {
        return parseString(photo,getDataSource());
    }

    public String getAccess_token() {
        return parseString(access_token,getDataSource());
    }
}
