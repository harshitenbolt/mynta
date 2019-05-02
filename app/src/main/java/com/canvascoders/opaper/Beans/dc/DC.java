package com.canvascoders.opaper.Beans.dc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by piyush on 26/03/2018.
 */

public class DC {

    @SerializedName("dc")
    @Expose
    private String dc;

    /**
     * No args constructor for use in serialization
     *
     */
    public DC() {
    }

    /**
     *
     * @param dc
     */
    public DC(String dc) {
        super();
        this.dc = dc;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }
}
