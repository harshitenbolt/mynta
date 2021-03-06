
package com.canvascoders.opaper.Beans.VendorListResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Datum implements Serializable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("lms_id")
    @Expose
    private String lmsId;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("agent_id")
    @Expose
    private Integer agentId;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("aadhaar_name")
    @Expose
    private String aadhaarName;
    @SerializedName("aadhaar_dob")
    @Expose
    private String aadhaarDob;
    @SerializedName("aadhaar_no")
    @Expose
    private String aadhaarNo;
    @SerializedName("aadhaar_pincode")
    @Expose
    private String aadhaarPincode;
    @SerializedName("pan_name")
    @Expose
    private String panName;
    @SerializedName("pan_no")
    @Expose
    private String panNo;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("oracle_csv")
    @Expose
    private String oracleCsv;
    @SerializedName("download_date")
    @Expose
    private String downloadDate;
    @SerializedName("franking_status")
    @Expose
    private String frankingStatus;
    @SerializedName("is_franking_download")
    @Expose
    private String isFrankingDownload;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("is_mass_upload")
    @Expose
    private String isMassUpload;
    @SerializedName("is_mass_doc_upload")
    @Expose
    private String isMassDocUpload;
    @SerializedName("is_upload")
    @Expose
    private String isUpload;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("without_aadhaar_verified")
    @Expose
    private String withoutAadhaarVerified;
    @SerializedName("bank_detail_updation_require")
    @Expose
    private String bankDetailUpdationRequire;
    @SerializedName("is_addendum_in_progress")
    @Expose
    private String isAddendumInProgress;
    @SerializedName("no_of_time_addendum_sign")
    @Expose
    private Integer noOfTimeAddendumSign;
    @SerializedName("status_change_time")
    @Expose
    private Object statusChangeTime;
    @SerializedName("is_agreement_updation_require")
    @Expose
    private String isAgreementUpdationRequire;
    @SerializedName("proccess_id")
    @Expose
    private Integer proccessId;
    @SerializedName("pan")
    @Expose
    private String pan;
    @SerializedName("adhar_card_front")
    @Expose
    private String adharCardFront;
    @SerializedName("adhar_card_back")
    @Expose
    private String adharCardBack;
    @SerializedName("if_shop_act")
    @Expose
    private Object ifShopAct;
    @SerializedName("cancelled_cheque")
    @Expose
    private Object cancelledCheque;
    @SerializedName("noc")
    @Expose
    private String noc;
    @SerializedName("shop_image")
    @Expose
    private String shopImage;
    @SerializedName("mass_upload")
    @Expose
    private Object massUpload;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("father_name")
    @Expose
    private String fatherName;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("dc")
    @Expose
    private String dc;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("store_address")
    @Expose
    private String storeAddress;
    @SerializedName("license_no")
    @Expose
    private Object licenseNo;
    @SerializedName("if_gst")
    @Expose
    private String ifGst;
    @SerializedName("gstn")
    @Expose
    private Object gstn;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("rate_approve_date")
    @Expose
    private String rateApproveDate;
    @SerializedName("approved_by")
    @Expose
    private Integer approvedBy;
    @SerializedName("is_rate_approved")
    @Expose
    private String isRateApproved;
    @SerializedName("franking")
    @Expose
    private Object franking;
    @SerializedName("zone")
    @Expose
    private Object zone;
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
    private String chequeVerify;
    @SerializedName("fill_details")
    @Expose
    private String fillDetails;
    @SerializedName("upload_files")
    @Expose
    private String uploadFiles;
    @SerializedName("rate_send_for_approval")
    @Expose
    private String rateSendForApproval;
    @SerializedName("agreement")
    @Expose
    private String agreement;
    @SerializedName("gstdeclaration")
    @Expose
    private String gstdeclaration;
    @SerializedName("vendor_send_for_approval")
    @Expose
    private String vendorSendForApproval;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLmsId() {
        return lmsId;
    }

    public void setLmsId(String lmsId) {
        this.lmsId = lmsId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAadhaarName() {
        return aadhaarName;
    }

    public void setAadhaarName(String aadhaarName) {
        this.aadhaarName = aadhaarName;
    }

    public String getAadhaarDob() {
        return aadhaarDob;
    }

    public void setAadhaarDob(String aadhaarDob) {
        this.aadhaarDob = aadhaarDob;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getAadhaarPincode() {
        return aadhaarPincode;
    }

    public void setAadhaarPincode(String aadhaarPincode) {
        this.aadhaarPincode = aadhaarPincode;
    }

    public String getPanName() {
        return panName;
    }

    public void setPanName(String panName) {
        this.panName = panName;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOracleCsv() {
        return oracleCsv;
    }

    public void setOracleCsv(String oracleCsv) {
        this.oracleCsv = oracleCsv;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getFrankingStatus() {
        return frankingStatus;
    }

    public void setFrankingStatus(String frankingStatus) {
        this.frankingStatus = frankingStatus;
    }

    public String getIsFrankingDownload() {
        return isFrankingDownload;
    }

    public void setIsFrankingDownload(String isFrankingDownload) {
        this.isFrankingDownload = isFrankingDownload;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getIsMassUpload() {
        return isMassUpload;
    }

    public void setIsMassUpload(String isMassUpload) {
        this.isMassUpload = isMassUpload;
    }

    public String getIsMassDocUpload() {
        return isMassDocUpload;
    }

    public void setIsMassDocUpload(String isMassDocUpload) {
        this.isMassDocUpload = isMassDocUpload;
    }

    public String getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(String isUpload) {
        this.isUpload = isUpload;
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

    public String getWithoutAadhaarVerified() {
        return withoutAadhaarVerified;
    }

    public void setWithoutAadhaarVerified(String withoutAadhaarVerified) {
        this.withoutAadhaarVerified = withoutAadhaarVerified;
    }

    public String getBankDetailUpdationRequire() {
        return bankDetailUpdationRequire;
    }

    public void setBankDetailUpdationRequire(String bankDetailUpdationRequire) {
        this.bankDetailUpdationRequire = bankDetailUpdationRequire;
    }

    public String getIsAddendumInProgress() {
        return isAddendumInProgress;
    }

    public void setIsAddendumInProgress(String isAddendumInProgress) {
        this.isAddendumInProgress = isAddendumInProgress;
    }

    public Integer getNoOfTimeAddendumSign() {
        return noOfTimeAddendumSign;
    }

    public void setNoOfTimeAddendumSign(Integer noOfTimeAddendumSign) {
        this.noOfTimeAddendumSign = noOfTimeAddendumSign;
    }

    public Object getStatusChangeTime() {
        return statusChangeTime;
    }

    public void setStatusChangeTime(Object statusChangeTime) {
        this.statusChangeTime = statusChangeTime;
    }

    public String getIsAgreementUpdationRequire() {
        return isAgreementUpdationRequire;
    }

    public void setIsAgreementUpdationRequire(String isAgreementUpdationRequire) {
        this.isAgreementUpdationRequire = isAgreementUpdationRequire;
    }

    public Integer getProccessId() {
        return proccessId;
    }

    public void setProccessId(Integer proccessId) {
        this.proccessId = proccessId;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAdharCardFront() {
        return adharCardFront;
    }

    public void setAdharCardFront(String adharCardFront) {
        this.adharCardFront = adharCardFront;
    }

    public String getAdharCardBack() {
        return adharCardBack;
    }

    public void setAdharCardBack(String adharCardBack) {
        this.adharCardBack = adharCardBack;
    }

    public Object getIfShopAct() {
        return ifShopAct;
    }

    public void setIfShopAct(Object ifShopAct) {
        this.ifShopAct = ifShopAct;
    }

    public Object getCancelledCheque() {
        return cancelledCheque;
    }

    public void setCancelledCheque(Object cancelledCheque) {
        this.cancelledCheque = cancelledCheque;
    }

    public String getNoc() {
        return noc;
    }

    public void setNoc(String noc) {
        this.noc = noc;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public Object getMassUpload() {
        return massUpload;
    }

    public void setMassUpload(Object massUpload) {
        this.massUpload = massUpload;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public Object getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(Object licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getIfGst() {
        return ifGst;
    }

    public void setIfGst(String ifGst) {
        this.ifGst = ifGst;
    }

    public Object getGstn() {
        return gstn;
    }

    public void setGstn(Object gstn) {
        this.gstn = gstn;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRateApproveDate() {
        return rateApproveDate;
    }

    public void setRateApproveDate(String rateApproveDate) {
        this.rateApproveDate = rateApproveDate;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getIsRateApproved() {
        return isRateApproved;
    }

    public void setIsRateApproved(String isRateApproved) {
        this.isRateApproved = isRateApproved;
    }

    public Object getFranking() {
        return franking;
    }

    public void setFranking(Object franking) {
        this.franking = franking;
    }

    public Object getZone() {
        return zone;
    }

    public void setZone(Object zone) {
        this.zone = zone;
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

    public String getChequeVerify() {
        return chequeVerify;
    }

    public void setChequeVerify(String chequeVerify) {
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

    public String getRateSendForApproval() {
        return rateSendForApproval;
    }

    public void setRateSendForApproval(String rateSendForApproval) {
        this.rateSendForApproval = rateSendForApproval;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getGstdeclaration() {
        return gstdeclaration;
    }

    public void setGstdeclaration(String gstdeclaration) {
        this.gstdeclaration = gstdeclaration;
    }

    public String getVendorSendForApproval() {
        return vendorSendForApproval;
    }

    public void setVendorSendForApproval(String vendorSendForApproval) {
        this.vendorSendForApproval = vendorSendForApproval;
    }

}
