
package com.canvascoders.opaper.Beans.SupportDetailResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SupportDetailResponse {

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
    private List<Details> data = null;

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

    public List<Details> getData() {
        return data;
    }

    public void setData(List<Details> data) {
        this.data = data;
    }

}
