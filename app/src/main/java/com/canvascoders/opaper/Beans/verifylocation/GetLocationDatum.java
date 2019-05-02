
package com.canvascoders.opaper.Beans.verifylocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLocationDatum {

    @SerializedName("proccess_id")
    @Expose
    private String proccessId;

    /**
     * No args constructor for use in serialization
     * 
     */
    public GetLocationDatum() {
    }

    /**
     * 
     * @param proccessId
     */
    public GetLocationDatum(String proccessId) {
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
