package com.canvascoders.opaper.Beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MensaAlteration {


    boolean isSelected;
    String rate="";

   /* public MensaAlteration(boolean isSelected, String subStoreType, String subStoreTypeId) {
        this.isSelected = isSelected;
        this.subStoreType = subStoreType;
        this.subStoreTypeId = subStoreTypeId;
    }*/

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public MensaAlteration(boolean isSelected, String subStoreType,String idType) {
        this.isSelected = isSelected;
        this.subStoreType = subStoreType;
        this.idType = idType;
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
