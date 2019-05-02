
package com.canvascoders.opaper.Beans.PanCardOcrResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PanCardSubmitResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("pan_card_detail")
    @Expose
    private PanCardDetail panCardDetail;

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

    public PanCardDetail getPanCardDetail() {
        return panCardDetail;
    }

    public void setPanCardDetail(PanCardDetail panCardDetail) {
        this.panCardDetail = panCardDetail;
    }

}
