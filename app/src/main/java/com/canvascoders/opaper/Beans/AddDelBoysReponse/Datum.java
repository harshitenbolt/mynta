
package com.canvascoders.opaper.Beans.AddDelBoysReponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("proccess_id")
    @Expose
    private String proccessId;

    public String getProccessId() {
        return proccessId;
    }

    public void setProccessId(String proccessId) {
        this.proccessId = proccessId;
    }

}
