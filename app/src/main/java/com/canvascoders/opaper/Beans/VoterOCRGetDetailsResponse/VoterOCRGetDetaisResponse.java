
package com.canvascoders.opaper.Beans.VoterOCRGetDetailsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoterOCRGetDetaisResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("voter_id_detail")
    @Expose
    private VoterIdDetail voterIdDetail;

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

    public VoterIdDetail getVoterIdDetail() {
        return voterIdDetail;
    }

    public void setVoterIdDetail(VoterIdDetail voterIdDetail) {
        this.voterIdDetail = voterIdDetail;
    }

}
