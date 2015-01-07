package com.bederr.beans;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.bederr.session.Session_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gantz on 5/07/14.
 */
public class Local_DTO implements Parcelable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private transient JSONObject jsonObject;
    private int mData;

    public Local_DTO(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {}

    public static final Creator<Local_DTO> CREATOR = new Creator<Local_DTO>() {
        public Local_DTO createFromParcel(Parcel in) {
            return new Local_DTO(in);
        }
        public Local_DTO[] newArray(int size) {
            return new Local_DTO[size];
        }
    };

    private Local_DTO(Parcel in) {
        mData = in.readInt();
    }

    public int isAzul() throws JSONException {
        JSONArray jsonArray_retail = jsonObject.getJSONArray("ListaCuponesRetail");
        if (jsonArray_retail.length() > 0) {
            for (int i = 0; i < jsonArray_retail.length(); i++) {
                String tipo_cupon = jsonArray_retail.getJSONObject(i).getString("Beneficio");
                if (tipo_cupon.matches("0")) {
                    return View.VISIBLE;
                }
            }
        }
      return View.GONE;
    }

    public int isVerde() throws JSONException {
        JSONArray jsonArray_retail = jsonObject.getJSONArray("ListaCuponesRetail");
        if (jsonArray_retail.length() > 0) {
            for (int i = 0; i < jsonArray_retail.length(); i++) {
                String tipo_cupon = jsonArray_retail.getJSONObject(i).getString("Beneficio");
                if (tipo_cupon.matches("1")) {
                    return View.VISIBLE;
                }
            }
        }
        return View.GONE;
    }

    public int isPlomo(Context context) throws JSONException {
        Session_Manager session_manager = new Session_Manager(context);
        if(session_manager.isLogin()){
            JSONArray jsonArray_life = jsonObject.getJSONArray("ListaCuponesLife");
            if(jsonArray_life.length() > 0){
                return View.VISIBLE;
            }else{
                return View.GONE;
            }
        }else{
            return View.GONE;
        }
    }
}
