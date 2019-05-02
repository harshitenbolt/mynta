
package com.canvascoders.opaper.Beans.GenerateResetPWResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Datum implements Serializable{

    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("agent_detail")
    @Expose
    private AgentDetail agentDetail;

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public AgentDetail getAgentDetail() {
        return agentDetail;
    }

    public void setAgentDetail(AgentDetail agentDetail) {
        this.agentDetail = agentDetail;
    }

}
