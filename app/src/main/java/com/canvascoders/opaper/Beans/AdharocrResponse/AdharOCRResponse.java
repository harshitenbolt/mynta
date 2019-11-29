
package com.canvascoders.opaper.Beans.AdharocrResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdharOCRResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("front_back_image_msg")
    @Expose
    private String FrontBackImageMessage;

    public String getFrontBackImageMessage() {
        return FrontBackImageMessage;
    }

    public void setFrontBackImageMessage(String frontBackImageMessage) {
        FrontBackImageMessage = frontBackImageMessage;
    }

    @SerializedName("is_front_ok")
    @Expose
    private Boolean isFrontOk;
    @SerializedName("is_back_ok")
    @Expose
    private Boolean isBackOk;
    @SerializedName("aadhar_card_detail")
    @Expose
    private AadharCardDetail aadharCardDetail;

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

    public Boolean getIsFrontOk() {
        return isFrontOk;
    }

    public void setIsFrontOk(Boolean isFrontOk) {
        this.isFrontOk = isFrontOk;
    }

    public Boolean getIsBackOk() {
        return isBackOk;
    }

    public void setIsBackOk(Boolean isBackOk) {
        this.isBackOk = isBackOk;
    }

    public AadharCardDetail getAadharCardDetail() {
        return aadharCardDetail;
    }

    public void setAadharCardDetail(AadharCardDetail aadharCardDetail) {
        this.aadharCardDetail = aadharCardDetail;
    }

}
