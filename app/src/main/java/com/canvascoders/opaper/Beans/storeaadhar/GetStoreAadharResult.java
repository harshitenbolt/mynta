
package com.canvascoders.opaper.Beans.storeaadhar;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetStoreAadharResult {

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
    private List<StoreAadharDatum> data = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public GetStoreAadharResult() {
    }

    /**
     * 
     * @param response
     * @param responseCode
     * @param status
     * @param data
     */
    public GetStoreAadharResult(Integer responseCode, String response, String status, List<StoreAadharDatum> data) {
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

    public List<StoreAadharDatum> getData() {
        return data;
    }

    public void setData(List<StoreAadharDatum> data) {
        this.data = data;
    }

}
