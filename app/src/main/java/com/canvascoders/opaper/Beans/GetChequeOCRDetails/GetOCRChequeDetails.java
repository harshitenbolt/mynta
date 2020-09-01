
package com.canvascoders.opaper.Beans.GetChequeOCRDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetOCRChequeDetails {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("re_select_image")
    @Expose
    private String reSelectImage;
    @SerializedName("cheque_detail")
    @Expose
    private ChequeDetail chequeDetail;

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

    public String getReSelectImage() {
        return reSelectImage;
    }

    public void setReSelectImage(String reSelectImage) {
        this.reSelectImage = reSelectImage;
    }

    public ChequeDetail getChequeDetail() {
        return chequeDetail;
    }

    public void setChequeDetail(ChequeDetail chequeDetail) {
        this.chequeDetail = chequeDetail;
    }

}
