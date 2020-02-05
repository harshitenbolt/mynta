
package com.canvascoders.opaper.Beans.getMerakApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarakDetail {

    @SerializedName("marak_detail_id")
    @Expose
    private Integer marakDetailId;

    public Integer getMarakDetailId() {
        return marakDetailId;
    }

    public void setMarakDetailId(Integer marakDetailId) {
        this.marakDetailId = marakDetailId;
    }

}
