package com.canvascoders.opaper.Beans;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class StoreTypeBean {


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
    private boolean selected = false;

    public StoreTypeBean(JSONObject o) {

        try {
            this.setIsApproved(o.getString("is_approved"));
            this.setStoreType(o.getString("store_type"));
            if (o.getString("rate") != null && o.getString("rate").length() > 0)
                this.setRate(o.getString("rate"));
            else
                this.setRate("0");
            this.setStoreTypeId(o.getInt("store_type_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
