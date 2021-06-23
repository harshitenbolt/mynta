
package com.canvascoders.opaper.Beans.getITrResponse;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Datum {

    @SerializedName("assessment_year")
    @Expose
    private String assessmentYear;
    @SerializedName("financial_year")
    @Expose
    private String financialYear;

    private boolean selectedImage = false;

    public boolean isSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(boolean selectedImage) {
        this.selectedImage = selectedImage;
    }

    public String getItrNumber() {
        return itrNumber;
    }

    public void setItrNumber(String itrNumber) {
        this.itrNumber = itrNumber;
    }

    @SerializedName("itrNumber")
    @Expose
    private String itrNumber;

    public String getDateofITR() {
        return dateofITR;
    }

    public void setDateofITR(String dateofITR) {
        this.dateofITR = dateofITR;
    }

    @SerializedName("dateofITR")
    @Expose
    private String dateofITR;

    @SerializedName("image")
    @Expose
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAssessmentYear() {
        return assessmentYear;
    }

    public void setAssessmentYear(String assessmentYear) {
        this.assessmentYear = assessmentYear;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

}
