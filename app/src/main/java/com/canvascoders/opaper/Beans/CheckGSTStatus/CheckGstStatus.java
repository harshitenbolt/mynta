
package com.canvascoders.opaper.Beans.CheckGSTStatus;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckGstStatus {

    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public boolean getSendLink() {
        return sendLink;
    }

    public void setSendLink(boolean sendLink) {
        this.sendLink = sendLink;
    }

    @SerializedName(("send_link"))
    @Expose
    private boolean sendLink;

    @SerializedName(("approval_gst_id"))
    @Expose
    private int approvalGSTId;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public int getApprovalGSTId() {
        return approvalGSTId;
    }

    public void setApprovalGSTId(int approvalGSTId) {
        this.approvalGSTId = approvalGSTId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
