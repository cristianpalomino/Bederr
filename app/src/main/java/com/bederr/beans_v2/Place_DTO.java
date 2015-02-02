package com.bederr.beans_v2;

import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Gantz on 22/12/14.
 */
public class Place_DTO extends Bederr_DTO {

    private String id = "id";

    private String name = "name";
    private String category_name = "category_name";
    private String category_code = "category_code";
    private String address = "address";
    private String city = "city";
    private String state = "state";
    private String telephone = "telephone";

    private String mf_hours_from = "mf_hours_from";
    private String mf_hours_to = "mf_hours_to";
    private String ss_hours_from = "ss_hours_from";
    private String ss_hours_to = "ss_hours_to";
    private String distance = "distance";

    private String inplace_offers = "inplace_offers";
    private String special_offers = "special_offers";
    private String corporate_offers = "corporate_offers";

    public int getId() {
        return parseInt(id, getDataSource());
    }

    public String getName() {
        return parseString(name, getDataSource());
    }

    public String getCategory_name() {
        return parseString(category_name, getDataSource());
    }

    public String getCategory_code() {
        return parseString(category_code, getDataSource());
    }

    public String getAddress() {
        return parseString(address, getDataSource());
    }

    public String getCity() {
        return parseString(city, getDataSource());
    }

    public String getState() {
        return parseString(state, getDataSource());
    }

    public String getTelephone() {
        return parseString(telephone, getDataSource());
    }

    public Point_DTO getPoint_dto() {
        return parsePointDTO(KEY_POINT, getDataSource());
    }

    public String getMf_hours_from() {
        return parseString(mf_hours_from, getDataSource());
    }

    public String getSs_hours_from() {
        return parseString(ss_hours_from, getDataSource());
    }

    public String getSs_hours_to() {
        return parseString(ss_hours_to, getDataSource());
    }

    public String getDistance() {
        return parseString(distance, getDataSource());
    }

    public String getMf_hours_to() {
        return parseString(mf_hours_to, getDataSource());
    }

    public ArrayList<Offer_DTO> getInplace_offers() {
        try {
            ArrayList<Offer_DTO> offer_dtos = new ArrayList<Offer_DTO>();
            JSONArray jsonArray = parseJSONArray(KEY_JSON_ARRAY_OFFER_INPLACE, getDataSource());

            for (int i = 0; i < jsonArray.length(); i++) {
                Offer_DTO offer_dto = new Offer_DTO();
                offer_dto.setDataSource(jsonArray.getJSONObject(i));
                offer_dtos.add(offer_dto);
            }
            return offer_dtos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int isInplace() {
        if (getInplace_offers().size() > 0) {
            return View.VISIBLE;
        }else{
            return View.GONE;
        }
    }

    public int isEspecial() {
        if (getSpecial_offers().size() > 0) {
            return View.VISIBLE;
        }else{
            return View.GONE;
        }
    }

    public int isCorporate() {
        if (getCorporate_offers().size() > 0 || getLegacy_offers().size() > 0) {
            return View.VISIBLE;
        }else{
            return View.GONE;
        }
    }

    public ArrayList<Offer_DTO> getSpecial_offers() {
        try {
            ArrayList<Offer_DTO> offer_dtos = new ArrayList<Offer_DTO>();
            JSONArray jsonArray = parseJSONArray(KEY_JSON_ARRAY_OFFER_SPECIAL, getDataSource());

            for (int i = 0; i < jsonArray.length(); i++) {
                Offer_DTO offer_dto = new Offer_DTO();
                offer_dto.setDataSource(jsonArray.getJSONObject(i));
                offer_dtos.add(offer_dto);
            }
            return offer_dtos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<CorporateOffer_DTO> getCorporate_offers() {
        try {
            ArrayList<CorporateOffer_DTO> corporateOffer_dtos = new ArrayList<CorporateOffer_DTO>();
            JSONArray jsonArray = parseJSONArray(KEY_JSON_ARRAY_CORPORATEOFFER, getDataSource());

            for (int i = 0; i < jsonArray.length(); i++) {
                CorporateOffer_DTO corporateOffer_dto = new CorporateOffer_DTO();
                corporateOffer_dto.setDataSource(jsonArray.getJSONObject(i));
                corporateOffer_dtos.add(corporateOffer_dto);
            }
            return corporateOffer_dtos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<CorporateOffer_DTO> getLegacy_offers() {
        try {
            ArrayList<CorporateOffer_DTO> corporateOffer_dtos = new ArrayList<CorporateOffer_DTO>();
            JSONArray jsonArray = parseJSONArray("legacy_corporate_offers", getDataSource());

            for (int i = 0; i < jsonArray.length(); i++) {
                CorporateOffer_DTO corporateOffer_dto = new CorporateOffer_DTO();
                corporateOffer_dto.setDataSource(jsonArray.getJSONObject(i));
                corporateOffer_dtos.add(corporateOffer_dto);
            }
            return corporateOffer_dtos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
