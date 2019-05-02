
package com.canvascoders.opaper.Beans.bizdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailDatum {

    @SerializedName("proccess_id")
    @Expose
    private String proccessId;

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserDetailDatum() {
    }

    /**
     * 
     * @param proccessId
     */
    public UserDetailDatum(String proccessId) {
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
