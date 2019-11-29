
package com.canvascoders.opaper.Beans.AdharocrResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AadharCardDetail {

    @SerializedName("aadhar_card_detail_id")
    @Expose
    private Integer aadharCardDetailId;
    @SerializedName("aadhar_card_number")
    @Expose
    private String aadharCardNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("birth_date")
    @Expose
    private String birthDate;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("file_url")
    @Expose
    private String fileUrl;
    @SerializedName("back_side_file_name")
    @Expose
    private String backSideFileName;
    @SerializedName("back_side_file_url")
    @Expose
    private String backSideFileUrl;

    public Integer getAadharCardDetailId() {
        return aadharCardDetailId;
    }

    public void setAadharCardDetailId(Integer aadharCardDetailId) {
        this.aadharCardDetailId = aadharCardDetailId;
    }

    public String getAadharCardNumber() {
        return aadharCardNumber;
    }

    public void setAadharCardNumber(String aadharCardNumber) {
        this.aadharCardNumber = aadharCardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getBackSideFileName() {
        return backSideFileName;
    }

    public void setBackSideFileName(String backSideFileName) {
        this.backSideFileName = backSideFileName;
    }

    public String getBackSideFileUrl() {
        return backSideFileUrl;
    }

    public void setBackSideFileUrl(String backSideFileUrl) {
        this.backSideFileUrl = backSideFileUrl;
    }

}
