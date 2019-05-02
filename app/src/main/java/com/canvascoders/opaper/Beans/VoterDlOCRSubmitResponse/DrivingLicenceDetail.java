
package com.canvascoders.opaper.Beans.VoterDlOCRSubmitResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrivingLicenceDetail {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("request_id")
    @Expose
    private Integer requestId;
    @SerializedName("driving_licence_detail_id")
    @Expose
    private String drivingLicenceDetailId;
    @SerializedName("app_name")
    @Expose
    private String appName;
    @SerializedName("driving_licence_number")
    @Expose
    private String drivingLicenceNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("birth_date")
    @Expose
    private String birthDate;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("file_url")
    @Expose
    private String fileUrl;
    @SerializedName("from_user_side")
    @Expose
    private String fromUserSide;
    @SerializedName("is_active")
    @Expose
    private String isActive;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public String getDrivingLicenceDetailId() {
        return drivingLicenceDetailId;
    }

    public void setDrivingLicenceDetailId(String drivingLicenceDetailId) {
        this.drivingLicenceDetailId = drivingLicenceDetailId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDrivingLicenceNumber() {
        return drivingLicenceNumber;
    }

    public void setDrivingLicenceNumber(String drivingLicenceNumber) {
        this.drivingLicenceNumber = drivingLicenceNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFromUserSide() {
        return fromUserSide;
    }

    public void setFromUserSide(String fromUserSide) {
        this.fromUserSide = fromUserSide;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}
