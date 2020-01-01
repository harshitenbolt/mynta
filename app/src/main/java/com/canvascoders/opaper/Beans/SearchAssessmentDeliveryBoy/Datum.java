
package com.canvascoders.opaper.Beans.SearchAssessmentDeliveryBoy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("store_name")
    @Expose
    private String storeName;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

}
