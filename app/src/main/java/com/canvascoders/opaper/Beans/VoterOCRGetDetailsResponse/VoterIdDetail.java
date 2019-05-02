
package com.canvascoders.opaper.Beans.VoterOCRGetDetailsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoterIdDetail {

    @SerializedName("voter_id_detail_id")
    @Expose
    private Integer voterIdDetailId;
    @SerializedName("voter_id_number")
    @Expose
    private String voterIdNumber;
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



    @SerializedName("back_side_file_name")
    @Expose
    private String backSideFileName;


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

    @SerializedName("back_side_file_url")
    @Expose
    private String backSideFileUrl;

    public Integer getVoterIdDetailId() {
        return voterIdDetailId;
    }

    public void setVoterIdDetailId(Integer voterIdDetailId) {
        this.voterIdDetailId = voterIdDetailId;
    }

    public String getVoterIdNumber() {
        return voterIdNumber;
    }

    public void setVoterIdNumber(String voterIdNumber) {
        this.voterIdNumber = voterIdNumber;
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
