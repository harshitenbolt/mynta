
package com.canvascoders.opaper.Beans.DrivingLicenceDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrivingLicenceDetail {

    @SerializedName("driving_licence_detail_id")
    @Expose
    private Integer drivingLicenceDetailId;
    @SerializedName("driving_licence_number")
    @Expose
    private String drivingLicenceNumber;
    @SerializedName("date_of_issue")
    @Expose
    private String dateofissue;

    public String getDateofissue() {
        return dateofissue;
    }

    public void setDateofissue(String dateofissue) {
        this.dateofissue = dateofissue;
    }

    public String getDateofExpiry() {
        return dateofExpiry;
    }

    public void setDateofExpiry(String dateofExpiry) {
        this.dateofExpiry = dateofExpiry;
    }

    @SerializedName("date_of_expiry")
    @Expose
    private String dateofExpiry;


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

    public Integer getDrivingLicenceDetailId() {
        return drivingLicenceDetailId;
    }

    public void setDrivingLicenceDetailId(Integer drivingLicenceDetailId) {
        this.drivingLicenceDetailId = drivingLicenceDetailId;
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

}
