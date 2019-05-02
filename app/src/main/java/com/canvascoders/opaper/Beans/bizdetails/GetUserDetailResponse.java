
package com.canvascoders.opaper.Beans.bizdetails;

import java.util.List;

import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserDetailResponse {

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
    private List<UserDetailDatum> data = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public GetUserDetailResponse() {
    }

    /**
     * 
     * @param response
     * @param responseCode
     * @param status
     * @param data
     */
    public GetUserDetailResponse(Integer responseCode, String response, String status, List<UserDetailDatum> data) {
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

    public List<UserDetailDatum> getData() {
        return data;
    }

    public void setData(List<UserDetailDatum> data) {
        this.data = data;
    }

    @SerializedName("validation")
    @Expose
    private Validation validation;



    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

}
