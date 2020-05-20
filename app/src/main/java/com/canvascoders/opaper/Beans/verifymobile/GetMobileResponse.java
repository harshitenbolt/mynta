
package com.canvascoders.opaper.Beans.verifymobile;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMobileResponse {

    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("pan_matched_kiran_proccess_id")
    @Expose
    private Integer panmatchedkiranaprocessId;

    public Integer getPanmatchedkiranaprocessId() {
        return panmatchedkiranaprocessId;
    }

    public void setPanmatchedkiranaprocessId(Integer panmatchedkiranaprocessId) {
        this.panmatchedkiranaprocessId = panmatchedkiranaprocessId;
    }

    @SerializedName("data")
    @Expose
    private List<VerifyMobileDatum> data = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public GetMobileResponse() {
    }

    /**
     * 
     * @param response
     * @param responseCode
     * @param status
     * @param data
     */
    public GetMobileResponse(Integer responseCode, String response, String status, List<VerifyMobileDatum> data) {
        super();
        this.responseCode = responseCode;
        this.response = response;
        this.status = status;
        this.data = data;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
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

    public List<VerifyMobileDatum> getData() {
        return data;
    }

    public void setData(List<VerifyMobileDatum> data) {
        this.data = data;
    }

}
