
package com.canvascoders.opaper.Beans;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetVendorTypeDetails {

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
    private List<String> data = null;
    @SerializedName("store_type_config")
    @Expose
    private List<String> storeTypeConfig = null;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public List<String> getStoreTypeConfig() {
        return storeTypeConfig;
    }

    public void setStoreTypeConfig(List<String> storeTypeConfig) {
        this.storeTypeConfig = storeTypeConfig;
    }

}
