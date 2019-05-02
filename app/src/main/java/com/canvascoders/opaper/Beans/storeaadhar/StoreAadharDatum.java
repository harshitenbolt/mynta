
package com.canvascoders.opaper.Beans.storeaadhar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreAadharDatum {

    @SerializedName("proccess_id")
    @Expose
    private String proccessId;

    /**
     * No args constructor for use in serialization
     * 
     */
    public StoreAadharDatum() {
    }

    /**
     * 
     * @param proccessId
     */
    public StoreAadharDatum(String proccessId) {
        super();
        this.proccessId = proccessId;
    }

    public String getProccessId() {
        return proccessId;
    }

    public void setProccessId(String proccessId) {
        this.proccessId = proccessId;
    }

}
