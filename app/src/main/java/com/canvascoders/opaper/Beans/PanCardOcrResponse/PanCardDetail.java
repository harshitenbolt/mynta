
package com.canvascoders.opaper.Beans.PanCardOcrResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PanCardDetail {

    @SerializedName("pan_card_detail_id")
    @Expose
    private String panCardDetailId;
    @SerializedName("new_pan_card_detail_id")
    @Expose
    private Integer newPanCardDetailId;
    @SerializedName("pan_card_number")
    @Expose
    private String panCardNumber;
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

    public String getPanCardDetailId() {
        return panCardDetailId;
    }

    public void setPanCardDetailId(String panCardDetailId) {
        this.panCardDetailId = panCardDetailId;
    }

    public Integer getNewPanCardDetailId() {
        return newPanCardDetailId;
    }

    public void setNewPanCardDetailId(Integer newPanCardDetailId) {
        this.newPanCardDetailId = newPanCardDetailId;
    }

    public String getPanCardNumber() {
        return panCardNumber;
    }

    public void setPanCardNumber(String panCardNumber) {
        this.panCardNumber = panCardNumber;
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
