
package com.canvascoders.opaper.Beans.GetTrackingDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackDetail {

    @SerializedName("location_verify")
    @Expose
    private Integer locationVerify;
    @SerializedName("aadhaar_verify")
    @Expose
    private Integer aadhaarVerify;
    @SerializedName("pan_verify")
    @Expose
    private Integer panVerify;
    @SerializedName("cheque_verify")
    @Expose
    private Integer chequeVerify;
    @SerializedName("fill_details")
    @Expose
    private Integer fillDetails;

    @SerializedName("fill_store_details")
    @Expose
    private Integer fillStoreDetails;
    @SerializedName("fill_owner_details")
    @Expose
    private Integer fillOwnerDetails;

    public Integer getFillStoreDetails() {
        return fillStoreDetails;
    }

    public void setFillStoreDetails(Integer fillStoreDetails) {
        this.fillStoreDetails = fillStoreDetails;
    }

    public Integer getFillOwnerDetails() {
        return fillOwnerDetails;
    }

    public void setFillOwnerDetails(Integer fillOwnerDetails) {
        this.fillOwnerDetails = fillOwnerDetails;
    }

    public Integer getFillGstDetails() {
        return fillGstDetails;
    }

    public void setFillGstDetails(Integer fillGstDetails) {
        this.fillGstDetails = fillGstDetails;
    }

    @SerializedName("fill_gst_details")
    @Expose
    private Integer fillGstDetails;



    @SerializedName("upload_files")
    @Expose
    private Integer uploadFiles;
    @SerializedName("delivery_boy")
    @Expose
    private Integer deliveryBoy;
    @SerializedName("rate")
    @Expose
    private Integer rate;
    @SerializedName("rate_send_for_approval")
    @Expose
    private Integer rateSendForApproval;
    @SerializedName("agreement")
    @Expose
    private Integer agreement;
    @SerializedName("ecom_agreement")
    @Expose
    private Integer ecomAgreement;
    @SerializedName("noc")
    @Expose
    private Integer noc;
    @SerializedName("gstdeclaration")
    @Expose
    private Integer gstdeclaration;
    @SerializedName("vendor_send_for_approval")
    @Expose
    private Integer vendorSendForApproval;
    @SerializedName("pan_approval")
    @Expose
    private Integer panApproval;
    @SerializedName("esign_link_send_date")
    @Expose
    private Integer esignLinkSendDate;

    public Integer getLocationVerify() {
        return locationVerify;
    }

    public void setLocationVerify(Integer locationVerify) {
        this.locationVerify = locationVerify;
    }

    public Integer getAadhaarVerify() {
        return aadhaarVerify;
    }

    public void setAadhaarVerify(Integer aadhaarVerify) {
        this.aadhaarVerify = aadhaarVerify;
    }

    public Integer getPanVerify() {
        return panVerify;
    }

    public void setPanVerify(Integer panVerify) {
        this.panVerify = panVerify;
    }

    public Integer getChequeVerify() {
        return chequeVerify;
    }

    public void setChequeVerify(Integer chequeVerify) {
        this.chequeVerify = chequeVerify;
    }

    public Integer getFillDetails() {
        return fillDetails;
    }

    public void setFillDetails(Integer fillDetails) {
        this.fillDetails = fillDetails;
    }

    public Integer getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(Integer uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public Integer getDeliveryBoy() {
        return deliveryBoy;
    }

    public void setDeliveryBoy(Integer deliveryBoy) {
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

    public Integer getAgreement() {
        return agreement;
    }

    public void setAgreement(Integer agreement) {
        this.agreement = agreement;
    }

    public Integer getEcomAgreement() {
        return ecomAgreement;
    }

    public void setEcomAgreement(Integer ecomAgreement) {
        this.ecomAgreement = ecomAgreement;
    }

    public Integer getNoc() {
        return noc;
    }

    public void setNoc(Integer noc) {
        this.noc = noc;
    }

    public Integer getGstdeclaration() {
        return gstdeclaration;
    }

    public void setGstdeclaration(Integer gstdeclaration) {
        this.gstdeclaration = gstdeclaration;
    }

    public Integer getVendorSendForApproval() {
        return vendorSendForApproval;
    }

    public void setVendorSendForApproval(Integer vendorSendForApproval) {
        this.vendorSendForApproval = vendorSendForApproval;
    }

    public Integer getPanApproval() {
        return panApproval;
    }

    public void setPanApproval(Integer panApproval) {
        this.panApproval = panApproval;
    }

    public Integer getEsignLinkSendDate() {
        return esignLinkSendDate;
    }

    public void setEsignLinkSendDate(Integer esignLinkSendDate) {
        this.esignLinkSendDate = esignLinkSendDate;
    }

}
