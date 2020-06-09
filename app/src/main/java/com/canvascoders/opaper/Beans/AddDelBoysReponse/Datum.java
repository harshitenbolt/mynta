
package com.canvascoders.opaper.Beans.AddDelBoysReponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("proccess_id")
    @Expose
    private String proccessId;

    public Integer getIsDcDexterPresent() {
        return isDcDexterPresent;
    }

    public void setIsDcDexterPresent(Integer isDcDexterPresent) {
        this.isDcDexterPresent = isDcDexterPresent;
    }

    @SerializedName("is_dc_dexter_present")
    @Expose
    private Integer isDcDexterPresent;

    public String getProccessId() {
        return proccessId;
    }

    public void setProccessId(String proccessId) {
        this.proccessId = proccessId;
    }

}
