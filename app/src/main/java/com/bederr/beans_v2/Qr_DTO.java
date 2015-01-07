package com.bederr.beans_v2;

/**
 * Created by Gantz on 4/01/15.
 */
public class Qr_DTO extends Bederr_DTO {

    private String code = "code";
    private String in_place_offer = "in_place_offer";
    private String purchase_corporate_offer = "purchase_corporate_offer";

    public String getCode() {
        return parseString(code,getDataSource());
    }

    public int getIn_place_offer() {
        return parseInt(in_place_offer,getDataSource());
    }

    public int getPurchase_corporate_offer() {
        return parseInt(purchase_corporate_offer,getDataSource());
    }
}
