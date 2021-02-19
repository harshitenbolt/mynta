package com.canvascoders.opaper.Beans;

import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubStoreType {
    @SerializedName("store_type")
    @Expose
    private String storeType;
    @SerializedName("rate")
    @Expose
    private String rate = "0";
    @SerializedName("is_approved")
    @Expose
    private String isApproved;
    @SerializedName("store_type_id")
    @Expose
    private Integer storeTypeId;
    boolean isSelected;


    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public Integer getStoreTypeId() {
        return storeTypeId;
    }

    public void setStoreTypeId(Integer storeTypeId) {
        this.storeTypeId = storeTypeId;
    }



   /* public MensaAlteration(boolean isSelected, String subStoreType, String subStoreTypeId) {
        this.isSelected = isSelected;
        this.subStoreType = subStoreType;
        this.subStoreTypeId = subStoreTypeId;
    }*/


    public SubStoreType(boolean isSelected, String subStoreType, Integer storeTypeId, String rate) {
        this.isSelected = isSelected;
        this.subStoreType = subStoreType;
        this.storeType = storeType;
        this.rate = rate;
        this.storeTypeId = storeTypeId;
    }


    private String subStoreType;

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    private String idType;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getSubStoreType() {
        return subStoreType;
    }

    public void setSubStoreType(String subStoreType) {
        this.subStoreType = subStoreType;
    }
}
