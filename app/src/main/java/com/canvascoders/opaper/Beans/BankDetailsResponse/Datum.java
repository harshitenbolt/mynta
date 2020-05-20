
package com.canvascoders.opaper.Beans.BankDetailsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("basic_details_id")
    @Expose
    private Integer basicDetailsId;
    @SerializedName("payee_name")
    @Expose
    private String payeeName;
    @SerializedName("ifsc")
    @Expose
    private String ifsc;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("bank_branch_name")
    @Expose
    private String bankBranchName;
    @SerializedName("bank_address")
    @Expose
    private String bankAddress;
    @SerializedName("bank_ac")
    @Expose
    private String bankAc;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("cancelled_cheque")
    @Expose
    private String cancelledCheque;


    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    @SerializedName("store_name")
    @Expose
    private String StoreName;

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

    public Integer getBasicDetailsId() {
        return basicDetailsId;
    }

    public void setBasicDetailsId(Integer basicDetailsId) {
        this.basicDetailsId = basicDetailsId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankAc() {
        return bankAc;
    }

    public void setBankAc(String bankAc) {
        this.bankAc = bankAc;
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

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getCancelledCheque() {
        return cancelledCheque;
    }

    public void setCancelledCheque(String cancelledCheque) {
        this.cancelledCheque = cancelledCheque;
    }

}
