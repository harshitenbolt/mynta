
package com.canvascoders.opaper.Beans;


import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;


@SuppressWarnings("unused")
public class VendorBankDetail {

    @SerializedName("bank_ac")
    private String mBankAc;
    @SerializedName("bank_address")
    private String mBankAddress;
    @SerializedName("bank_branch_name")
    private String mBankBranchName;
    @SerializedName("bank_name")
    private String mBankName;
    @SerializedName("basic_details_id")
    private int mBasicDetailsId;
    @SerializedName("cancelled_cheque")
    private String mCancelledCheque;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("deleted")
    private String mDeleted;
    @SerializedName("id")
    private int mId;
    @SerializedName("ifsc")
    private String mIfsc;
    @SerializedName("payee_name")
    private String mPayeeName;
    @SerializedName("proccess_id")
    private int mProccessId;
    @SerializedName("updated_at")
    private String mUpdatedAt;

    public VendorBankDetail(JSONObject jsonObject) {
        this.setBankAc(jsonObject.optString("bank_ac"));
        this.setBankBranchName(jsonObject.optString("bank_branch_name"));
        this.setBankAddress(jsonObject.optString("bank_address"));
        this.setBankBranchName(jsonObject.optString("bank_branch_name"));
        this.setBasicDetailsId(jsonObject.optInt("basic_details_id"));
        this.setId(jsonObject.optInt("id"));
        this.setIfsc(jsonObject.optString("ifsc"));
        this.setPayeeName(jsonObject.optString("payee_name"));
        this.setProccessId(jsonObject.optInt("proccess_id"));
        this.setCancelledCheque(jsonObject.optString("cancelled_cheque"));

    }

    public String getBankAc() {
        return mBankAc;
    }

    public void setBankAc(String bankAc) {
        mBankAc = bankAc;
    }

    public String getBankAddress() {
        return mBankAddress;
    }

    public void setBankAddress(String bankAddress) {
        mBankAddress = bankAddress;
    }

    public String getBankBranchName() {
        return mBankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        mBankBranchName = bankBranchName;
    }

    public String getBankName() {
        return mBankName;
    }

    public void setBankName(String bankName) {
        mBankName = bankName;
    }

    public int getBasicDetailsId() {
        return mBasicDetailsId;
    }

    public void setBasicDetailsId(int basicDetailsId) {
        mBasicDetailsId = basicDetailsId;
    }

    public String getCancelledCheque() {
        return mCancelledCheque;
    }

    public void setCancelledCheque(String cancelledCheque) {
        mCancelledCheque = cancelledCheque;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getDeleted() {
        return mDeleted;
    }

    public void setDeleted(String deleted) {
        mDeleted = deleted;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getIfsc() {
        return mIfsc;
    }

    public void setIfsc(String ifsc) {
        mIfsc = ifsc;
    }

    public String getPayeeName() {
        return mPayeeName;
    }

    public void setPayeeName(String payeeName) {
        mPayeeName = payeeName;
    }

    public int getProccessId() {
        return mProccessId;
    }

    public void setProccessId(int proccessId) {
        mProccessId = proccessId;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

}
