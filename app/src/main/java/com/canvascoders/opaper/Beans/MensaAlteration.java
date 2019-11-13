package com.canvascoders.opaper.Beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MensaAlteration {


    boolean isSelected;

    public MensaAlteration(boolean isSelected, String subStoreType, String subStoreTypeId) {
        this.isSelected = isSelected;
        this.subStoreType = subStoreType;
        this.subStoreTypeId = subStoreTypeId;
    }

    @SerializedName("sub_store_type")
    @Expose
    private String subStoreType;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @SerializedName("sub_store_type_id")
    @Expose
    private String subStoreTypeId;

    public String getSubStoreType() {
        return subStoreType;
    }

    public void setSubStoreType(String subStoreType) {
        this.subStoreType = subStoreType;
    }

    public String getSubStoreTypeId() {
        return subStoreTypeId;
    }

    public void setSubStoreTypeId(String subStoreTypeId) {
        this.subStoreTypeId = subStoreTypeId;
    }

}
