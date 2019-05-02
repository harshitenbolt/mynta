
package com.canvascoders.opaper.Beans.UpdatePanDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("pan_name")
    @Expose
    private String panName;
    @SerializedName("pan_no")
    @Expose
    private String panNo;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("pan")
    @Expose
    private String pan;

    public String getPanName() {
        return panName;
    }

    public void setPanName(String panName) {
        this.panName = panName;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

}
