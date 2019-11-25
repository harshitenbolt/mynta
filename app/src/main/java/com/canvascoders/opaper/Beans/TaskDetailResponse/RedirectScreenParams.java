
package com.canvascoders.opaper.Beans.TaskDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RedirectScreenParams {

    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("is_addendum_rate_edit")
    @Expose
    private String isAddendumRateEdit;

    public Integer getProccessId() {
        return proccessId;
    }

    public void setProccessId(Integer proccessId) {
        this.proccessId = proccessId;
    }

    public String getIsAddendumRateEdit() {
        return isAddendumRateEdit;
    }

    public void setIsAddendumRateEdit(String isAddendumRateEdit) {
        this.isAddendumRateEdit = isAddendumRateEdit;
    }

}
