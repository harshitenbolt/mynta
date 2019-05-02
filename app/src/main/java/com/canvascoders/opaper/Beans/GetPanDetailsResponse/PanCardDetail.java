
package com.canvascoders.opaper.Beans.GetPanDetailsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PanCardDetail {

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

    @SerializedName("pan_card_detail_id")
    @Expose
    private Integer PanCardDetailId;




    public String getPanCardNumber() {
        return panCardNumber;
    }

    public Integer getPanCardDetailId() {
        return PanCardDetailId;
    }

    public void setPanCardDetailId(Integer panCardDetailId) {
        PanCardDetailId = panCardDetailId;
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
