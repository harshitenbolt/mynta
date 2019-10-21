
package com.canvascoders.opaper.Beans.GetVendorInvoiceList;

import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetVendorInvoiceDetails {

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
    private List<Map<String, Map<String,List<MensaDelivery>>>> data = null;


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

    public List<Map<String, Map<String, List<MensaDelivery>>>> getData() {
        return data;
    }

    public void setData(List<Map<String, Map<String, List<MensaDelivery>>>> data) {
        this.data = data;
    }

/*public List<Map<String, Datum>> getData() {
        return data;
    }

    public void setData(List<Map<String, Datum>> data) {
        this.data = data;
    }*/

    public Map<String, Integer> getResult() {
        return result;
    }

    public void setResult(Map<String, Integer> result) {
        this.result = result;
    }

    @SerializedName("store_list")
    @Expose
    private Map<String, Integer> result;


}
