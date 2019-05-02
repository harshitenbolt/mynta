
package com.canvascoders.opaper.Beans.PancardVerifyResponse;

import com.canvascoders.opaper.Beans.ErrorResponsePan.Validation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonResponse {

    /*private Integer responseCode;
    private String response;
    private String status;
    private List<AadharVerifyDatum> data = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    public List<AadharVerifyDatum> getData() {
        return data;
    }

    public void setData(List<AadharVerifyDatum> data) {
        this.data = data;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }*/

    private String response;

    private Integer responseCode;

    private String status;

    private List<CommonDatum> data;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CommonDatum> getData() {
        return data;
    }

    public void setData(List<CommonDatum> data) {
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
    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", responseCode = " + responseCode + ", status = " + status + ", data = " + data + "]";
    }

}
