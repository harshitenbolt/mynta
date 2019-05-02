package com.canvascoders.opaper.Beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankDetailResp {
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<BankDetail> data = null;

    @SerializedName("add_btn")
    @Expose
    private Boolean add_btn;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BankDetail> getData() {
        return data;
    }

    public void setData(List<BankDetail> data) {
        this.data = data;
    }

    public Boolean getAdd_btn() {
        return add_btn;
    }

    public void setAdd_btn(Boolean add_btn) {
        this.add_btn = add_btn;
    }


    public class BankDetail {

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
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

}
