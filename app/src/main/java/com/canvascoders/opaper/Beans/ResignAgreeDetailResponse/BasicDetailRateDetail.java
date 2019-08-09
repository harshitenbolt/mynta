
package com.canvascoders.opaper.Beans.ResignAgreeDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasicDetailRateDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("basic_detail_id")
    @Expose
    private Integer basicDetailId;
    @SerializedName("store_type")
    @Expose
    private String storeType;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("is_addendum_sign")
    @Expose
    private String isAddendumSign;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("oracle_csv")
    @Expose
    private String oracleCsv;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("is_active")
    @Expose
    private String isActive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProccessId() {
        return proccessId;
    }

    public void setProccessId(Integer proccessId) {
        this.proccessId = proccessId;
    }

    public Integer getBasicDetailId() {
        return basicDetailId;
    }

    public void setBasicDetailId(Integer basicDetailId) {
        this.basicDetailId = basicDetailId;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getIsAddendumSign() {
        return isAddendumSign;
    }

    public void setIsAddendumSign(String isAddendumSign) {
        this.isAddendumSign = isAddendumSign;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getOracleCsv() {
        return oracleCsv;
    }

    public void setOracleCsv(String oracleCsv) {
        this.oracleCsv = oracleCsv;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}
