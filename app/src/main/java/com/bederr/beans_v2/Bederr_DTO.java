package com.bederr.beans_v2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Gantz on 22/12/14.
 */
public class Bederr_DTO {

    private JSONObject datasource;
    private JSONArray datasourcearray;

    public static final String KEY_QUESTION = "";
    public static final String KEY_ANSWER = "";
    public static final String KEY_POINT = "location";
    public static final String KEY_PLACE = "";
    public static final String KEY_LISTING = "";
    public static final String KEY_OFFER = "";
    public static final String KEY_CORPORATEOFFER = "";

    public static final String KEY_JSON_ARRAY_QUESTION = "";
    public static final String KEY_JSON_ARRAY_ANSWER = "";
    public static final String KEY_JSON_ARRAY_POINT = "location";
    public static final String KEY_JSON_ARRAY_PLACE = "places";
    public static final String KEY_JSON_ARRAY_LISTING = "";
    public static final String KEY_JSON_ARRAY_OFFER_INPLACE = "inplace_offers";
    public static final String KEY_JSON_ARRAY_OFFER_SPECIAL = "special_offers";
    public static final String KEY_JSON_ARRAY_CORPORATEOFFER = "corporate_offers";

    /**
     * Parse Data(JSON) to CorporateOffer_DTO
     *
     * @param key
     * @param dataSource
     * @return
     */
    public CorporateOffer_DTO parseCorporateOfferDTO(String key, JSONObject dataSource) {
        try {
            if (!dataSource.isNull(key)) {
                CorporateOffer_DTO corporateOffer_dto = new CorporateOffer_DTO();
                corporateOffer_dto.setDataSource(dataSource.getJSONObject(key));
                return corporateOffer_dto;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse Data(JSON) to Offer_DTO
     *
     * @param key
     * @param dataSource
     * @return
     */
    public Offer_DTO parseOfferDTO(String key, JSONObject dataSource) {
        try {
            if (!dataSource.isNull(key)) {
                Offer_DTO offer_dto = new Offer_DTO();
                offer_dto.setDataSource(dataSource.getJSONObject(key));
                return offer_dto;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse Data(JSON) to Listing_DTO
     *
     * @param key
     * @param dataSource
     * @return
     */
    public Listing_DTO parseListingDTO(String key, JSONObject dataSource) {
        try {
            if (!dataSource.isNull(key)) {
                Listing_DTO listing_dto = new Listing_DTO();
                listing_dto.setDataSource(dataSource.getJSONObject(key));
                return listing_dto;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse Data(JSON) to Place_DTO
     *
     * @param key
     * @param dataSource
     * @return
     */
    public Place_DTO parsePlaceDTO(String key, JSONObject dataSource) {
        try {
            if (!dataSource.isNull(key)) {
                Place_DTO place_dto = new Place_DTO();
                place_dto.setDataSource(dataSource.getJSONObject(key));
                return place_dto;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse Data(JSON) to Place_DTO
     *
     * @return
     */
    public ArrayList<Place_DTO> parsePlaceDTOs() {
        ArrayList<Place_DTO> place_dtos = new ArrayList<Place_DTO>();
        try {
            JSONArray array = getDataSource().getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                Place_DTO place_dto = new Place_DTO();
                place_dto.setDataSource(array.getJSONObject(i));
                place_dtos.add(place_dto);
            }
            return place_dtos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse Data(JSON) to Place_DTO
     *
     * @return
     */
    public ArrayList<Country_DTO> parseCountrys() {
        ArrayList<Country_DTO> country_dtos = new ArrayList<Country_DTO>();
        try {
            JSONArray array = getDatasourcearray();
            for (int i = 0; i < array.length(); i++) {
                Country_DTO country_dto = new Country_DTO();
                country_dto.setDataSource(array.getJSONObject(i));
                country_dtos.add(country_dto);
            }
            return country_dtos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse Data(JSON) to Benefit_Program_DTO
     *
     * @return
     */
    public ArrayList<Benefit_Program_DTO> parseBenefitProgramDTOs() {
        ArrayList<Benefit_Program_DTO> benefit_program_dtos = new ArrayList<Benefit_Program_DTO>();
        try {
            JSONArray array = getDataSource().getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                Benefit_Program_DTO benefit_program_dto = new Benefit_Program_DTO();
                benefit_program_dto.setDataSource(array.getJSONObject(i));
                benefit_program_dtos.add(benefit_program_dto);
            }
            return benefit_program_dtos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse Data(JSON) to Category_DTO
     *
     * @return
     */
    public ArrayList<Category_DTO> parseCategoryDTOs() {
        ArrayList<Category_DTO> category_dtos = new ArrayList<Category_DTO>();
        try {
            for (int i = 0; i < getDatasourcearray().length(); i++) {
                Category_DTO category_dto = new Category_DTO();
                category_dto.setDataSource(getDatasourcearray().getJSONObject(i));
                category_dtos.add(category_dto);
            }
            return category_dtos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse Data(JSON) to Locality_DTO
     *
     * @return
     */
    public ArrayList<Locality_DTO> parseLocalityDTOs() {
        ArrayList<Locality_DTO> locality_dtos = new ArrayList<Locality_DTO>();
        try {
            for (int i = 0; i < getDatasourcearray().length(); i++) {
                Locality_DTO locality_dto = new Locality_DTO();
                locality_dto.setDataSource(getDatasourcearray().getJSONObject(i));
                locality_dtos.add(locality_dto);
            }
            return locality_dtos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse Data(JSON) to Listing_DTO
     *
     * @return
     */
    public ArrayList<Listing_DTO> parseListingDTOs() {
        ArrayList<Listing_DTO> listing_dtos = new ArrayList<Listing_DTO>();
        try {
            JSONArray array = getDataSource().getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                Listing_DTO listing_dto = new Listing_DTO();
                listing_dto.setDataSource(array.getJSONObject(i));
                listing_dtos.add(listing_dto);
            }
            return listing_dtos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse Data(JSON) to Listing_DTO
     *
     * @return
     */
    public ArrayList<Question_DTO> parseQuestionDTOs() {
        ArrayList<Question_DTO> question_dtos = new ArrayList<Question_DTO>();
        try {
            JSONArray array = getDataSource().getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                Question_DTO question_dto = new Question_DTO();
                question_dto.setDataSource(array.getJSONObject(i));
                question_dtos.add(question_dto);
            }
            return question_dtos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse Data(JSON) to Listing_DTO
     *
     * @return
     */
    public ArrayList<Answer_DTO> parseAnswerDTOs() {
        ArrayList<Answer_DTO> answer_dtos = new ArrayList<Answer_DTO>();
        try {
            JSONArray array = getDataSource().getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                Answer_DTO answer_dto = new Answer_DTO();
                answer_dto.setDataSource(array.getJSONObject(i));
                answer_dtos.add(answer_dto);
            }
            return answer_dtos;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse Data(JSON) to Point_DTO
     *
     * @param key
     * @param dataSource
     * @return
     */
    public Point_DTO parsePointDTO(String key, JSONObject dataSource) {
        try {
            if (!dataSource.isNull(key)) {
                Point_DTO point_dto = new Point_DTO();
                point_dto.setDataSource(dataSource.getJSONObject(key));
                return point_dto;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse Data(JSON) to Answer_DTO
     *
     * @param key
     * @param dataSource
     * @return
     */
    public Answer_DTO parseAnswerDTO(String key, JSONObject dataSource) {
        try {
            if (!dataSource.isNull(key)) {
                Answer_DTO answer_dto = new Answer_DTO();
                answer_dto.setDataSource(dataSource.getJSONObject(key));
                return answer_dto;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse Data(JSON) to Question_DTO
     *
     * @param key
     * @param dataSource
     * @return
     */
    public Question_DTO parseQuestionDTO(String key, JSONObject dataSource) {
        try {
            if (!dataSource.isNull(key)) {
                Question_DTO question_dto = new Question_DTO();
                question_dto.setDataSource(dataSource.getJSONObject(key));
                return question_dto;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse String
     *
     * @param key
     * @param dataSource
     * @return
     */
    public String parseString(String key, JSONObject dataSource) {
        try {
            if (!dataSource.isNull(key)) {
                return dataSource.getString(key);
            } else {
                return "NULL";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "NULL";
        }
    }

    /**
     * Parse Int
     *
     * @param key
     * @param dataSource
     * @return
     */
    public int parseInt(String key, JSONObject dataSource) {
        try {
            if (!dataSource.isNull(key)) {
                return dataSource.getInt(key);
            } else {
                return -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Parse Boolean
     *
     * @param key
     * @param dataSource
     * @return
     */
    public boolean parseBooelan(String key, JSONObject dataSource) {
        try {
            if (!dataSource.isNull(key)) {
                return dataSource.getBoolean(key);
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Parse JSON
     *
     * @param key
     * @param dataSource
     * @return
     */
    public JSONObject parseJSON(String key, JSONObject dataSource) {
        try {
            if (!dataSource.isNull(key)) {
                return dataSource.getJSONObject(key);
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse JSONArray
     *
     * @param key
     * @param dataSource
     * @return
     */
    public JSONArray parseJSONArray(String key, JSONObject dataSource) {
        try {
            if (!dataSource.isNull(key)) {
                return dataSource.getJSONArray(key);
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Set Datasource API
     *
     * @param datasource
     */
    public void setDataSource(JSONObject datasource) {
        this.datasource = datasource;
    }

    /**
     * Get Datasource API
     *
     * @return
     */
    public JSONObject getDataSource() {
        return datasource;
    }

    public JSONArray getDatasourcearray() {
        return datasourcearray;
    }

    public void setDatasourcearray(JSONArray datasourcearray) {
        this.datasourcearray = datasourcearray;
    }

}