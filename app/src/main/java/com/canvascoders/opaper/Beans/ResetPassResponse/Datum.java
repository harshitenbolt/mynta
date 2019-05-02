
package com.canvascoders.opaper.Beans.ResetPassResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("agent_detail")
    @Expose
    private AgentDetail agentDetail;

    public AgentDetail getAgentDetail() {
        return agentDetail;
    }

    public void setAgentDetail(AgentDetail agentDetail) {
        this.agentDetail = agentDetail;
    }

}
