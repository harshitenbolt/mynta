package com.canvascoders.opaper.Beans.NotificationResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificattionResponse {
    //    {"response":"success","count":104,"isupdateavailable":true,"version":19}
    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("live_vendors_count")
    @Expose
    private int liveVendorCount;

    public int getShowAdharAPi() {
        return showAdharAPi;
    }

    public void setShowAdharAPi(int showAdharAPi) {
        this.showAdharAPi = showAdharAPi;
    }

    @SerializedName("show_aadhar_api")
    @Expose
    private int showAdharAPi;

    public int getAgentNotificationCount() {
        return agentNotificationCount;
    }

    public void setAgentNotificationCount(int agentNotificationCount) {
        this.agentNotificationCount = agentNotificationCount;
    }

    @SerializedName("agent_notification_count")
    @Expose
    private int agentNotificationCount;

    public String getIsMobileVerify() {
        return IsMobileVerify;
    }

    public void setIsMobileVerify(String isMobileVerify) {
        IsMobileVerify = isMobileVerify;
    }

    @SerializedName("is_mobile_verify")
    @Expose
    private String IsMobileVerify;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @SerializedName("mobile")
    @Expose
    private String mobile;

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    @SerializedName("task_count")
    @Expose
    private int taskCount;


    @SerializedName("in_procces_count")
    @Expose
    private int inProgressCount;

    public int getLiveVendorCount() {
        return liveVendorCount;
    }

    public void setLiveVendorCount(int liveVendorCount) {
        this.liveVendorCount = liveVendorCount;
    }

    public int getInProgressCount() {
        return inProgressCount;
    }

    public void setInProgressCount(int inProgressCount) {
        this.inProgressCount = inProgressCount;
    }

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("isupdateavailable")
    @Expose
    private Boolean isupdateavailable;

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @SerializedName("notice")
    @Expose
    private String notice;


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
