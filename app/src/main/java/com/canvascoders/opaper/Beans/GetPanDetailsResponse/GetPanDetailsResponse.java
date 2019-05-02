
package com.canvascoders.opaper.Beans.GetPanDetailsResponse;

import com.canvascoders.opaper.Beans.GetPanDetailsResponse.PanCardDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPanDetailsResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("pan_card_detail")
    @Expose
    private PanCardDetail panCardDetail;

    @SerializedName("re_select_image")
    @Expose
    private String ReselectImage;

    public String getReselectImage() {
        return ReselectImage;
    }

    public void setReselectImage(String reselectImage) {
        ReselectImage = reselectImage;
    }

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
