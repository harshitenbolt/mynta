
package com.canvascoders.opaper.Beans.DrivingLicenceDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrivingLicenceDetailResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("driving_licence_detail")
    @Expose
    private DrivingLicenceDetail drivingLicenceDetail;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DrivingLicenceDetail getDrivingLicenceDetail() {
        return drivingLicenceDetail;
    }

    public void setDrivingLicenceDetail(DrivingLicenceDetail drivingLicenceDetail) {
        this.drivingLicenceDetail = drivingLicenceDetail;
    }

}
