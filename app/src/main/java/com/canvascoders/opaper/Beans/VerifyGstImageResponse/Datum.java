
package com.canvascoders.opaper.Beans.VerifyGstImageResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("gstn")
    @Expose
    private String gstn;

    public String getGstn() {
        return gstn;
    }

    public void setGstn(String gstn) {
        this.gstn = gstn;
    }

}
