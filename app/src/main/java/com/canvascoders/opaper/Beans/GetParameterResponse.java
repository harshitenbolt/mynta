
package com.canvascoders.opaper.Beans;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetParameterResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("number_for_itr_filled")
    @Expose
    private String numberForItrFilled;

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

    public String getNumberForItrFilled() {
        return numberForItrFilled;
    }

    public void setNumberForItrFilled(String numberForItrFilled) {
        this.numberForItrFilled = numberForItrFilled;
    }

}
