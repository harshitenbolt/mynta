
package com.canvascoders.opaper.Beans.getMerakApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMerakResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("marak_detail")
    @Expose
    private MarakDetail marakDetail;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MarakDetail getMarakDetail() {
        return marakDetail;
    }

    public void setMarakDetail(MarakDetail marakDetail) {
        this.marakDetail = marakDetail;
    }

}
