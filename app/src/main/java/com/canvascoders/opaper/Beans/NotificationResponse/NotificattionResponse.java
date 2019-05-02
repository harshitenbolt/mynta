package com.canvascoders.opaper.Beans.NotificationResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificattionResponse {
    //    {"response":"success","count":104,"isupdateavailable":true,"version":19}
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("isupdateavailable")
    @Expose
    private Boolean isupdateavailable;
    @SerializedName("version")
    @Expose
    private Integer version;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getIsupdateavailable() {
        return isupdateavailable;
    }

    public void setIsupdateavailable(Boolean isupdateavailable) {
        this.isupdateavailable = isupdateavailable;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ClassPojo [response = " + response + ", count = " + count + ", isupdateavailable = " + isupdateavailable + ", version = " + version + "]";
    }
}
