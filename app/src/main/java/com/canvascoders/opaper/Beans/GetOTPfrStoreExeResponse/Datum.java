
package com.canvascoders.opaper.Beans.GetOTPfrStoreExeResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("agent_id")
    @Expose
    private String agentId;
    @SerializedName("otp")
    @Expose
    private String otp;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}
