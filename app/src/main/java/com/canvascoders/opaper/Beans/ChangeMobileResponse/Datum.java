
package com.canvascoders.opaper.Beans.ChangeMobileResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("approval_mobile_number_id")
    @Expose
    private Integer approvalMobileNumberId;

    public Integer getApprovalMobileNumberId() {
        return approvalMobileNumberId;
    }

    public void setApprovalMobileNumberId(Integer approvalMobileNumberId) {
        this.approvalMobileNumberId = approvalMobileNumberId;
    }

}
