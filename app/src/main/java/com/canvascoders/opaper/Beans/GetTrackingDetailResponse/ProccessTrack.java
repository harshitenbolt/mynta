
package com.canvascoders.opaper.Beans.GetTrackingDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProccessTrack {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("mobile_verify")
    @Expose
    private String mobileVerify;
    @SerializedName("location_verify")
    @Expose
    private String locationVerify;
    @SerializedName("aadhaar_verify")
    @Expose
    private String aadhaarVerify;
    @SerializedName("pan_verify")
    @Expose
    private String panVerify;
    @SerializedName("cheque_verify")
    @Expose
    private Integer chequeVerify;
    @SerializedName("fill_details")
    @Expose
    private String fillDetails;
    @SerializedName("upload_files")
    @Expose
    private String uploadFiles;
    @SerializedName("delivery_boy")
    @Expose
    private String deliveryBoy;
    @SerializedName("rate")
    @Expose
    private Integer rate;
    @SerializedName("rate_send_for_approval")
    @Expose
    private Integer rateSendForApproval;
    @SerializedName("agreement")
    @Expose
    private String agreement;
    @SerializedName("ecom_agreement")
    @Expose
    private String ecomAgreement;
    @SerializedName("noc")
    @Expose
    private String noc;
    @SerializedName("gstdeclaration")
    @Expose
    private String gstdeclaration;
    @SerializedName("assessment_verify")
    @Expose
    private String assessmentVerify;
    @SerializedName("vendor_send_for_approval")
    @Expose
    private Integer vendorSendForApproval;
    @SerializedName("pan_approval")
    @Expose
    private String panApproval;
    @SerializedName("esign_link_send_date")
    @Expose
    private String esignLinkSendDate;
    @SerializedName("bank_detail_updation_require")
    @Expose
    private String bankDetailUpdationRequire;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("status_change_time")
    @Expose
    private String statusChangeTime;

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

    public String getMobileVerify() {
        return mobileVerify;
    }

    public void setMobileVerify(String mobileVerify) {
        this.mobileVerify = mobileVerify;
    }

    public String getLocationVerify() {
        return locationVerify;
    }

    public void setLocationVerify(String locationVerify) {
        this.locationVerify = locationVerify;
    }

    public String getAadhaarVerify() {
        return aadhaarVerify;
    }

    public void setAadhaarVerify(String aadhaarVerify) {
        this.aadhaarVerify = aadhaarVerify;
    }

    public String getPanVerify() {
        return panVerify;
    }

    public void setPanVerify(String panVerify) {
        this.panVerify = panVerify;
    }

    public Integer getChequeVerify() {
        return chequeVerify;
    }

    public void setChequeVerify(Integer chequeVerify) {
        this.chequeVerify = chequeVerify;
    }

    public String getFillDetails() {
        return fillDetails;
    }

    public void setFillDetails(String fillDetails) {
        this.fillDetails = fillDetails;
    }

    public String getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(String uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public String getDeliveryBoy() {
        return deliveryBoy;
    }

    public void setDeliveryBoy(String deliveryBoy) {
        this.deliveryBoy = deliveryBoy;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getRateSendForApproval() {
        return rateSendForApproval;
    }

    public void setRateSendForApproval(Integer rateSendForApproval) {
        this.rateSendForApproval = rateSendForApproval;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getEcomAgreement() {
        return ecomAgreement;
    }

    public void setEcomAgreement(String ecomAgreement) {
        this.ecomAgreement = ecomAgreement;
    }

    public String getNoc() {
        return noc;
    }

    public void setNoc(String noc) {
        this.noc = noc;
    }

    public String getGstdeclaration() {
        return gstdeclaration;
    }

    public void setGstdeclaration(String gstdeclaration) {
        this.gstdeclaration = gstdeclaration;
    }

    public String getAssessmentVerify() {
        return assessmentVerify;
    }

    public void setAssessmentVerify(String assessmentVerify) {
        this.assessmentVerify = assessmentVerify;
    }

    public Integer getVendorSendForApproval() {
        return vendorSendForApproval;
    }

    public void setVendorSendForApproval(Integer vendorSendForApproval) {
        this.vendorSendForApproval = vendorSendForApproval;
    }

    public String getPanApproval() {
        return panApproval;
    }

    public void setPanApproval(String panApproval) {
        this.panApproval = panApproval;
    }

    public String getEsignLinkSendDate() {
        return esignLinkSendDate;
    }

    public void setEsignLinkSendDate(String esignLinkSendDate) {
        this.esignLinkSendDate = esignLinkSendDate;
    }

    public String getBankDetailUpdationRequire() {
        return bankDetailUpdationRequire;
    }

    public void setBankDetailUpdationRequire(String bankDetailUpdationRequire) {
        this.bankDetailUpdationRequire = bankDetailUpdationRequire;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getStatusChangeTime() {
        return statusChangeTime;
    }

    public void setStatusChangeTime(String statusChangeTime) {
        this.statusChangeTime = statusChangeTime;
    }

}
