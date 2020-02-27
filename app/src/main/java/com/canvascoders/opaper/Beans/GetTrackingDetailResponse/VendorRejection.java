package com.canvascoders.opaper.Beans.GetTrackingDetailResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VendorRejection {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("approval_vendor_id")
    @Expose
    private Integer approvalVendorId;
    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("location_verify")
    @Expose
    private Integer locationVerify;
    @SerializedName("location_verify_solved")
    @Expose
    private Integer locationVerifySolved;
    @SerializedName("location_verify_remark")
    @Expose
    private String locationVerifyRemark;
    @SerializedName("aadhaar_verify")
    @Expose
    private Integer aadhaarVerify;
    @SerializedName("aadhaar_verify_solved")
    @Expose
    private Integer aadhaarVerifySolved;
    @SerializedName("aadhaar_verify_remark")
    @Expose
    private String aadhaarVerifyRemark;
    @SerializedName("pan_verify")
    @Expose
    private Integer panVerify;
    @SerializedName("pan_verify_solved")
    @Expose
    private Integer panVerifySolved;
    @SerializedName("pan_verify_remark")
    @Expose
    private String panVerifyRemark;
    @SerializedName("cheque_verify")
    @Expose
    private Integer chequeVerify;
    @SerializedName("cheque_verify_solved")
    @Expose
    private Integer chequeVerifySolved;
    @SerializedName("cheque_verify_remark")
    @Expose
    private String chequeVerifyRemark;
    @SerializedName("fill_details")
    @Expose
    private Integer fillDetails;
    @SerializedName("fill_details_solved")
    @Expose
    private Integer fillDetailsSolved;
    @SerializedName("fill_details_remark")
    @Expose
    private String fillDetailsRemark;
    @SerializedName("fill_store_details")
    @Expose
    private Integer fillStoreDetails;
    @SerializedName("fill_store_details_solved")
    @Expose
    private Integer fillStoreDetailsSolved;
    @SerializedName("fill_store_details_remark")
    @Expose
    private String fillStoreDetailsRemark;
    @SerializedName("fill_owner_details")
    @Expose
    private Integer fillOwnerDetails;
    @SerializedName("fill_owner_details_solved")
    @Expose
    private Integer fillOwnerDetailsSolved;
    @SerializedName("fill_owner_details_remark")
    @Expose
    private String fillOwnerDetailsRemark;
    @SerializedName("fill_gst_details")
    @Expose
    private Integer fillGstDetails;
    @SerializedName("fill_gst_details_solved")
    @Expose
    private Integer fillGstDetailsSolved;
    @SerializedName("fill_gst_details_remark")
    @Expose
    private String fillGstDetailsRemark;
    @SerializedName("upload_files")
    @Expose
    private Integer uploadFiles;
    @SerializedName("upload_files_solved")
    @Expose
    private Integer uploadFilesSolved;
    @SerializedName("upload_files_remark")
    @Expose
    private String uploadFilesRemark;
    @SerializedName("delivery_boy")
    @Expose
    private Integer deliveryBoy;
    @SerializedName("delivery_boy_solved")
    @Expose
    private Integer deliveryBoySolved;
    @SerializedName("delivery_boy_remark")
    @Expose
    private String deliveryBoyRemark;
    @SerializedName("rate")
    @Expose
    private Integer rate;
    @SerializedName("rate_solved")
    @Expose
    private Integer rateSolved;
    @SerializedName("rate_remark")
    @Expose
    private String rateRemark;
    @SerializedName("rate_send_for_approval")
    @Expose
    private Integer rateSendForApproval;
    @SerializedName("rate_send_for_approval_solved")
    @Expose
    private Integer rateSendForApprovalSolved;
    @SerializedName("rate_send_for_approval_remark")
    @Expose
    private String rateSendForApprovalRemark;
    @SerializedName("agreement")
    @Expose
    private Integer agreement;
    @SerializedName("agreement_solved")
    @Expose
    private Integer agreementSolved;
    @SerializedName("agreement_remark")
    @Expose
    private String agreementRemark;
    @SerializedName("ecom_agreement")
    @Expose
    private Integer ecomAgreement;
    @SerializedName("ecom_agreement_solved")
    @Expose
    private Integer ecomAgreementSolved;
    @SerializedName("ecom_agreement_remark")
    @Expose
    private String ecomAgreementRemark;
    @SerializedName("noc")
    @Expose
    private Integer noc;
    @SerializedName("noc_solved")
    @Expose
    private Integer nocSolved;
    @SerializedName("noc_remark")
    @Expose
    private String nocRemark;
    @SerializedName("gstdeclaration")
    @Expose
    private Integer gstdeclaration;
    @SerializedName("gstdeclaration_solved")
    @Expose
    private Integer gstdeclarationSolved;
    @SerializedName("gstdeclaration_remark")
    @Expose
    private String gstdeclarationRemark;
    @SerializedName("vendor_send_for_approval")
    @Expose
    private Integer vendorSendForApproval;
    @SerializedName("vendor_send_for_approval_solved")
    @Expose
    private Integer vendorSendForApprovalSolved;
    @SerializedName("vendor_send_for_approval_remark")
    @Expose
    private String vendorSendForApprovalRemark;
    @SerializedName("pan_approval")
    @Expose
    private Integer panApproval;
    @SerializedName("pan_approval_solved")
    @Expose
    private Integer panApprovalSolved;
    @SerializedName("pan_approval_remark")
    @Expose
    private String panApprovalRemark;
    @SerializedName("esign_link_send_date")
    @Expose
    private Integer esignLinkSendDate;
    @SerializedName("esign_link_send_date_solved")
    @Expose
    private Integer esignLinkSendDateSolved;
    @SerializedName("esign_link_send_date_remark")
    @Expose
    private String esignLinkSendDateRemark;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApprovalVendorId() {
        return approvalVendorId;
    }

    public void setApprovalVendorId(Integer approvalVendorId) {
        this.approvalVendorId = approvalVendorId;
    }

    public Integer getProccessId() {
        return proccessId;
    }

    public void setProccessId(Integer proccessId) {
        this.proccessId = proccessId;
    }

    public Integer getLocationVerify() {
        return locationVerify;
    }

    public void setLocationVerify(Integer locationVerify) {
        this.locationVerify = locationVerify;
    }

    public Integer getLocationVerifySolved() {
        return locationVerifySolved;
    }

    public void setLocationVerifySolved(Integer locationVerifySolved) {
        this.locationVerifySolved = locationVerifySolved;
    }

    public String getLocationVerifyRemark() {
        return locationVerifyRemark;
    }

    public void setLocationVerifyRemark(String locationVerifyRemark) {
        this.locationVerifyRemark = locationVerifyRemark;
    }

    public Integer getAadhaarVerify() {
        return aadhaarVerify;
    }

    public void setAadhaarVerify(Integer aadhaarVerify) {
        this.aadhaarVerify = aadhaarVerify;
    }

    public Integer getAadhaarVerifySolved() {
        return aadhaarVerifySolved;
    }

    public void setAadhaarVerifySolved(Integer aadhaarVerifySolved) {
        this.aadhaarVerifySolved = aadhaarVerifySolved;
    }

    public String getAadhaarVerifyRemark() {
        return aadhaarVerifyRemark;
    }

    public void setAadhaarVerifyRemark(String aadhaarVerifyRemark) {
        this.aadhaarVerifyRemark = aadhaarVerifyRemark;
    }

    public Integer getPanVerify() {
        return panVerify;
    }

    public void setPanVerify(Integer panVerify) {
        this.panVerify = panVerify;
    }

    public Integer getPanVerifySolved() {
        return panVerifySolved;
    }

    public void setPanVerifySolved(Integer panVerifySolved) {
        this.panVerifySolved = panVerifySolved;
    }

    public String getPanVerifyRemark() {
        return panVerifyRemark;
    }

    public void setPanVerifyRemark(String panVerifyRemark) {
        this.panVerifyRemark = panVerifyRemark;
    }

    public Integer getChequeVerify() {
        return chequeVerify;
    }

    public void setChequeVerify(Integer chequeVerify) {
        this.chequeVerify = chequeVerify;
    }

    public Integer getChequeVerifySolved() {
        return chequeVerifySolved;
    }

    public void setChequeVerifySolved(Integer chequeVerifySolved) {
        this.chequeVerifySolved = chequeVerifySolved;
    }

    public String getChequeVerifyRemark() {
        return chequeVerifyRemark;
    }

    public void setChequeVerifyRemark(String chequeVerifyRemark) {
        this.chequeVerifyRemark = chequeVerifyRemark;
    }

    public Integer getFillDetails() {
        return fillDetails;
    }

    public void setFillDetails(Integer fillDetails) {
        this.fillDetails = fillDetails;
    }

    public Integer getFillDetailsSolved() {
        return fillDetailsSolved;
    }

    public void setFillDetailsSolved(Integer fillDetailsSolved) {
        this.fillDetailsSolved = fillDetailsSolved;
    }

    public String getFillDetailsRemark() {
        return fillDetailsRemark;
    }

    public void setFillDetailsRemark(String fillDetailsRemark) {
        this.fillDetailsRemark = fillDetailsRemark;
    }

    public Integer getFillStoreDetails() {
        return fillStoreDetails;
    }

    public void setFillStoreDetails(Integer fillStoreDetails) {
        this.fillStoreDetails = fillStoreDetails;
    }

    public Integer getFillStoreDetailsSolved() {
        return fillStoreDetailsSolved;
    }

    public void setFillStoreDetailsSolved(Integer fillStoreDetailsSolved) {
        this.fillStoreDetailsSolved = fillStoreDetailsSolved;
    }

    public String getFillStoreDetailsRemark() {
        return fillStoreDetailsRemark;
    }

    public void setFillStoreDetailsRemark(String fillStoreDetailsRemark) {
        this.fillStoreDetailsRemark = fillStoreDetailsRemark;
    }

    public Integer getFillOwnerDetails() {
        return fillOwnerDetails;
    }

    public void setFillOwnerDetails(Integer fillOwnerDetails) {
        this.fillOwnerDetails = fillOwnerDetails;
    }

    public Integer getFillOwnerDetailsSolved() {
        return fillOwnerDetailsSolved;
    }

    public void setFillOwnerDetailsSolved(Integer fillOwnerDetailsSolved) {
        this.fillOwnerDetailsSolved = fillOwnerDetailsSolved;
    }

    public String getFillOwnerDetailsRemark() {
        return fillOwnerDetailsRemark;
    }

    public void setFillOwnerDetailsRemark(String fillOwnerDetailsRemark) {
        this.fillOwnerDetailsRemark = fillOwnerDetailsRemark;
    }

    public Integer getFillGstDetails() {
        return fillGstDetails;
    }

    public void setFillGstDetails(Integer fillGstDetails) {
        this.fillGstDetails = fillGstDetails;
    }

    public Integer getFillGstDetailsSolved() {
        return fillGstDetailsSolved;
    }

    public void setFillGstDetailsSolved(Integer fillGstDetailsSolved) {
        this.fillGstDetailsSolved = fillGstDetailsSolved;
    }

    public String getFillGstDetailsRemark() {
        return fillGstDetailsRemark;
    }

    public void setFillGstDetailsRemark(String fillGstDetailsRemark) {
        this.fillGstDetailsRemark = fillGstDetailsRemark;
    }

    public Integer getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(Integer uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public Integer getUploadFilesSolved() {
        return uploadFilesSolved;
    }

    public void setUploadFilesSolved(Integer uploadFilesSolved) {
        this.uploadFilesSolved = uploadFilesSolved;
    }

    public String getUploadFilesRemark() {
        return uploadFilesRemark;
    }

    public void setUploadFilesRemark(String uploadFilesRemark) {
        this.uploadFilesRemark = uploadFilesRemark;
    }

    public Integer getDeliveryBoy() {
        return deliveryBoy;
    }

    public void setDeliveryBoy(Integer deliveryBoy) {
        this.deliveryBoy = deliveryBoy;
    }

    public Integer getDeliveryBoySolved() {
        return deliveryBoySolved;
    }

    public void setDeliveryBoySolved(Integer deliveryBoySolved) {
        this.deliveryBoySolved = deliveryBoySolved;
    }

    public String getDeliveryBoyRemark() {
        return deliveryBoyRemark;
    }

    public void setDeliveryBoyRemark(String deliveryBoyRemark) {
        this.deliveryBoyRemark = deliveryBoyRemark;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getRateSolved() {
        return rateSolved;
    }

    public void setRateSolved(Integer rateSolved) {
        this.rateSolved = rateSolved;
    }

    public String getRateRemark() {
        return rateRemark;
    }

    public void setRateRemark(String rateRemark) {
        this.rateRemark = rateRemark;
    }

    public Integer getRateSendForApproval() {
        return rateSendForApproval;
    }

    public void setRateSendForApproval(Integer rateSendForApproval) {
        this.rateSendForApproval = rateSendForApproval;
    }

    public Integer getRateSendForApprovalSolved() {
        return rateSendForApprovalSolved;
    }

    public void setRateSendForApprovalSolved(Integer rateSendForApprovalSolved) {
        this.rateSendForApprovalSolved = rateSendForApprovalSolved;
    }

    public String getRateSendForApprovalRemark() {
        return rateSendForApprovalRemark;
    }

    public void setRateSendForApprovalRemark(String rateSendForApprovalRemark) {
        this.rateSendForApprovalRemark = rateSendForApprovalRemark;
    }

    public Integer getAgreement() {
        return agreement;
    }

    public void setAgreement(Integer agreement) {
        this.agreement = agreement;
    }

    public Integer getAgreementSolved() {
        return agreementSolved;
    }

    public void setAgreementSolved(Integer agreementSolved) {
        this.agreementSolved = agreementSolved;
    }

    public String getAgreementRemark() {
        return agreementRemark;
    }

    public void setAgreementRemark(String agreementRemark) {
        this.agreementRemark = agreementRemark;
    }

    public Integer getEcomAgreement() {
        return ecomAgreement;
    }

    public void setEcomAgreement(Integer ecomAgreement) {
        this.ecomAgreement = ecomAgreement;
    }

    public Integer getEcomAgreementSolved() {
        return ecomAgreementSolved;
    }

    public void setEcomAgreementSolved(Integer ecomAgreementSolved) {
        this.ecomAgreementSolved = ecomAgreementSolved;
    }

    public String getEcomAgreementRemark() {
        return ecomAgreementRemark;
    }

    public void setEcomAgreementRemark(String ecomAgreementRemark) {
        this.ecomAgreementRemark = ecomAgreementRemark;
    }

    public Integer getNoc() {
        return noc;
    }

    public void setNoc(Integer noc) {
        this.noc = noc;
    }

    public Integer getNocSolved() {
        return nocSolved;
    }

    public void setNocSolved(Integer nocSolved) {
        this.nocSolved = nocSolved;
    }

    public String getNocRemark() {
        return nocRemark;
    }

    public void setNocRemark(String nocRemark) {
        this.nocRemark = nocRemark;
    }

    public Integer getGstdeclaration() {
        return gstdeclaration;
    }

    public void setGstdeclaration(Integer gstdeclaration) {
        this.gstdeclaration = gstdeclaration;
    }

    public Integer getGstdeclarationSolved() {
        return gstdeclarationSolved;
    }

    public void setGstdeclarationSolved(Integer gstdeclarationSolved) {
        this.gstdeclarationSolved = gstdeclarationSolved;
    }

    public String getGstdeclarationRemark() {
        return gstdeclarationRemark;
    }

    public void setGstdeclarationRemark(String gstdeclarationRemark) {
        this.gstdeclarationRemark = gstdeclarationRemark;
    }

    public Integer getVendorSendForApproval() {
        return vendorSendForApproval;
    }

    public void setVendorSendForApproval(Integer vendorSendForApproval) {
        this.vendorSendForApproval = vendorSendForApproval;
    }

    public Integer getVendorSendForApprovalSolved() {
        return vendorSendForApprovalSolved;
    }

    public void setVendorSendForApprovalSolved(Integer vendorSendForApprovalSolved) {
        this.vendorSendForApprovalSolved = vendorSendForApprovalSolved;
    }

    public String getVendorSendForApprovalRemark() {
        return vendorSendForApprovalRemark;
    }

    public void setVendorSendForApprovalRemark(String vendorSendForApprovalRemark) {
        this.vendorSendForApprovalRemark = vendorSendForApprovalRemark;
    }

    public Integer getPanApproval() {
        return panApproval;
    }

    public void setPanApproval(Integer panApproval) {
        this.panApproval = panApproval;
    }

    public Integer getPanApprovalSolved() {
        return panApprovalSolved;
    }

    public void setPanApprovalSolved(Integer panApprovalSolved) {
        this.panApprovalSolved = panApprovalSolved;
    }

    public String getPanApprovalRemark() {
        return panApprovalRemark;
    }

    public void setPanApprovalRemark(String panApprovalRemark) {
        this.panApprovalRemark = panApprovalRemark;
    }

    public Integer getEsignLinkSendDate() {
        return esignLinkSendDate;
    }

    public void setEsignLinkSendDate(Integer esignLinkSendDate) {
        this.esignLinkSendDate = esignLinkSendDate;
    }

    public Integer getEsignLinkSendDateSolved() {
        return esignLinkSendDateSolved;
    }

    public void setEsignLinkSendDateSolved(Integer esignLinkSendDateSolved) {
        this.esignLinkSendDateSolved = esignLinkSendDateSolved;
    }

    public String getEsignLinkSendDateRemark() {
        return esignLinkSendDateRemark;
    }

    public void setEsignLinkSendDateRemark(String esignLinkSendDateRemark) {
        this.esignLinkSendDateRemark = esignLinkSendDateRemark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

}
