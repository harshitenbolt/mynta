
package com.canvascoders.opaper.Beans.otp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetOtpDatum {

    @SerializedName("otp")
    @Expose
    private String otp;

    /**
     * No args constructor for use in serialization
     */
    public GetOtpDatum() {
    }

    /**
     * @param otp
     */
    public GetOtpDatum(String otp) {
        super();
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}
